package com.morgan.andy.web.rest.vm;

import java.util.ArrayList;

public class HomeworkVM {

    private Long classId;

    private ArrayList<QuestionVM> questions;


    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public ArrayList<QuestionVM> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<QuestionVM> questions) {
        this.questions = questions;
    }

    public HomeworkVM() {
    }

    public HomeworkVM(Long classId, ArrayList<QuestionVM> questions) {
        this.classId = classId;
        this.questions = questions;
    }

}
