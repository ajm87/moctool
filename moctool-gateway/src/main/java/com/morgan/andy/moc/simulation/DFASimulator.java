package com.morgan.andy.moc.simulation;

import com.morgan.andy.domain.FiniteAutomaton;
import com.morgan.andy.domain.State;
import com.morgan.andy.domain.Transition;

import java.util.ArrayList;
import java.util.Optional;

public class DFASimulator extends Simulator {

    @Override
    public void simulateAutomaton(FiniteAutomaton automaton, String[] input, Integer simulationId) {
        addTrapState(automaton);
        State currentState = automaton.getStartState();
        int stepCount = 1;
        for (String s : input) {
            for (Transition transition : currentState.getTransitions()) {
                if(transition.getTransitionSymbol().equals(s)) {
                    DfaStep step = new DfaStep(stepCount, currentState, transition.getTargetState(), s);
                    simulatedAutomatonStore.addStepToSimulation(simulationId, step);
                    currentState = transition.getTargetState();
                    stepCount++;
                }
            }
        }
        ArrayList<Step> finalStateSteps = simulatedAutomatonStore.getSimulation(simulationId).getSteps();
        finalStateSteps.get(finalStateSteps.size() - 1).setFinalStep(true);
        if(((DfaStep) finalStateSteps.get(finalStateSteps.size() - 1)).getFinishState().isAcceptState()) {
            simulatedAutomatonStore.getSimulation(simulationId).setFinalState(Simulation.SimulationState.ACCEPT);
            finalStateSteps.get(finalStateSteps.size() - 1).setCurrentState(Simulation.SimulationState.ACCEPT);
        }
    }

    private void addTrapState(FiniteAutomaton automaton) {
        State trapState = new State("TRAP");
        for (String s : automaton.getAlphabet()) {
            trapState.addTransition(new Transition(trapState, trapState, s));
        }

        automaton.getStates().forEach(s -> {
            for (String s1 : automaton.getAlphabet()) {
                Optional<Transition> matching = s.getTransitions().stream().filter(t -> t.getTransitionSymbol().equals(s1)).findAny();
                if(!matching.isPresent()) {
                    s.addTransition(new Transition(s, trapState, s1));
                }
            }
        });

    }
}
