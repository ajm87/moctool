(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .factory('Load', Load);

    Load.$inject = ['$resource'];

    function Load ($resource) {
        return $resource('api/load', {}, {});
    }
})();
