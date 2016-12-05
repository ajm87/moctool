(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .controller('EditorController', EditorController)
        .controller('SimulateModalController', SimulateModalController)
        .config(ToastrConfigurer);

    EditorController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Simulate', 'Load', 'NfaToDfa', '$uibModal', '$compile', 'CytoscapeService', 'toastr'];

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
            addedNode.addClass('standard');
            addedNode.addClass('non-initial');
            stateCount++;
            addedNode.qtip({
                content: {
                    text: function(api) {
                        var tipScope = $scope.$new();
                        tipScope.state = addedNode;
                        var ele = angular.element('<div class="form-group"><label>Input a name for this state:</label><div><input type="text" ng-model="name"><div><button type="button" class="btn btn-primary" ng-click="vm.setStateName(state, name)">OK</button><button type="button" class="btn btn-danger" ng-click="vm.cancelSetStateName(state)">Cancel</button></div>');
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
        vm.setStateName = setStateName;
        vm.cancelSetStateName = cancelSetStateName;

        function cancelSetStateName(state) {
            state.qtip('api').hide();
        }

        function setStateName(state, name) {
            state.data('name', name);
            state.qtip('api').hide();
        }

        function setSymbol(connector, symbol) {
            if(!symbol || symbol.length === 0) {
                connector.data('label', '&epsilon;');
            } else {
                connector.data('label', symbol);
            }
            connector.qtip("api").hide();
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
            var modal = $uibModal.open({
                templateUrl: 'app/editor/simulate.modal.html',
                controller: 'SimulateModalController',
                controllerAs: 'vm'
            });

            modal.result.then(function (selected) {
                var automaton = jsonifyAutomaton();
                var toSend = {
                    input: selected.value.split(''),
                    finiteAutomaton: automaton
                };
                Simulate.save(toSend, function(data) {
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

        function getAllSteps() {
            Simulate.getAllSteps({simulationId: vm.currentSimulation}, function(data) {
                console.log('Got simulation: ', data);
            });
        }

        function getNextStep() {
            Simulate.getStep({simulationId: vm.currentSimulation, stepId: vm.simulationStep}, function(data) {
                console.log('Got simulation step: ', data);
                parseNextStep(data);
            }, function(response) {
                // step not found
                if(response.status === 404) {
                    console.log('Step not found');
                }
            });
            vm.simulationStep++;
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

    SimulateModalController.$inject = ['$uibModalInstance'];
    function SimulateModalController($uibModalInstance) {
        
                    var vm = this;
                    vm.input = "";
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
