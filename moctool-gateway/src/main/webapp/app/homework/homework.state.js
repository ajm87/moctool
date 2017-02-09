(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('homework', {
            parent: 'app',
            url: '/homework',
            data: {
                authorities: ['ROLE_INSTRUCTOR']
            },
            views: {
                 'outside-container@': {
                     templateUrl: 'app/homework/homework.html',
                     controller: 'HomeworkController',
                     controllerAs: 'vm'
                 }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('homework');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
