(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .controller('AchievementsController', AchievementsController);

    AchievementsController.$inject = ['Principal', 'Auth', 'JhiLanguageService', '$translate', 'Achievements', 'AchievementService'];

    function AchievementsController (Principal, Auth, JhiLanguageService, $translate, Achievements, AchievementService) {
        var vm = this;
        vm.error = null;
        vm.settingsAccount = null;
        vm.success = null;
        vm.profilePictureUrl = 'https://x1.xingassets.com/assets/frontend_minified/img/users/nobody_m.original.jpg';
        vm.achievements = {

        };
        vm.lookupKey = lookupKey;

        function lookupKey(key) {
            return AchievementService.achievements[key];
        }

        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */
        var copyAccount = function (account) {
            return {
                activated: account.activated,
                email: account.email,
                firstName: account.firstName,
                langKey: account.langKey,
                lastName: account.lastName,
                login: account.login
            };
        };

        Principal.identity().then(function(account) {
            vm.settingsAccount = copyAccount(account);
        });

        Achievements.getAllAchievementStatuses(function(data) {
            vm.achievements = data;
        });
    }
})();
