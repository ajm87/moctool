package com.morgan.andy.web.rest.vm;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.morgan.andy.domain.FiniteAutomaton;

import java.util.Arrays;

public class SimulateVM {

    private FiniteAutomaton finiteAutomaton;
    private String[] input;

    public FiniteAutomaton getFiniteAutomaton() {
        return finiteAutomaton;
    }

    public void setFiniteAutomaton(FiniteAutomaton finiteAutomaton) {
        this.finiteAutomaton = finiteAutomaton;
    }

    public String[] getInput() {
        return input;
    }

    public void setInput(String[] input) {
        this.input = input;
    }

    public SimulateVM(FiniteAutomaton finiteAutomaton, String[] input) {
        this.finiteAutomaton = finiteAutomaton;
        this.input = input;
    }

    public SimulateVM() {

    }
}
