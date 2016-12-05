(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .service('SimulatorService', SimulatorService);

    SimulatorService.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Simulate', 'Load', 'NfaToDfa', '$uibModal', '$compile'];

    function SimulatorService($scope, Principal, LoginService, $state, Simulate, Load, NfaToDfa, $uibModal, $compile) {

    }
});