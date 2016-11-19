(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .factory('Simulate', Simulate);

    Simulate.$inject = ['$resource'];

    function Simulate ($resource) {
        return $resource('api/simulate', {}, {});
    }
})();
