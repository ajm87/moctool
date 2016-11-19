package com.morgan.andy.domain;

import java.util.ArrayList;

public class State {
    private String stateName;
    private ArrayList<Transition> transitions = new ArrayList<>();
    private boolean startState = false;
    private boolean finalState = false;

    public State(String stateName) {
        this.stateName = stateName;
    }


    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(ArrayList<Transition> transitions) {
        this.transitions = transitions;
    }

    public void addTransition(Transition transition) {
        transitions.add(transition);
    }

    public boolean isStartState() {
        return startState;
    }

    public State(String stateName, ArrayList<Transition> transitions, boolean startState, boolean finalState) {
        this.stateName = stateName;
        this.transitions = transitions;
        this.startState = startState;
        this.finalState = finalState;
    }

    public void setStartState(boolean startState) {
        this.startState = startState;
    }

    public boolean isFinalState() {
        return finalState;
    }

    public void setFinalState(boolean finalState) {
        this.finalState = finalState;
    }
}
