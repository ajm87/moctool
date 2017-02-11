(function() {
    'use strict';

    angular
        .module('moctoolApp')
        .controller('HomeworkController', HomeworkController);

    HomeworkController.$inject = ['$scope', 'Principal', 'Homework', 'Persist', 'User', 'toastr'];

    function HomeworkController ($scope, Principal, Homework, Persist, User, toastr) {
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
        vm.students;
        vm.addClass = addClass;
        vm.addStudentToClass = addStudentToClass;

        function addStudentToClass(classId, studentId) {
            var toSend = {
                classId: classId,
                studentId: studentId
            };
            Homework.addStudentToClass(toSend, function(data) {
                toastr.success('Student added!', 'Student');
            });
        }

        function addClass(newClassName) {
            Homework.createClass(newClassName, function(data){ 
                toastr.success('Class ' + newClassName + ' created!', 'New Class');
            });
        }

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
            User.query({}, function(data) {
                vm.students = data;
            });

            Persist.loadAll(function(saved) {
                vm.savedAutomata = saved;
            }, function(err) {
                vm.hasSaved = false;
            });
        }

    }
})();
