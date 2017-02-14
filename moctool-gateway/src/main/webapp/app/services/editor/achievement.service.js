(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .factory('Achievements', Achievements);

    Achievements.$inject = ['$resource'];

    function Achievements ($resource) {
        return $resource('api/achievements', {}, {
            getStatus: {method: 'GET', params: {achievementKey: '@achievementKey'}, url: 'api/achievements/:achievementKey/status'},
            getProgress: {method: 'GET', params: {achievementKey: '@achievementKey'}, url: 'api/achievements/:achievementKey/progress'},
            updateProgress: {method: 'POST', params: {achievementKey: '@achievementKey', progressToAdd: '@progressToAdd'}, url: 'api/achievements/:achievementKey/progress/:progressToAdd'},
            unlockAchievement: {method: 'POST', params: {achievementKey: '@achievementKey'}, url: 'api/achievements/:achievementKey/unlock'},
            getAllAchievementStatuses: {method: 'GET'}
        });
    }
})();
