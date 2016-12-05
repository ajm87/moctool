(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .factory('NfaToDfa', NfaToDfa);

    NfaToDfa.$inject = ['$resource'];

    function NfaToDfa ($resource) {
        return $resource('api/convert/nfa/dfa', {}, {});
    }
})();
