package com.morgan.andy.domain;

import java.util.ArrayList;
import java.util.HashMap;

public class FiniteAutomaton {

    private State startState;
    private HashMap<String, State> states;
    private String[] alphabet;

    public State getStartState() {
        return startState;
    }

    public void setStartState(State startState) {
        this.startState = startState;
    }

    public HashMap<String, State> getStates() {
        return states;
    }

    public void setStates(HashMap<String, State> states) {
        this.states = states;
    }

    public void addState(String stateName, State state) {
        if(states == null) {
            states = new HashMap<>();
        }
        states.put(stateName, state);
    }

    public String[] getAlphabet() { return alphabet; }

    public void setAlphabet(String[] alphabet) { this.alphabet = alphabet; }
}
