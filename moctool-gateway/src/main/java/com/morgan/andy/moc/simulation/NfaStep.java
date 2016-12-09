package com.morgan.andy.moc.simulation;

import com.morgan.andy.domain.State;

import java.util.ArrayList;

/**
 * @author Andy Morgan (ajm87)
 */
public class NfaStep extends Step {

    private ArrayList<State> startActiveStates;
    private ArrayList<State> finishActiveStates;

    public NfaStep(int stepId, String transitionSymbol, ArrayList<State> startActiveStates, ArrayList<State> finishActiveStates) {
        super(stepId, transitionSymbol);
        this.startActiveStates = startActiveStates;
        this.finishActiveStates = finishActiveStates;
    }

    public ArrayList<State> getStartActiveStates() {
        return startActiveStates;
    }

    public void setStartActiveStates(ArrayList<State> startActiveStates) {
        this.startActiveStates = startActiveStates;
    }

    public ArrayList<State> getFinishActiveStates() {
        return finishActiveStates;
    }

    public void setFinishActiveStates(ArrayList<State> finishActiveStates) {
        this.finishActiveStates = finishActiveStates;
    }
}
