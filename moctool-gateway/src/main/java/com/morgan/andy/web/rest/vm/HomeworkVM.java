package com.morgan.andy.web.rest.vm;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.morgan.andy.config.ConverterConfiguration;

import java.util.ArrayList;
import java.util.Date;

public class HomeworkVM {

    private Long classId;

    private ArrayList<QuestionVM> questions;

    private String title;

    @JsonDeserialize(using = ConverterConfiguration.CustomJsonDateDeserializer.class)
    private Date dueDate;

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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
