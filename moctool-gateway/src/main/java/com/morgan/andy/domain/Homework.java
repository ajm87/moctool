package com.morgan.andy.domain;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "homework", fetch = FetchType.EAGER)
    private Set<HomeworkQuestions> homeworkQuestions;

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
