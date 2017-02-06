(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .service('AchievementService', AchievementService);

    AchievementService.$inject = ['toastr', 'Achievements', '$q'];

    function AchievementService(toastr, Achievements, $q) {

        var achievements = {
            "firstSimulate": {
                "name": "Amateur Simulator",
                "desc": "Simulate once"
            },
            "tenthSimulate": {
                "name": "Experienced Simulator",
                "desc": "Simulate ten times"
            },
            "twentiethSimulate": { 
                "name": "Master Simulator",
                "desc": "Simulate twenty times"
            },
            "firstConversion": { 
                "name": "Amateur Converter",
                "desc": "Convert once"
            },
            "tenthConversion": {
                "name": "Experienced Converter",
                "desc": "Convert ten times"
            },
            "twentiethConversion": {
                "name": "Master Converter",
                "desc": "Convert twenty times"
            },
            "firstLoad": {
                "name": "Locked and Loaded",
                "desc": "Load a previously saved automaton"
            },
            "firstSave": {
                "name": "Safe and Sound",
                "desc": "Save a created automaton"
            },
            "over30States": {
                "name": "Big Boy",
                "desc": "Create an automaton with over 30 states"
            },
            "over100States": {
                "name": "500 Server Error",
                "desc": "Secret"
            },
            "deadbeef": {
                "name": "0xDEADBEEF",
                "desc": "Secret"
            }
        };
        
        this.achievements = achievements;
        this.unlockAchievement = unlockAchievement;

        function unlockAchievement(achievementKey) {
            var hasUnlocked = false;
            Achievements.getStatus({achievementKey: achievementKey}, function(data) {
                hasUnlocked = data.hasUnlocked;
            }).$promise.then(function() {
                if(!hasUnlocked) {
                    Achievements.unlockAchievement({achievementKey: achievementKey}, function(ok) {
                        toastr.info('You earned the <i>' + achievements[achievementKey].name + '</i> achievement!', 'Achievement Unlocked!');
                    });
                }
            });
        }

        this.updateAchievementProgress = function(achievementKey, newProgress) {
            Achievements.updateProgress({achievementKey: achievementKey, progressToAdd: newProgress}).$promise.then(function() {Achievements.getProgress({achievementKey: achievementKey}, function(data){ 
                if(data.currentProgress >= data.unlockProgress) {
                    unlockAchievement(achievementKey);
                }
            })});
        }
    }
})();