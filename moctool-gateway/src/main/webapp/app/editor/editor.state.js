(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('editor', {
            parent: 'app',
            url: '/editor',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                 'outside-container@': {
                     templateUrl: 'app/editor/editor.html',
                     controller: 'EditorController',
                     controllerAs: 'vm'
                 }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('editor');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
