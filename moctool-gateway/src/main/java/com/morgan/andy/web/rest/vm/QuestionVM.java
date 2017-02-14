package com.morgan.andy.web.rest.vm;

/**
 * @author Andy Morgan (ajm87)
 */
public class QuestionVM {
    private Long selectedQuestion;
    private String context;

    public QuestionVM() {
    }

    public QuestionVM(Long selectedQuestion, String context) {
        this.selectedQuestion = selectedQuestion;
        this.context = context;
    }

    public Long getSelectedQuestion() {
        return selectedQuestion;
    }

    public void setSelectedQuestion(Long selectedQuestion) {
        this.selectedQuestion = selectedQuestion;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
