package com.morgan.andy.domain;

public class Transition {

    private State targetState;
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
}
