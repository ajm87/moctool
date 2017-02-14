package com.morgan.andy.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Andy Morgan (ajm87)
 */
@Entity
@Table(name = "homework_status")
public class HomeworkStatus {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "homework_id")
    private Homework homework;

    @Column(name = "status")
    private Long status;

    public HomeworkStatus() {
    }

    public HomeworkStatus(User user, Homework homework, Long status) {
        this.user = user;
        this.homework = homework;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Homework getHomework() {
        return homework;
    }

    public void setHomework(Homework homework) {
        this.homework = homework;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
}
