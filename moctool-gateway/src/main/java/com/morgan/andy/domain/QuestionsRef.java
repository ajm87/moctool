package com.morgan.andy.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Andy Morgan (ajm87)
 */
@Entity
@Table(name = "hw_questions_ref")
public class QuestionsRef {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "question")
    @NotNull
    private String question;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
