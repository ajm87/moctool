(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .service('AchievementService', AchievementService);

    AchievementService.$inject = ['toastr'];

    function AchievementService(toastr) {

        this.achievements = {
            "firstSimulate": "Amateur Simulator",
            "tenthSimulate": "Experienced Simulator",
            "twentiethSimulate": "Master Simulator",
            "firstConversion": "Amateur Converter",
            "tenthConversion": "Experienced Converter",
            "twentiethConversion": "Master Converter",
            "firstLoad": "Locked and Loaded",
            "firstSave": "Safe and Secure",
            "over30States": "Big Boy",
            "over100States": "500 Server Error",
            "deadbeef": "0xDEADBEEF"
        };

        this.unlockAchievement = function(achievementKey) {
            toastr.info('You earned the <i>' + this.achievements[achievementKey] + '</i> achievement!', 'Achievement Unlocked!');
        }
    }
})();