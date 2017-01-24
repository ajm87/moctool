package com.morgan.andy.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

//@Entity
//@Table(name = "transitions")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Transition {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @Column(name = "targetState")
    private State targetState;

//    @Column(name = "transitionSymbol")
    private String transitionSymbol;

    public State getTargetState() {
        return targetState;
    }

    public void setTargetState(State targetState) {
        this.targetState = targetState;
    }

    public String getTransitionSymbol() {
        return transitionSymbol;
    }

    public void setTransitionSymbol(String transitionSymbol) {
        this.transitionSymbol = transitionSymbol;
    }

    public Transition(State targetState, String transitionSymbol) {
        this.targetState = targetState;
        this.transitionSymbol = transitionSymbol;
    }

    public Transition() {

    }
}
