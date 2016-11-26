(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .controller('EditorController', EditorController);

    EditorController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Simulate', 'Load', 'NfaToDfa', '$uibModal', '$compile'];

    function EditorController ($scope, Principal, LoginService, $state, Simulate, Load, NfaToDfa, $uibModal, $compile) {
        var vm = this;
        vm.stateCount = 0;
        vm.zoom = zoom;
        vm.jsonifyAutomaton = jsonifyAutomaton;
        vm.loadAutomaton = loadAutomaton;
        vm.loadAutomatonFromServer = loadAutomatonFromServer;
        vm.convertNfaToDfa = convertNfaToDfa;
        vm.createStateTest = createStateTest;
        vm.setSymbol = setSymbol;
        vm.simulate = simulate;
        vm.cancelConnection = cancelConnection;
        vm.zoomLevel = 1;
        vm.isOpen = true;
        vm.getSimulation = getSimulation;
        vm.getSimulationStepId = getSimulationStepId;
        vm.popoverTemplate = '/app/editor/popover.html';
        vm.menuItems = [
            {
                name: 'Item 1'                
            },
            {
                name: 'Item 2'
            },
            {
                name: 'Item 3'
            }
        ];
        vm.config = {
        };
        vm.model = {};

        vm.tools = [{
            text: "Circle"
        }
        ];

        vm.menuOptions = [
            ['Select', function ($itemScope, $event, modelValue, text, $li) {
                $scope.selected = $itemScope.item.name;
            }],
            null, // Dividier
            ['Remove', function ($itemScope, $event, modelValue, text, $li) {
                $scope.items.splice($itemScope.$index, 1);
            }]
        ];

        jsPlumb.bind('connection', function(obj, oe) {
            // oe will be undefined if the connection was created programmatically, i.e.
            // from us loading an automaton from server
            if(typeof oe === "undefined") {
                return;
            }
            var i = obj.connection;
            i.isOpen = true;
            var $button = $('<div id="popover" uib-popover-template="vm.popoverTemplate" popover-is-open="connector.isOpen" popover-trigger="none" popover-title="Transition symbol"></div>');
            var buttonScope = $scope.$new();
            buttonScope.connector = i;
            buttonScope.symbol = i.getLabel();
            $compile($button)(buttonScope);
            $button.appendTo($(i.canvas.nextSibling));
            i.setLabel('&epsilon;');
            console.log(i);
            $(i.canvas.nextSibling.nextSibling).click(function() {
                $scope.$apply(function() {
                    if(i.getLabel() !== "&epsilon;") {
                        buttonScope.symbol = i.getLabel();
                    }
                    i.isOpen = !i.isOpen;
                });
            });
        });

        function setSymbol(connector, symbol) {
            if(!symbol || symbol.length === 0) {
                connector.setLabel('&epsilon;')
            } else {
                connector.setLabel(symbol);
            }
            connector.hasBeenSet = true;
            connector.isOpen = false;
        }

        function cancelConnection(connector) {
            if(!connector.hasBeenSet) {
                jsPlumb.detach(connector);
            }
            connector.isOpen = false;
        }

        function convertNfaToDfa() {
            var automatonObj = jsonifyAutomaton(false);
            NfaToDfa.save(automatonObj, function(data) {
                console.log('Got converted automaton: ', data);
                clearCanvas();
                loadNewAutomaton(data);
            });
        }

        function clearCanvas() {
            jsPlumb.deleteEveryEndpoint();
            $('.state').remove();
        }

        function createStateTest() {
            var circle = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
            circle.setAttributeNS(null,"id","circle");
            circle.setAttributeNS(null,"cx",50);
            circle.setAttributeNS(null,"cy",50);
            circle.setAttributeNS(null,"r",20);
            circle.setAttributeNS(null,"fill","none");
            circle.setAttributeNS(null,"stroke","black");
            circle.setAttributeNS(null,"stroke-width","1");
            circle.setAttributeNS(null, "class","draggable");
            document.getElementById("svg").appendChild(circle);
            $(circle).draggable({
                helper: "clone",
                revert: "invalid"
            });
        }

        function loadAutomatonFromServer() {
                Load.get(function(data){
                    loadAutomaton(data);
                });
        }

        function loadNewAutomaton(automatonToLoad) {
            jsPlumb.importDefaults({
                    ConnectorOverlays: [ [ "PlainArrow", { location:0.98, paintStyle: {fill: '#000000'}, width: 10, length: 10 } ],
                                         [ "Label", {location: 0.5, id:"label", cssClass: 'connector-label'}] ],
                    Connector: ['StateMachine', {curviness: -1, loopbackRadius: 20}],
                    EndpointStyle: {fill: '#000000'},
                    Endpoint: ['Dot', {radius: 5}],
            });     
            var exampleGreyEndpointOptions = {
                    isSource:true,
                    isTarget:true,
                    maxConnections: -1,
                    allowLoopback: true
                  };

            var states = automatonToLoad.stateVMs;
            var transitions = automatonToLoad.transitionVMs;
            var midpoint = 200;
            var startLeft = 100;
            var initialLeft = 100;
            var sideOffset = 150;
            var offset = 50;
            var offsetNoCentre = 25;
            var position = {};
            $.each(states, function(i, e) {
                var placed = 0;
                var source = e.id;
                var tCount = 0;
                $.each(transitions, function(a, b) {
                    if(b.sourceId === source) {
                        tCount++;
                        console.log('tCount is now: ', tCount);
                    }
                });
                if(e.id === "0") {
                    position[e.id] = 0;
                    var newState = $('<img>').attr('id', e.id)
                                .css('position', 'absolute')
                                .attr('src', 'content/images/FA-State-' + e.id + '.png')
                                .addClass('draggable')
                                .attr('draggable', 'vm.StateCount')
                                .css('left', startLeft + 'px')
                                .css('top', midpoint + 'px');
                    $('#zoomcontainer').append(newState);
                    jsPlumb.draggable(newState);
                    jsPlumb.addEndpoint(newState, exampleGreyEndpointOptions);
                }
                startLeft = initialLeft + (sideOffset * position[e.id]);
                midpoint = parseInt($('#' + source).css('top'));
                $.each(transitions, function(a, b) {
                    if(b.sourceId !== source) {
                        return true;
                    }
                    position[b.targetId] = position[e.id] + 1;
                    var newState = $('<img>').attr('id', b.targetId)
                                                .css('position', 'absolute')
                                                .attr('src', 'content/images/FA-State-' + b.targetId + '.png')
                                                .addClass('draggable')
                                                .attr('draggable', 'vm.StateCount');
                        if(tCount % 2 === 0) {
                            //even
                            if((placed + 1) % 2 === 0) {
                                var newSidePos = startLeft + sideOffset;
                                newState.css('left', newSidePos + 'px');
                                var newTopPos = midpoint + ((placed) * offsetNoCentre); 
                                newState.css('top', newTopPos + 'px');
                                placed++;
                            } else {
                                var newSidePos = startLeft + sideOffset;
                                newState.css('left', newSidePos + 'px');
                                var newTopPos = midpoint - ((placed + 1) * offsetNoCentre); 
                                newState.css('top', newTopPos + 'px');
                                placed++;
                            }
                        } else {
                            //odd
                            if(placed === 0) {
                                var newSidePos = startLeft + sideOffset;
                                newState.css('left', newSidePos + 'px');
                                newState.css('top', midpoint + 'px');
                                placed = -1;
                            } else {
                                if(placed === -1) {
                                    placed = 0;
                                }
                                if((placed + 1) % 2 === 0) {
                                    var newSidePos = startLeft + sideOffset;
                                    newState.css('left', newSidePos + 'px');
                                    var newTopPos = midpoint + ((placed) * offset); 
                                    newState.css('top', newTopPos + 'px');
                                    placed++;
                                } else {
                                    var newSidePos = startLeft + sideOffset;
                                    newState.css('left', newSidePos + 'px');
                                    var newTopPos = midpoint - ((placed + 1) * offset); 
                                    newState.css('top', newTopPos + 'px');
                                    placed++;
                                }
                            }
                        }
                        console.log(position);
                    $('#zoomcontainer').append(newState);
                    jsPlumb.draggable(newState);
                    jsPlumb.addEndpoint(newState, exampleGreyEndpointOptions);
                });
            });
            $.each(transitions, function(i, e) {
                console.log(e);
                var connection = jsPlumb.connect({
                    source: e.sourceId,
                    target: e.targetId,
                    overlays: [ [ "PlainArrow", { location:0.98, paintStyle: {fill: '#000000'}, width: 10, length: 10 } ],
                                        [ "Label", {location: 0.5, id:"label", cssClass: 'connector-label'}] ],
                });
                connection.setLabel(e.label);
            });
        }

        function loadAutomaton(automatonToLoad) {
            jsPlumb.importDefaults({
                    ConnectorOverlays: [ [ "PlainArrow", { location:0.98, paintStyle: {fill: '#000000'}, width: 10, length: 10 } ],
                                         [ "Label", {location: 0.5, id:"label", cssClass: 'connector-label'}] ],
                    Connector: ['StateMachine', {curviness: -1, loopbackRadius: 20}],
                    EndpointStyle: {fill: '#000000'},
                    Endpoint: ['Dot', {radius: 5}],
            });     
            var exampleGreyEndpointOptions = {
                    isSource:true,
                    isTarget:true,
                    maxConnections: -1,
                    allowLoopback: true
                  };

            var states = automatonToLoad.stateVMs;
            var transitions = automatonToLoad.transitionVMs;
            $.each(states, function(i, e) {
                var newState = $('<img>').attr('id', e.id)
                            .css('position', 'absolute')
                            .css('top', e.top)
                            .css('left', e.left)
                            .attr('src', e.stateName)
                            .addClass('draggable')
                            .attr('draggable', 'vm.StateCount');
                $('#zoomcontainer').append(newState);
                jsPlumb.draggable(newState);
                jsPlumb.addEndpoint(newState, exampleGreyEndpointOptions);
            });
            $.each(transitions, function(i, e) {
                console.log(e);
                var connection = jsPlumb.connect({
                    source: e.sourceId,
                    target: e.targetId,
                    overlays: [ [ "PlainArrow", { location:0.98, paintStyle: {fill: '#000000'}, width: 10, length: 10 } ],
                                        [ "Label", {location: 0.5, id:"label", cssClass: 'connector-label'}] ],
                });
                connection.setLabel(e.label);
            });
        }

        function simulate() {
            var automaton = jsonifyAutomaton();
            var toSend = {
                input: ['a', 'b', 'c'],
                finiteAutomaton: {
                    states: automaton.states,
                    alphabet: automaton.alphabet
                }
            };
            Simulate.save(toSend, function(data) {
                console.log('Got simulation ID: ', data);
            });
        }

        function getSimulation(id) {
            Simulate.getAllSteps({simulationId: id}, function(data) {
                console.log('Got simulation: ', data);
            });
        }

        function getSimulationStepId(simulationId, stepId) {
            Simulate.getStep({simulationId: simulationId, stepId: stepId}, function(data) {
                console.log('Got simulation step: ', data);
            });
        }

        function jsonifyAutomaton() {
            var states = [];
            var alphabet = ['a','b','c'];
            var isStart = false;
            var isFinal = false;
            $('.state').each(function(i,e) {
                var transitions = [];
                if(i === 0) {
                    isStart = true;
                }
                jsPlumb.select().each(function(c) {
                    if(c.sourceId !== $(e).attr('id')) {
                        return true;
                    }
                    transitions.push({
                        id: c.id,
                        sourceState: c.sourceId,
                        targetState: c.targetId,
                        transitionSymbol: c.getLabel()
                    });
                });
                states.push({
                    id: $(e).attr('id'),
                    top: parseInt($(e).css('top')),
                    left: parseInt($(e).css('left')),
                    stateName: $(e).attr('id'),
                    transitions: transitions,
                    startState: isStart,
                    finalState: isFinal
                });
            });
            var obj = {
                states: states,
                alphabet: alphabet
            };
            return obj;
        }

        // function jsonifyAutomaton(shouldPost) {
        //     var states = [];
        //     var startState;
        //     var alphabet = ['a','b', 'c'];
        //     $('.state').each(function(i, e) {
        //         var isStart = false;
        //         var isFinal = false;
        //         console.log(i);
        //             if(i === 0) {
        //                 isStart = true;
        //             }
        //         states.push({
        //             id: $(e).attr('id'),
        //             top: $(e).css('top'),
        //             left: $(e).css('left'),
        //             stateName: $(e).attr('src'),
        //             start: isStart,
        //             final: isFinal
        //         });
        //     });
        //     var transitions = [];
        //     jsPlumb.select().each(function(c) {
        //         transitions.push({
        //             id: c.floatingId,
        //             sourceId: c.sourceId,
        //             targetId: c.targetId,
        //             label: c.getLabel()
        //         });
        //     });
        //     var obj = {
        //         stateVMs: states,
        //         transitionVMs: transitions,
        //         alphabet: alphabet
        //     };
        //     if(shouldPost) {
        //         Simulate.save(obj);
        //     } else {
        //         return obj;
        //     }
        // }

        function zoom(event, delta, deltaX, deltaY) {
            // console.log("Delta : " + delta + ", Delta X: " + deltaX + ", Delta Y: " + deltaY);

            // vm.zoomLevel = vm.zoomLevel + (0.05 * deltaY);

            // if(vm.zoomLevel < 0.25 || vm.zoomLevel > 4) {
            //     return;
            // }

            // $('#zoomcontainer').css({
            //     "-webkit-transform":"scale("+ vm.zoomLevel + ")",
            //     "-moz-transform":"scale(" + vm.zoomLevel + ")",
            //     "-ms-transform":"scale(" + vm.zoomLevel + ")",
            //     "-o-transform":"scale(" + vm.zoomLevel + ")",
            //     "transform":"scale(" + vm.zoomLevel + ")"
            // });
            // jsPlumb.setZoom(vm.zoomLevel);
        }
    }
    
})();
