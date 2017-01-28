package com.morgan.andy.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;

public class State {

    private String stateName;

    private ArrayList<Transition> transitions = new ArrayList<>();

    private ArrayList<Transition> incomingTransitions = new ArrayList<>();

    private boolean initialState = false;

    private boolean acceptState = false;

    private int yPos = -1;

    private int xPos = -1;

    private String id = "";

    public ArrayList<Transition> getIncomingTransitions() {
        return incomingTransitions;
    }

    public void setIncomingTransitions(ArrayList<Transition> incomingTransitions) {
        this.incomingTransitions = incomingTransitions;
    }

    public void addIncomingTransition(Transition incomingTransition) {
        incomingTransitions.add(incomingTransition);
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int top) {
        this.yPos = top;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
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
        transition.getTargetState().addIncomingTransition(transition);
    }

    public boolean isInitialState() {
        return initialState;
    }

    public State(String stateName, ArrayList<Transition> transitions, boolean initialState, boolean acceptState) {
        this.stateName = stateName;
        this.transitions = transitions;
        this.initialState = initialState;
        this.acceptState = acceptState;
    }

    public State(String stateName, boolean initialState, boolean acceptState) {
        this.stateName = stateName;
        this.initialState = initialState;
        this.acceptState = acceptState;
    }

    public State(String stateName, boolean initialState) {
        this.stateName = stateName;
        this.initialState = initialState;
    }

    public State() {

    }

    public void setInitialState(boolean initialState) {
        this.initialState = initialState;
    }

    public boolean isAcceptState() {
        return acceptState;
    }

    public void setAcceptState(boolean acceptState) {
        this.acceptState = acceptState;
    }

    /* Helper methods */

    /**
     * Return the state that is reached by travelling backwards along an incoming transition
     * with the provided transition symbol. Travels along the <b>first</b> matching transition
     * according to the ArrayList of incoming transitions. If no matching transitions are found,
     * returns null.
     */
    public State stepBackwards(String transitionSymbol) {

        for (Transition i : incomingTransitions) {
            if(i.getTransitionSymbol().equals(transitionSymbol)) {
                return i.getSourceState();
            }
        }

        return null;
    }

}
