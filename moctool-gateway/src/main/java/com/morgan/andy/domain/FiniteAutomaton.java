package com.morgan.andy.domain;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class FiniteAutomaton {

    private ArrayList<State> states;
    private String[] alphabet;
    private State startState;

    public State getStartState() {
        return startState;
    }

    public void setStartState(State startState) {
        this.startState = startState;
    }


    public ArrayList<State> getStates() {
        return states;
    }

    public void setStates(ArrayList<State> states) {
        this.states = states;
        startState = states.stream().filter(State::isStartState).collect(Collectors.toList()).get(0);
    }

    public void addState(State state) {
        if(states == null) {
            states = new ArrayList<>();
        }
        states.add(state);
    }

    public String[] getAlphabet() { return alphabet; }

    public void setAlphabet(String[] alphabet) { this.alphabet = alphabet; }
}
