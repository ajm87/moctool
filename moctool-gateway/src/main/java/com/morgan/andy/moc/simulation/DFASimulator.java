package com.morgan.andy.moc.simulation;

import com.morgan.andy.domain.FiniteAutomaton;
import com.morgan.andy.domain.State;
import com.morgan.andy.domain.Transition;

import java.util.ArrayList;
import java.util.Arrays;

public class DFASimulator extends Simulator {

    @Override
    public void simulateAutomaton(FiniteAutomaton automaton, String[] input, Integer simulationId) {
        ArrayList<Step> simulationSteps = new ArrayList<>();
        State currentState = automaton.getStartState();
        int stepCount = 1;
        for (String s : input) {
            for (Transition transition : currentState.getTransitions()) {
                if(transition.getTransitionSymbol().equals(s)) {
                    Step step = new Step(stepCount, currentState, transition.getTargetState(), s);
                    simulationSteps.add(step);
                    currentState = transition.getTargetState();
                    stepCount++;
                }
            }
        }
        Simulation simulation = simulatedAutomatonStore.getSimulation(simulationId);
        simulation.setSteps(simulationSteps);
        simulatedAutomatonStore.addSimulation(simulation.getId(), simulation);
    }
}
