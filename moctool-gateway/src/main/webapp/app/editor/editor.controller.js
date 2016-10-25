(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .controller('EditorController', EditorController);

    EditorController.$inject = ['$scope', 'Principal', 'LoginService', '$state'];

    function EditorController ($scope, Principal, LoginService, $state) {
        var vm = this;
        vm.stateCount = 0;
        vm.zoom = zoom;
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
