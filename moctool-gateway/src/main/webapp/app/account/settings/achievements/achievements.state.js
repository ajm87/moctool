(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('achievements', {
            parent: 'account',
            url: '/settings/achievements',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'global.menu.account.settings'
            },
            views: {
                'outside-container@': {
                    templateUrl: 'app/account/settings/achievements/achievements.html',
                    controller: 'AchievementsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('settings');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
