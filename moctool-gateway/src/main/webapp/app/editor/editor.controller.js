(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .controller('EditorController', EditorController)
        .controller('ModalController', ModalController);

    EditorController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Simulate', 'Load', 'NfaToDfa', '$uibModal'];
    ModalController.$inject = ['$uibModalInstance'];

    function ModalController($uibModalInstance) {
        var vm = this;
        vm.symbol = "";
        vm.ok = function() {
            $uibModalInstance.close(vm.symbol);
        }
        vm.cancel = function() {
            $uibModalInstance.dismiss('cancel');
        }
    }

    function EditorController ($scope, Principal, LoginService, $state, Simulate, Load, NfaToDfa, $uibModal) {
        var vm = this;
        vm.stateCount = 0;
        vm.zoom = zoom;
        vm.jsonifyAutomaton = jsonifyAutomaton;
        vm.loadAutomaton = loadAutomaton;
        vm.loadAutomatonFromServer = loadAutomatonFromServer;
        vm.convertNfaToDfa = convertNfaToDfa;
        vm.createStateTest = createStateTest;
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
            var modal = $uibModal.open({
                templateUrl: 'app/editor/modal.html',
                size: 'sm',
                controller: 'ModalController',
                controllerAs: 'vm'
            });
            modal.result.then(function (label) {
                i.setLabel(label);
            }, function() {
                jsPlumb.detach(i);
            });
        });

        function convertNfaToDfa() {
            var automatonObj = jsonifyAutomaton(false);
            NfaToDfa.save(automatonObj, function(data) {
                console.log('Got converted automaton: ', data);
                loadAutomaton(data);
            });
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

        function jsonifyAutomaton(shouldPost) {
            var states = [];
            var startState;
            var alphabet = ['a','b'];
            $('.state').each(function(i, e) {
                var isStart = false;
                var isFinal = false;
                    if(i === 0) {
                        isStart = true;
                    }
                states.push({
                    id: $(e).attr('id'),
                    top: $(e).css('top'),
                    left: $(e).css('left'),
                    stateName: $(e).attr('src'),
                    isStart: isStart,
                    isFinal: isFinal
                });
            });
            var transitions = [];
            jsPlumb.select().each(function(c) {
                transitions.push({
                    id: c.floatingId,
                    sourceId: c.sourceId,
                    targetId: c.targetId,
                    label: c.getLabel()
                });
            });
            var obj = {
                stateVMs: states,
                transitionVMs: transitions,
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
