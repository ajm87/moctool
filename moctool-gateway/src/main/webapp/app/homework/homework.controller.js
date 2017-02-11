(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .controller('HomeworkController', HomeworkController);

    HomeworkController.$inject = ['$scope', 'Principal', 'Homework', 'Persist'];

    function HomeworkController ($scope, Principal, Homework, Persist) {
        var vm = this;

        var qcount = 2;

        vm.instructingClasses;
        vm.possibleQuestions;
        vm.selectedClass;
        vm.questions = [1];
        vm.selectedQuestions = [];
        vm.addQuestion = addQuestion;
        vm.homework = {
            questions: []
        };
        vm.hasSaved = true;
        vm.savedAutomata;
        vm.setHomework = setHomework;
        vm.setDueDate = setDueDate;
        vm.currentHomeworks;
        vm.chosenClass;
        vm.chosenHomework;
        vm.changeClass = changeClass;
        vm.changeHomework = changeHomework;
        vm.statuses;
        vm.getTotalQuestions = getTotalQuestions;

        function changeHomework() {
            Homework.getStatusesForHomework({homeworkId: vm.chosenHomework}, function(data) {
                vm.statuses = data;
                console.log(data);
            });
        }

        function changeClass() {
            Homework.getHomeworksForClass({classId: vm.chosenClass}, function(data) {
                vm.currentHomeworks = data;
                console.log(data);
            });
        }

        function getTotalQuestions() {
            return vm.statuses[0].homework.homeworkQuestions.length;
        }

        function setDueDate(newdate) {
            vm.homework.dueDate = newdate;
        }

        function addQuestion() {
            vm.questions.push(qcount++);
            console.log(vm.homework);
        }

        function setHomework() {
            Homework.setHomework(vm.homework, function(data) {
                console.log('set');
            });
        }

        init();
        function init() {
            Homework.getCurrentInstructorClasses({}, function(data) {
                vm.instructingClasses = data;
            });
            Homework.getQuestionRefs({}, function(data) {
                vm.possibleQuestions = data;
            });

            Persist.loadAll(function(saved) {
                vm.savedAutomata = saved;
            }, function(err) {
                vm.hasSaved = false;
            });
        }

    }
})();
