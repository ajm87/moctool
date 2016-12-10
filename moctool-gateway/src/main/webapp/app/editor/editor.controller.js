(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .controller('EditorController', EditorController)
        .controller('SimulateModalController', SimulateModalController)
        .config(ToastrConfigurer);

    EditorController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Simulate', 'Load', 'NfaToDfa', '$uibModal', '$compile', 'CytoscapeService', 'toastr'];

    //TODO: Autoconnect new nodes
    function EditorController ($scope, Principal, LoginService, $state, Simulate, Load, NfaToDfa, $uibModal, $compile, CytoscapeService, toastr) {
        var cy = CytoscapeService.getCytoscapeInstance($scope);
        var stateCount = 0;
        cy.on('tap', function(e) {
            if(e.cyTarget === cy) {
            var setID = stateCount.toString(),
                offset = $("#cyCanvas").offset(),
                position = {
                    x: e.originalEvent.x - offset.left,
                    y: e.originalEvent.y - offset.top
                };
            var addedNode = cy.add([{
                group: "nodes",
                data: { id: "n" + setID, name: setID },
                renderedPosition: {
                    x: position.x,
                    y: position.y
                },
            }]);
            addedNode.data('initial', 'false');
            addedNode.data('accept', 'false');
            stateCount++;
            addedNode.qtip({
                content: {
                    text: function(api) {
                        var tipScope = $scope.$new();
                        tipScope.state = addedNode;
                        tipScope.name = addedNode.data('name');
                        var ele = angular.element('<div class="form-group"><label>Input a name for this state:</label><input type="text" class="form-control state-name" ng-model="name" ng-change="vm.setStateName(state, name)"></div><div class="form-actions"><button type="button" class="btn btn-primary" ng-click="vm.hideQtip(state)">OK</button></div>');
                        $compile(ele)(tipScope);
                        return ele;
                    }
                },
                show: {
                    event: 'click'
                },
                hide: {
                    event: 'unfocus'
                },
                position: {
                    my: 'top center',
                    at: 'bottom center'
                },
                style: {
                    classes: 'qtip-bootstrap qtip-shadow'
                },
                events: {
                    visible: function(event, api) {
                        setTimeout(function() {
                        $('.state-name').focus();
                        $('.state-name').on('keypress', function(e) {
                            if(e.which === 13) {
                                api.hide();
                            }
                        });
                        }, 1);
                    }
                }
            });

            }
        });

        var vm = this;
        vm.stateCount = 0;
        vm.jsonifyAutomaton = jsonifyAutomaton;
        vm.loadAutomatonFromServer = loadAutomatonFromServer;
        vm.convertNfaToDfa = convertNfaToDfa;
        vm.setSymbol = setSymbol;
        vm.simulate = simulate;
        vm.getAllSteps = getAllSteps;
        vm.getNextStep = getNextStep;
        vm.simulationStep = 1;
        vm.currentSimulation = 0;
        vm.previousConnection;
        vm.previousNfaTransitions;
        vm.setStateName = setStateName;
        vm.hideQtip = hideQtip;
        vm.currentSymbol;
        vm.simulationInput;
        vm.simulateNfa = simulateNfa;

        init();
        function init() {
            $('#sim-fast-backward').tooltip();
        }

        function hideQtip(state) {
            state.qtip('api').hide();
        }

        function setStateName(state, name) {
            state.data('name', name);
        }

        function setSymbol(connector, symbol) {
            if(!symbol || symbol.length === 0) {
                connector.data('label', '\u03b5');
            } else {
                connector.data('label', symbol);
            }
        }

        function convertNfaToDfa() {
            var automatonObj = jsonifyAutomaton();
            NfaToDfa.save(automatonObj, function(data) {
                console.log('Got converted automaton: ', data);
                clearCanvas();
                cy.add(data.elements);
                cy.layout({name: 'dagre', rankDir: 'LR', fit: false});
                cy.center();
            });
        }

        function clearCanvas() {
            cy.remove('*');
        }

        function loadAutomatonFromServer() {
                Load.get(function(data){
                    loadAutomaton(data);
                });
        }

        function simulate() {
            vm.simulationStep = 1;
            var modalScope = $scope.$new();
            var automaton = jsonifyAutomaton();

            var validation = validateBeforeSimulation(automaton);
            modalScope.validation = validation;

            var modal = $uibModal.open({
                templateUrl: 'app/editor/simulate.modal.html',
                controller: 'SimulateModalController',
                controllerAs: 'vm',
                scope: modalScope
            });

            modal.result.then(function (selected) {
                var toSend = {
                    input: selected.value.split(''),
                    finiteAutomaton: automaton
                };
                vm.simulationInput = selected.value.split('');
                vm.currentSymbol = vm.simulationInput[vm.simulationStep - 1];
                Simulate.saveDfa(toSend, function(data) {
                    if(!angular.isUndefined(vm.previousConnection)) {
                        vm.previousConnection.style({
                            'line-color': 'black',
                            'target-arrow-color': 'black'
                        });
                    }
                    vm.simulationStep = 1;
                    console.log('Got simulation ID: ', data.id);
                    vm.currentSimulation = data.id;
                });

            });
        }

        function simulateNfa() {
                        vm.simulationStep = 1;
            var modalScope = $scope.$new();
            var automaton = jsonifyAutomaton();

            var validation = validateBeforeSimulation(automaton);
            modalScope.validation = validation;

            var modal = $uibModal.open({
                templateUrl: 'app/editor/simulate.modal.html',
                controller: 'SimulateModalController',
                controllerAs: 'vm',
                scope: modalScope
            });

            modal.result.then(function (selected) {
                var toSend = {
                    input: selected.value.split(''),
                    finiteAutomaton: automaton
                };
                vm.simulationInput = selected.value.split('');
                vm.currentSymbol = vm.simulationInput[vm.simulationStep - 1];
                Simulate.saveNfa(toSend, function(data) {
                    if(!angular.isUndefined(vm.previousConnection)) {
                        vm.previousConnection.style({
                            'line-color': 'black',
                            'target-arrow-color': 'black'
                        });
                    }
                    vm.simulationStep = 1;
                    console.log('Got simulation ID: ', data.id);
                    vm.currentSimulation = data.id;
                });

            });
        }

        function validateBeforeSimulation(automaton) {
            //is there an orphan node?
            var returnObj = {
                hasInitial: false,
                hasAccept: false
            };
            var alphabet = [];
            var linkedStates = [];
            angular.forEach(automaton.elements, function(value, key) {
                if(!angular.isUndefined(value.data.initial) && value.data.initial === "true") {
                    returnObj.hasInitial = true;
                }
                if(!angular.isUndefined(value.data.accept) && value.data.accept === "true") {
                    returnObj.hasAccept = true;
                }
                if(value.group === "edges") {
                    alphabet.push(value.data.label);
                    linkedStates.push(value.data.source);
                    linkedStates.push(value.data.target);
                }
            });

            angular.forEach(automaton.elements, function(value, key) {
                if(value.group === "nodes") {
                    if(linkedStates.indexOf(value.data.id) === -1) {
                        returnObj.orphanedState = true;
                        returnObj.orphanedStateName = value.data.name;
                    }
                }
            });

            returnObj.alphabet = alphabet;
            return returnObj;
        }

        function getAllSteps() {
            Simulate.getAllSteps({simulationId: vm.currentSimulation}, function(data) {
                console.log('Got simulation: ', data);
            });
        }

        function getNextStep(isNfa) {
            vm.currentSymbol = vm.simulationInput[vm.simulationStep - 1];
            Simulate.getStep({simulationId: vm.currentSimulation, stepId: vm.simulationStep}, function(data) {
                console.log('Got simulation step: ', data);
                if(isNfa) {
                    parseNextStepNfa(data);
                } else {
                    parseNextStep(data);
                }
            }, function(response) {
                // step not found
                if(response.status === 404) {
                    console.log('Step not found');
                }
            });
            vm.simulationStep++;
        }

        function parseNextStepNfa(data) {
            var startCollection = cy.collection();
            var finishCollection = cy.collection();
            angular.forEach(data.startActiveStates, function(value, key) {
                startCollection = startCollection.add(cy.getElementById(value.id));
            });
            angular.forEach(data.finishActiveStates, function(value, key) {
                finishCollection = finishCollection.add(cy.getElementById(value.id));
            });
            startCollection.style({
                'border-color': 'black'
            });
            finishCollection.style({
                'border-color': 'orange'
            });
            var transitions = startCollection.edgesTo(finishCollection);
            if(!angular.isUndefined(vm.previousNfaTransitions)) {
                vm.previousNfaTransitions.style({
                    'line-color': 'black',
                    'target-arrow-color': 'black'
                });
            }
            transitions.style({
                'line-color': 'orange',
                'target-arrow-color': 'orange'
            });
            if(data.finalStep && data.currentState === 'ACCEPT') {
                toastr.success('Input string accepted!', 'Simulation');
                transitions.style({
                    'line-color': 'green',
                    'target-arrow-color': 'green'
                });
                finishCollection.style({
                    'border-color': 'green'
                });
            } else if(data.finalStep && data.currentState === 'REJECT') {
                toastr.error('Input string rejected!', 'Simulation');
                transitions.style({
                    'line-color': 'red',
                    'target-arrow-color': 'red'
                });
                finishCollection.style({
                    'border-color': 'red'
                });
            }
            vm.previousNfaTransitions = transitions;
        }

        function parseNextStep(data) {
                var startNode = cy.getElementById(data.startState.id);
                var transitions = startNode.outgoers('edge');
                var matchingTransitions = transitions.filter("[label = '" + data.transitionSymbol + "']");
                if(!angular.isUndefined(vm.previousConnection)) {
                    vm.previousConnection.style({
                        'line-color': 'black',
                        'target-arrow-color': 'black'
                    });
                }
                matchingTransitions.style({
                    'line-color': 'orange',
                    'target-arrow-color': 'orange'
                });
                if(data.finalStep && data.currentState === 'ACCEPT') {
                    toastr.success('Input string accepted!', 'Simulation');
                    matchingTransitions.style({
                        'line-color': 'green',
                        'target-arrow-color': 'green'
                    });
                } else if(data.finalStep && data.currentState === 'REJECT') {
                    toastr.error('Input string rejected!', 'Simulation');
                    matchingTransitions.style({
                        'line-color': 'red',
                        'target-arrow-color': 'red'
                    });
                }
                vm.previousConnection = matchingTransitions;
        }

        function jsonifyAutomaton() {
            return {elements: cy.elements().jsons()};
        }
    }

    SimulateModalController.$inject = ['$uibModalInstance', '$scope'];
    function SimulateModalController($uibModalInstance, $scope) {
        
                    var vm = this;
                    vm.input = "";
                    vm.validation = $scope.validation;
                    vm.translateParams = {
                        stateName: vm.validation.orphanedStateName
                    };
                    vm.translateInput = {
                        invalidChars: []
                    };

                    vm.validateInput = function() {
                        vm.translateInput.invalidChars = [];
                        angular.forEach(vm.input.split(""), function(value, key) {
                            if(vm.validation.alphabet.indexOf(value) === -1) {
                                vm.translateInput.invalidChars.push(value);
                            }
                        });
                    }

                    vm.ok = function() {
                        $uibModalInstance.close({value: vm.input});
                    };

                    vm.cancel = function() {
                        $uibModalInstance.dismiss({value: 'cancel'});
                    }
    }
    
    ToastrConfigurer.$inject = ['toastrConfig'];
    function ToastrConfigurer(toastrConfig) {
        angular.extend(toastrConfig, {
            timeOut: 15000,
            positionClass: 'toast-top-center',
            closeButton: true
        });
    }

})();
