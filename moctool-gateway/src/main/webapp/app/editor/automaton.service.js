(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .service('AutomatonService', AutomatonService);

    AutomatonService.$inject = ['CytoscapeService'];

    function AutomatonService(CytoscapeService) {

        var cy;
        var treatDfaAsNfa = false;
    
        function init() {
            cy = CytoscapeService.getCytoscapeInstanceForService();
        }

        this.validateBeforeConversion = function() {
            init();
            var ret = {
                isValid: true,
                noNodes: false,
                noInitial: false,
                noAccept: false,
                hasOrphan: false
            };

            if(cy.nodes().empty()) {
                ret.isValid = false;
                ret.noNodes = true;
                return ret;
            }

            if(cy.nodes('[initial = "true"]').empty()) {
                ret.isValid = false;
                ret.noInitial = true;
                return ret;
            }

            if(cy.nodes('[accept = "true"]').empty()) {
                ret.isValid = false;
                ret.noAccept = true;
                return ret;
            }

            cy.nodes().forEach(function(ele, i) {
                if(ele.incomers().empty() && ele.outgoers().empty()) {
                    ret.isValid = false;
                    ret.hasOrphan = true;
                    return false;
                }
            });

            return ret;
        }

        this.setTreatDfaAsNfa = function(shouldTreat) {
            console.log("setting to ", shouldTreat);
            treatDfaAsNfa = shouldTreat;
        }

        this.isNfa = function() {
            init();
            return hasEpsilon() || treatDfaAsNfa || hasMultipleTransitions();
        }

        this.isDfa = function() {
            init();
            return !hasEpsilon() && !treatDfaAsNfa && !hasMultipleTransitions();
        }

        function hasMultipleTransitions() {
            init();
            var hasMultipleTransitions = false;
            cy.nodes().forEach(function(e, i) {
                var symbols = [];
                e.outgoers().filter('edge').forEach(function(edge, index) {
                    if(symbols.indexOf(edge.data('label')) === -1) {
                        symbols.push(edge.data('label'));
                    } else {
                        hasMultipleTransitions = true;
                        return false;
                    }
                });
            });
            return hasMultipleTransitions;
        }

        this.isMissingTransitions = function(alphabet) {
            init();
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
            init();
            var epsilonEdges = cy.edges('[label = "\u03b5"]');
            return (epsilonEdges.size() > 0);
        }
    }
})();