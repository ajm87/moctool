package com.morgan.andy.domain;

import java.util.ArrayList;
import java.util.stream.Collectors;

//@Entity
//@Table(name = "automata")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FiniteAutomaton {

    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //@Column(name = "states")
    private ArrayList<State> states;

    //@Column(name = "alphabet")
    private String[] alphabet;

    //@Column(name = "startState)
    private State startState;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
