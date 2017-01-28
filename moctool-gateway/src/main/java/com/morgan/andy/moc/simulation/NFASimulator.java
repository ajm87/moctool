package com.morgan.andy.moc.simulation;

import com.morgan.andy.domain.FiniteAutomaton;
import com.morgan.andy.domain.State;
import com.morgan.andy.domain.Transition;
import com.morgan.andy.service.util.NfaUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class NFASimulator extends Simulator {

    @Inject
    private SimulatedAutomatonStore simulatedAutomatonStore;


    @Override
    public void simulateAutomaton(FiniteAutomaton automaton, String[] input, Integer simulationId) {
        ArrayList<State> activeStates = new ArrayList<>();
        ArrayList<State> initialState = new ArrayList<>();
        initialState.add(automaton.getStartState());
        AtomicInteger stepCount = new AtomicInteger(1);

        activeStates.addAll(NfaUtils.epsilonClosure(automaton.getStartState()));

        NfaStep firstStep = new NfaStep(stepCount.getAndIncrement(), NfaUtils.EPSILON_TRANSITION_SYMBOL, initialState, new ArrayList<>(activeStates));
        simulatedAutomatonStore.addStepToSimulation(simulationId, firstStep);

        Arrays.stream(input).forEach(s -> {
            ArrayList<State> reached = NfaUtils.move(activeStates, s);
            ArrayList<State> activeBeforeDelete = new ArrayList<>(activeStates);
            activeStates.removeIf(a -> {
               Optional<Transition> toReached = a.getTransitions().stream().filter(t -> reached.contains(t.getTargetState()) && t.getTransitionSymbol().equals(s)).findAny();
                return toReached.isPresent();
            });
            activeStates.addAll(reached);
            NfaStep step = new NfaStep(stepCount.getAndIncrement(), s, activeBeforeDelete, new ArrayList<>(activeStates));
            simulatedAutomatonStore.addStepToSimulation(simulationId, step);
        });
        Optional<State> acceptState = activeStates.stream().filter(State::isAcceptState).findAny();
        ArrayList<Step> steps = simulatedAutomatonStore.getSimulation(simulationId).getSteps();
        steps.get(steps.size() - 1).setFinalStep(true);
        if(acceptState.isPresent()) {
            simulatedAutomatonStore.getSimulation(simulationId).setFinalState(Simulation.SimulationState.ACCEPT);
            steps.get(steps.size() - 1).setCurrentState(Simulation.SimulationState.ACCEPT);
        } else {
            simulatedAutomatonStore.getSimulation(simulationId).setFinalState(Simulation.SimulationState.REJECT);
            steps.get(steps.size() - 1).setCurrentState(Simulation.SimulationState.REJECT);
        }
    }
}
