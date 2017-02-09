package com.morgan.andy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author Andy Morgan (ajm87)
 */
@Entity
@Table(name = "homework_questions")
public class HomeworkQuestions {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "homework_id")
    private Homework homework;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "questions_ref_id")
    private QuestionsRef questionsRef;

    @Column(name = "context")
    private String context;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Homework getHomework() {
        return homework;
    }

    public void setHomework(Homework homework) {
        this.homework = homework;
    }

    public QuestionsRef getQuestionsRef() {
        return questionsRef;
    }

    public void setQuestionsRef(QuestionsRef questionsRef) {
        this.questionsRef = questionsRef;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
