package com.morgan.andy.domain;

import java.util.ArrayList;

public class State {
    private String stateName;
    private ArrayList<Transition> transitions = new ArrayList<>();
    private boolean startState = false;
    private boolean finalState = false;
    private int top = -1;
    private int left = -1;
    private String id = "";

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
        if(transitions == null) {
            transitions = new ArrayList<>();
        }
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

    public State(String stateName, boolean startState, boolean finalState) {
        this.stateName = stateName;
        this.startState = startState;
        this.finalState = finalState;
    }

    public State(String stateName, boolean startState) {
        this.stateName = stateName;
        this.startState = startState;
    }

    public State() {

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
