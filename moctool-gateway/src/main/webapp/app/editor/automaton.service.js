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

        // validates the automaton before it is sent to the server
        this.validateBeforeConversion = function() {
            // get our cytoscape instance
            init();
            var ret = {
                isValid: true,
                noNodes: false,
                noInitial: false,
                noAccept: false,
                hasOrphan: false
            };

            // no nodes
            if(cy.nodes().empty()) {
                ret.isValid = false;
                ret.noNodes = true;
                return ret;
            }

            // no initial state
            if(cy.nodes('[initial = "true"]').empty()) {
                ret.isValid = false;
                ret.noInitial = true;
                return ret;
            }

            // no accept states
            if(cy.nodes('[accept = "true"]').empty()) {
                ret.isValid = false;
                ret.noAccept = true;
                return ret;
            }

            // find orphan states, i.e. states with no incoming and outgoing edges
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

        // checks if the drawn automaton is an NFA
        this.isNfa = function() {
            // get cytoscape instance
            init();
            // short circuit - if it has an epsilon it must be NFA so return instantly.
            // if the user asked us to treat it as an nfa, return.
            // finally we do the computationally intensive work.
            return hasEpsilon() || treatDfaAsNfa || hasMultipleTransitions();
        }

        // checks if the drawn automaton is a DFA
        this.isDfa = function() {
            // get cytoscape instance
            init();
            // only a DFA if there is no epsilon AND the user hasn't asked us to
            // treat it as an NFA AND it does not have multiple transitions.
            return !hasEpsilon() && !treatDfaAsNfa && !hasMultipleTransitions();
        }

        // checks if the automaton has multiple transitions of the same symbol
        // from the same state
        function hasMultipleTransitions() {
            // get cytoscape instance
            init();
            var hasMultipleTransitions = false;
            // iterate over nodes
            cy.nodes().forEach(function(e, i) {
                var symbols = [];
                // for all outgoers of this node, if symbol is already seen for this
                // node, it has multiple transitions. if not, store it in the array for
                // further checking.
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

        // checks if the automaton is missing a transition.
        // the warning that we will add a trap state for this
        // symbol is shown to the user.
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

        // checks if the automaton has an epsilon edge anywhere
        function hasEpsilon() {
            init();
            var epsilonEdges = cy.edges('[label = "\u03b5"]');
            return (epsilonEdges.size() > 0);
        }
    }
})();