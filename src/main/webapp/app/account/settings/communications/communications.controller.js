(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .controller('CommunicationsController', CommunicationsController);

    CommunicationsController.$inject = ['Principal', 'Auth', 'JhiLanguageService', '$translate'];

    function CommunicationsController (Principal, Auth, JhiLanguageService, $translate) {
        var vm = this;

    }
})();
