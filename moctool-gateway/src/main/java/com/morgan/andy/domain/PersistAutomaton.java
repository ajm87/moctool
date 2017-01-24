package com.morgan.andy.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author Andy Morgan (ajm87)
 */
@Entity
@Table(name = "automata")
public class PersistAutomaton {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "userid")
    private Long userid;

    @Column(name = "name")
    @NotNull
    private String automatonName;

    @Column(name = "automaton")
    @NotNull
    private String json;

    @Column(name = "created")
    private LocalDate created;

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

    public String getAutomatonName() {
        return automatonName;
    }

    public void setAutomatonName(String automatonName) {
        this.automatonName = automatonName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
