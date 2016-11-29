package com.morgan.andy.moc.simulation;

import com.morgan.andy.domain.State;

public class Step {

    private int stepId;
    private State startState;
    private State finishState;
    private String transitionSymbol;
    private boolean finalStep = false;

    public Step(int stepId, State startState, State finishState, String transitionSymbol) {
        this.stepId = stepId;
        this.startState = startState;
        this.finishState = finishState;
        this.transitionSymbol = transitionSymbol;
    }

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public State getStartState() {
        return startState;
    }

    public void setStartState(State startState) {
        this.startState = startState;
    }

    public State getFinishState() {
        return finishState;
    }

    public void setFinishState(State finishState) {
        this.finishState = finishState;
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
}
