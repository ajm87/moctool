(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .service('AutomatonService', AutomatonService);

    AutomatonService.$inject = ['CytoscapeService'];

    function AutomatonService(CytoscapeService) {

        var cy = CytoscapeService.getCytoscapeInstanceForService();

        this.isNfa = function(automaton) {
            return hasEpsilon(automaton);
        }

        this.isDfa = function(automaton) {
            return !hasEpsilon(automaton);
        }

        function hasEpsilon(automaton) {
            var epsilonEdges = cy.edges('[label = "\u03b5"]');
            return (epsilonEdges.size() > 0);
        }
    }
})();