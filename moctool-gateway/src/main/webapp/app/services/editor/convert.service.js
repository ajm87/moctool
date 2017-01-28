(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .factory('Convert', Convert);

    Convert.$inject = ['$resource'];

    function Convert ($resource) {
        return $resource('api/convert', {}, {
            convertNfaToDfa: {method: 'POST', url: 'api/convert/nfa/dfa'},
            convertReToNfa: {method: 'POST', url: 'api/convert/re/nfa'}
        });
    }
})();