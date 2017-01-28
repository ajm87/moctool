(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .factory('Persist', Persist);

    Persist.$inject = ['$resource'];

    function Persist ($resource) {
        return $resource('api/persist', {}, {
            load: {method: 'GET', params: {simulationId: '@simulationId', stepId: '@stepId'}, url: 'api/persist/load'},
            loadAll: {method: 'GET', url: 'api/persist/load', isArray: true},
            save: {method: 'POST', url: 'api/persist/save'}});
    }
})();
