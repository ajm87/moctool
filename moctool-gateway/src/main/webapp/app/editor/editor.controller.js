(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .controller('EditorController', EditorController)
        .controller('SimulateModalController', SimulateModalController)
        .config(ToastrConfigurer);

    EditorController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Simulate', 'Load', 'NfaToDfa', '$uibModal', '$compile', 'CytoscapeService', 'toastr', 'AutomatonService'];

    function EditorController ($scope, Principal, LoginService, $state, Simulate, Load, NfaToDfa, $uibModal, $compile, CytoscapeService, toastr, AutomatonService) {
        var cy = CytoscapeService.getCytoscapeInstance($scope);
        var stateCount = 0;

                var ctx = document.querySelector('#current-input');
                var instance = new Mark(ctx);
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
        vm.simulationStep = 0;
        vm.currentSimulation = 0;
        vm.previousConnection;
        vm.previousNfaTransitions;
        vm.setStateName = setStateName;
        vm.hideQtip = hideQtip;
        vm.currentSymbol;
        vm.cancelSimulation = cancelSimulation;
        vm.simulationInput;
        vm.isSimulating = false;
        vm.resetSimulation = resetSimulation;
        vm.stepBackward = stepBackward;
        vm.pause = pause;
        vm.stepForward = stepForward;
        vm.completeSimulation = completeSimulation;
        vm.startCollection;
        vm.finishCollection;
        vm.currentInput;
        vm.clearCanvas = clearCanvas;

        init();
        function init() {
            $('#simulation-panel').draggable();
            $('#simulation-panel').on('drag', function(e, ui) {
                $('#simulation-panel').css('right', '');
                $('#simulation-panel').off('drag');
            });
            $('#sim-fast-backward').qtip({
                content: {
                    text: 'Reset simulation to beginning'
                },
                style: {
                    classes: 'qtip-bootstrap'
                },
                position: {
                    my: 'top center',
                    at: 'bottom center'
                }
            });
            $('#sim-step-backward').qtip({
                content: {
                    text: 'Step back'
                },
                style: {
                    classes: 'qtip-bootstrap'
                },
                position: {
                    my: 'top center',
                    at: 'bottom center'
                }
            });
            $('#sim-pause').qtip({
                content: {
                    text: 'Pause simulation'
                },
                style: {
                    classes: 'qtip-bootstrap'
                },
                position: {
                    my: 'top center',
                    at: 'bottom center'
                }
            });
            $('#sim-step-forward').qtip({
                content: {
                    text: 'Step forward'
                },
                style: {
                    classes: 'qtip-bootstrap'
                },
                position: {
                    my: 'top center',
                    at: 'bottom center'
                }
            });
            $('#sim-fast-forward').qtip({
                content: {
                    text: 'Simulate all steps'
                },
                style: {
                    classes: 'qtip-bootstrap'
                },
                position: {
                    my: 'top center',
                    at: 'bottom center'
                }
            });
        }

        function clearSimulationHighlights() {
            toastr.clear();
            instance.unmark();
            if(!angular.isUndefined(vm.previousConnection)) {
                vm.previousConnection.style({
                    'line-color': 'black',
                    'target-arrow-color': 'black'
                });
            }
            if(!angular.isUndefined(vm.previousNfaTransitions)) {
                vm.previousNfaTransitions.style({
                    'line-color': 'black',
                    'target-arrow-color': 'black'
                });
            }
            if(!angular.isUndefined(vm.startCollection)) {
                vm.startCollection.style({
                    'border-color': 'black'
                });
            }
            if(!angular.isUndefined(vm.finishCollection)) {
                vm.finishCollection.style({
                    'border-color': 'black'
                });
            }
        }

        function cancelSimulation() {
            clearSimulationHighlights();
            vm.simulationStep = 0;
            vm.currentSimulation = 0;
            vm.isSimulating = false;
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

        function resetSimulation() {
            clearSimulationHighlights();
            vm.simulationStep = 0;
            vm.currentSymbol = "";
        }

        function stepBackward() {
            if(vm.simulationStep < 2) {
                return;
            }
            vm.simulationStep--;
            clearSimulationHighlights();
            getCurrentStep();
        }

        function pause() {
            console.log('pause');
        }

        function stepForward() {
            vm.simulationStep++;
            getCurrentStep();
        }

        function getCurrentStep() {
            vm.currentSymbol = vm.simulationInput[vm.simulationStep - 1];
            Simulate.getStep({simulationId: vm.currentSimulation, stepId: vm.simulationStep}, function(data) {
                console.log('Got simulation step: ', data);
                if(AutomatonService.isNfa()) {
                    parseNextStepNfa(data);
                } else if(AutomatonService.isDfa()) {
                    parseNextStep(data);
                }
            }, function(response) {
                // step not found
                if(response.status === 404) {
                    console.log('Step not found');
                }
            });
        }

        function completeSimulation() {
            getAllSteps(vm.simulationStep);
        }

        function loadAutomatonFromServer() {
                Load.get(function(data){
                    loadAutomaton(data);
                });
        }

        function simulate() {
            vm.simulationStep = 0;
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
                vm.currentInput = selected.value;
                vm.simulationInput = selected.value.split('');

                if(AutomatonService.isDfa(automaton)) {
                    Simulate.saveDfa(toSend, function(data) {
                        simulateCallback(data);
                    });
                } else if(AutomatonService.isNfa(automaton)) {
                    Simulate.saveNfa(toSend, function(data) {
                        simulateCallback(data);
                    });
                }

            });
        }

        function simulateCallback(data) {
            clearSimulationHighlights();
            vm.simulationStep = 0;
            console.log('Got simulation ID: ', data.id);
            vm.currentSimulation = data.id;
            vm.isSimulating = true;
            $('#simulation-panel').css('right', '100px');
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

        function getAllSteps(startStep) {
            if(angular.isUndefined(startStep)) {
                startStep = 1;
            }
            Simulate.getAllSteps({simulationId: vm.currentSimulation}, function(data) {
                angular.forEach(data, function(value, key) {
                    console.log(value);
                    setTimeout(function() {
                        if(AutomatonService.isNfa()) {
                            parseNextStepNfa(value);
                        } else if(AutomatonService.isDfa()) {
                            parseNextStep(value);
                        }
                    }, 1000*vm.simulationStep);
                        vm.simulationStep++;
                });
            });
        }

        function parseNextStepNfa(data) {
            vm.startCollection = cy.collection();
            vm.finishCollection = cy.collection();
            vm.currentSymbol = data.transitionSymbol;
            angular.forEach(data.startActiveStates, function(value, key) {
                vm.startCollection = vm.startCollection.add(cy.getElementById(value.id));
            });
            angular.forEach(data.finishActiveStates, function(value, key) {
                vm.finishCollection = vm.finishCollection.add(cy.getElementById(value.id));
            });
            vm.startCollection.style({
                'border-color': 'black'
            });
            vm.finishCollection.style({
                'border-color': 'orange'
            });
            var transitions = vm.startCollection.edgesTo(vm.finishCollection);
            var matchingTransitions = transitions.filter("[label = '" + data.transitionSymbol + "']");
            if(!angular.isUndefined(vm.previousNfaTransitions)) {
                vm.previousNfaTransitions.style({
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
                vm.finishCollection.style({
                    'border-color': 'green'
                });
            } else if(data.finalStep && data.currentState === 'REJECT') {
                toastr.error('Input string rejected!', 'Simulation');
                matchingTransitions.style({
                    'line-color': 'red',
                    'target-arrow-color': 'red'
                });
                vm.finishCollection.style({
                    'border-color': 'red'
                });
            }
            vm.previousNfaTransitions = matchingTransitions;
        }

        function parseNextStep(data) {
                var startNode = cy.getElementById(data.startState.id);
                var transitions = startNode.outgoers('edge');
                var matchingTransitions = transitions.filter("[label = '" + data.transitionSymbol + "']");
                vm.currentSymbol = data.transitionSymbol;
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
                instance.mark(vm.currentSymbol, {
                    filter: function(node, term, maxCount, count) {
                        if(count > 0) {
                            return false;
                        } else {
                            return true;
                        }
                    },
                    className: 'mark-accept'
                });
                } else if(data.finalStep && data.currentState === 'REJECT') {
                    toastr.error('Input string rejected!', 'Simulation');
                    matchingTransitions.style({
                        'line-color': 'red',
                        'target-arrow-color': 'red'
                    });
                                    instance.mark(vm.currentSymbol, {
                    filter: function(node, term, maxCount, count) {
                        if(count > 0) {
                            return false;
                        } else {
                            return true;
                        }
                    },
                    className: 'mark-reject'
                });
                } else {

                instance.mark(vm.currentSymbol, {
                    filter: function(node, term, maxCount, count) {
                        if(count > 0) {
                            return false;
                        } else {
                            return true;
                        }
                    }
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
