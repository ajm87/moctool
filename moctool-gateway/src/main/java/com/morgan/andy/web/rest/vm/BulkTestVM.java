package com.morgan.andy.web.rest.vm;

import java.util.ArrayList;

public class BulkTestVM {

    private AutomatonVM finiteAutomaton;
    private ArrayList<String> inputs;

    public AutomatonVM getFiniteAutomaton() {
        return finiteAutomaton;
    }

    public void setFiniteAutomaton(AutomatonVM finiteAutomaton) {
        this.finiteAutomaton = finiteAutomaton;
    }

    public ArrayList<String> getInputs() {
        return inputs;
    }

    public void setInput(ArrayList<String> inputs) {
        this.inputs = inputs;
    }

    public BulkTestVM(AutomatonVM finiteAutomaton, ArrayList<String> inputs) {
        this.finiteAutomaton = finiteAutomaton;
        this.inputs = inputs;
    }

    public BulkTestVM() {

    }
}
