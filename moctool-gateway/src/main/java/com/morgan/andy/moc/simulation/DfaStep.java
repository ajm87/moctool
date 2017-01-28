package com.morgan.andy.moc.simulation;

import com.morgan.andy.domain.State;

/**
 * @author Andy Morgan (ajm87)
 */
public class DfaStep extends Step {

    private State startState;
    private State finishState;

    public DfaStep(int stepId, State startState, State finishState, String transitionSymbol) {
        super(stepId, transitionSymbol);
        this.startState = startState;
        this.finishState = finishState;
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
}
