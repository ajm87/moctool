(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .service('AutomatonService', AutomatonService);

    AutomatonService.$inject = ['CytoscapeService'];

    function AutomatonService(CytoscapeService) {

        var cy = CytoscapeService.getCytoscapeInstanceForService();
        var treatDfaAsNfa = false;
    

        this.setTreatDfaAsNfa = function(shouldTreat) {
            console.log("setting to ", shouldTreat);
            treatDfaAsNfa = shouldTreat;
        }

        this.isNfa = function() {
            return hasEpsilon() || treatDfaAsNfa;
        }

        this.isDfa = function() {
            return !hasEpsilon() && !treatDfaAsNfa;
        }

        this.isMissingTransitions = function(alphabet) {
            var isMissing = false;

            cy.elements('node').forEach(function(ele) {
                $.each(alphabet, function(i, s) {
                    if(ele.connectedEdges().filter('[label="' + s + '"]').empty()) {
                        // missing a transition with symbol s
                        isMissing = true;
                        return false;
                    }
                });
                return !isMissing;
            });

            return isMissing;
        }

        function hasEpsilon() {
            var epsilonEdges = cy.edges('[label = "\u03b5"]');
            return (epsilonEdges.size() > 0);
        }
    }
})();