package com.morgan.andy.moc.simulation;

import com.morgan.andy.domain.State;

public class Step {

    private int stepId;
    private String transitionSymbol;
    private boolean finalStep = false;
    private Simulation.SimulationState currentState = Simulation.SimulationState.REJECT;

    public Step() {

    }

    public Step(int stepId, String transitionSymbol) {
        this.stepId = stepId;
        this.transitionSymbol = transitionSymbol;
    }

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public String getTransitionSymbol() {
        return transitionSymbol;
    }

    public void setTransitionSymbol(String transitionSymbol) {
        this.transitionSymbol = transitionSymbol;
    }

    public boolean isFinalStep() {
        return finalStep;
    }

    public void setFinalStep(boolean finalStep) {
        this.finalStep = finalStep;
    }

    public Simulation.SimulationState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(Simulation.SimulationState currentState) {
        this.currentState = currentState;
    }
}
