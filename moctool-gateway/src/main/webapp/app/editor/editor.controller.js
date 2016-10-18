(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .controller('EditorController', EditorController);

    EditorController.$inject = ['$scope', 'Principal', 'LoginService', '$state'];

    function EditorController ($scope, Principal, LoginService, $state) {
        var vm = this;
        
    }
    
})();
