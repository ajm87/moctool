package com.morgan.andy.moc.simulation;

import com.morgan.andy.domain.FiniteAutomaton;
import com.morgan.andy.domain.State;
import com.morgan.andy.domain.Transition;

public class DFASimulator extends Simulator {

    @Override
    public void simulateAutomaton(FiniteAutomaton automaton, String[] input, Integer simulationId) {
        State currentState = automaton.getStartState();
        int stepCount = 1;
        boolean finalStep = false;
        for (String s : input) {
            for (Transition transition : currentState.getTransitions()) {
                if(transition.getTransitionSymbol().equals(s)) {
                    Step step = new Step(stepCount, currentState, transition.getTargetState(), s);
                    simulatedAutomatonStore.addStepToSimulation(simulationId, step);
                    currentState = transition.getTargetState();
                    stepCount++;
                }
            }
        }
        simulatedAutomatonStore.getSimulation(simulationId).setFinalState(Simulation.SimulationState.ACCEPT);
    }
}
