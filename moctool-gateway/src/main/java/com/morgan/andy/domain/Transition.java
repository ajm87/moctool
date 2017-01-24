package com.morgan.andy.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

public class Transition {

    private State sourceState;

    private State targetState;

    private String transitionSymbol;

    public State getSourceState() {
        return sourceState;
    }

    public void setSourceState(State sourceState) {
        this.sourceState = sourceState;
    }

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

    public Transition(State sourceState, State targetState, String transitionSymbol) {
        this.targetState = targetState;
        this.transitionSymbol = transitionSymbol;
        this.sourceState = sourceState;
    }

    public Transition() {

    }
}
