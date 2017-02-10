package com.morgan.andy.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

/**
 * @author Andy Morgan (ajm87)
 */
@Entity
@Table(name = "homework")
public class Homework {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long homeworkId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "class_id")
    private HWClass hwClass;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "homework", fetch = FetchType.EAGER)
    private Set<HomeworkQuestions> homeworkQuestions;

    @Column(name = "created")
    private LocalDate created;

    @Column(name = "due_date")
    private Date dueDate;

    @PrePersist
    protected void onCreate() {
        created = LocalDate.now();
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<HomeworkQuestions> getHomeworkQuestions() {
        return homeworkQuestions;
    }

    public void setHomeworkQuestions(Set<HomeworkQuestions> homeworkQuestions) {
        this.homeworkQuestions = homeworkQuestions;
    }

    public Long getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(Long homeworkId) {
        this.homeworkId = homeworkId;
    }

    public HWClass getHwClass() {
        return hwClass;
    }

    public void setHwClass(HWClass hwClass) {
        this.hwClass = hwClass;
    }
}
