(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .controller('EditorController', EditorController);

    EditorController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Simulate', 'Load', 'NfaToDfa'];

    function EditorController ($scope, Principal, LoginService, $state, Simulate, Load, NfaToDfa) {
        var vm = this;
        vm.stateCount = 0;
        vm.zoom = zoom;
        vm.jsonifyAutomaton = jsonifyAutomaton;
        vm.loadAutomaton = loadAutomaton;
        vm.convertNfaToDfa = convertNfaToDfa;
        vm.zoomLevel = 1;
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
            panOnClickDrag: false
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

        function convertNfaToDfa() {
            var automatonObj = jsonifyAutomaton(false);
            console.log(automatonObj);
            NfaToDfa.save(automatonObj, function(data) {
                console.log('Got converted automaton: ', data);
                loadAutomaton(data);
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

            var states = automatonToLoad.states;
            var transitions = automatonToLoad.transitions;
            $.each(states, function(i, e) {
                console.log(e);
                var newState = $('<img>').attr('id', e.id)
                            .css('position', 'absolute')
                            .css('top', e.top)
                            .css('left', e.left)
                            .attr('src', e.src)
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

        function jsonifyAutomaton(shouldPost) {
            var states = [];
            var startState;
            var alphabet = ['a','b'];
            $('.state').each(function(i, e) {
                states.push({
                    id: $(e).attr('id'),
                    top: $(e).css('top'),
                    left: $(e).css('left'),
                    src: $(e).attr('src')
                });
            });
            startState = states[0];
            var transitions = [];
            jsPlumb.select().each(function(c) {
                transitions.push({
                    id: c.floatingId,
                    sourceId: c.sourceId,
                    targetId: c.targetId,
                    label: c.getLabel()
                });
            });
            console.log("POSTing...");
            var obj = {
                states: states,
                transitions: transitions,
                startState: startState,
                alphabet: alphabet
            };
            if(shouldPost) {
                Simulate.save(obj);
            } else {
                return obj;
            }
        }

        function zoom(event, delta, deltaX, deltaY) {
            console.log("Delta : " + delta + ", Delta X: " + deltaX + ", Delta Y: " + deltaY);

            vm.zoomLevel = vm.zoomLevel + (0.05 * deltaY);

            if(vm.zoomLevel < 0.25 || vm.zoomLevel > 4) {
                return;
            }

            $('#zoomcontainer').css({
                "-webkit-transform":"scale("+ vm.zoomLevel + ")",
                "-moz-transform":"scale(" + vm.zoomLevel + ")",
                "-ms-transform":"scale(" + vm.zoomLevel + ")",
                "-o-transform":"scale(" + vm.zoomLevel + ")",
                "transform":"scale(" + vm.zoomLevel + ")"
            });
            jsPlumb.setZoom(vm.zoomLevel);
        }
    }
    
})();
