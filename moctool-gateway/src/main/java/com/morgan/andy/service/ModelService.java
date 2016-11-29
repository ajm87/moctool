package com.morgan.andy.service;

import com.morgan.andy.domain.FiniteAutomaton;
import com.morgan.andy.domain.State;
import com.morgan.andy.moc.simulation.SimulatedAutomatonStore;
import com.morgan.andy.moc.simulation.Simulation;

import java.util.HashMap;

public class ModelService {

    public FiniteAutomaton populateTransitionStates(FiniteAutomaton finiteAutomaton) {
        HashMap<String, State> stateMap = new HashMap<>();
        finiteAutomaton.getStates().forEach(s -> stateMap.put(s.getStateName(), s));
        finiteAutomaton.getStates().forEach(s -> s.getTransitions().forEach(t -> {
            t.setTargetState(stateMap.get(t.getTargetState().getStateName()));
        }));
        return finiteAutomaton;
    }

    public FiniteAutomaton removeTransitionStates(FiniteAutomaton finiteAutomaton) {
        finiteAutomaton.getStates().forEach(s -> s.getTransitions().forEach(t -> {
            t.setTargetState(new State(t.getTargetState().getStateName()));
        }));
        return finiteAutomaton;
    }

    public Simulation removeTransitionsFromSimulation(Simulation simulation) {
        simulation.getSteps().forEach(step -> {
            step.getStartState().setTransitions(null);
            step.getFinishState().setTransitions(null);
        });
        return simulation;
    }

}
