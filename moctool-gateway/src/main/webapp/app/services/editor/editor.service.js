(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .factory('Editor', Editor);

    Editor.$inject = ['$resource'];

    function Editor ($resource) {
        return $resource('api/simulate', {}, {});
    }
})();
