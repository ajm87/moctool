(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .controller('SimulatorController', SimulatorController);

    SimulatorController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Simulate', 'Load', 'NfaToDfa', '$uibModal', '$compile'];

    function SimulatorController($scope, Principal, LoginService, $state, Simulate, Load, NfaToDfa, $uibModal, $compile) {

    }
});