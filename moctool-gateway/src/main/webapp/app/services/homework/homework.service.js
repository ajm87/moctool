(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .factory('Homework', Homework);

    Homework.$inject = ['$resource'];

    function Homework ($resource) {
        return $resource('api/classes', {}, {
            getClassesForUser: {method: 'GET', params: {userId: '@userId'}, url: 'api/user/:userId/classes', isArray: true},
            getCurrentInstructorClasses: {method: 'GET', url: 'api/user/instructing', isArray: true},
            getQuestionRefs: {method: 'GET', url: 'api/homework/questionrefs', isArray: true},
            setHomework: {method: 'POST', url: 'api/homework/set'},
            getHomeworkStatusForUser: {method: 'GET', url: 'api/homework/status', isArray: true}
        });
    }
})();
