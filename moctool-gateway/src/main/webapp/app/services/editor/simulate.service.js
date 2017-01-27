(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .factory('Simulate', Simulate);

    Simulate.$inject = ['$resource'];

    function Simulate ($resource) {
        return $resource('api/simulate/:simulationId/step/:stepId', {}, {
            getStep: {method: 'GET', params: {simulationId: '@simulationId', stepId: '@stepId'}},
            getAllSteps: {method: 'GET', params: {simulationId: '@simulationId'}, isArray: true},
            saveDfa: {method: 'POST', url: 'api/simulate/dfa'},
            saveNfa: {method: 'POST', url: 'api/simulate/nfa'},
            bulkTestNfa: {method: 'POST', url: 'api/test/nfa', isArray: true},
            bulkTestDfa: {method: 'POST', url: 'api/test/dfa', isArray: true},
            getStatus: {method: 'GET', params: {simulationId: '@simulationId', stepId: '@stepId'}, url: '/api/simulate/:simulationId/status'}
        });
    }
})();
