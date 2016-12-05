package com.morgan.andy.moc.simulation;

import com.morgan.andy.domain.FiniteAutomaton;
import com.morgan.andy.domain.State;
import com.morgan.andy.domain.Transition;

import java.util.ArrayList;

public class DFASimulator extends Simulator {

    @Override
    public void simulateAutomaton(FiniteAutomaton automaton, String[] input, Integer simulationId) {
        State currentState = automaton.getStartState();
        int stepCount = 1;
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
        ArrayList<Step> finalStateSteps = simulatedAutomatonStore.getSimulation(simulationId).getSteps();
        finalStateSteps.get(finalStateSteps.size() - 1).setFinalStep(true);
        if(finalStateSteps.get(finalStateSteps.size() - 1).getFinishState().isAcceptState()) {
            simulatedAutomatonStore.getSimulation(simulationId).setFinalState(Simulation.SimulationState.ACCEPT);
            finalStateSteps.get(finalStateSteps.size() - 1).setCurrentState(Simulation.SimulationState.ACCEPT);
        }
    }
}
