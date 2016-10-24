(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .controller('EditorController', EditorController);

    EditorController.$inject = ['$scope', 'Principal', 'LoginService', '$state'];

    function EditorController ($scope, Principal, LoginService, $state) {
        var vm = this;
        vm.stateCount = 0;
        
        vm.config = {
            panOnClickDrag: false
        };
        vm.model = {};

        vm.tools = [{
            text: "Circle"
        }
        ];
    }
    
})();
