package com.morgan.andy.moc.simulation;

import com.morgan.andy.domain.FiniteAutomaton;
import com.morgan.andy.domain.State;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;

@EnableAsync
public abstract class Simulator {

    @Inject
    protected SimulatedAutomatonStore simulatedAutomatonStore;

    @Async
    public abstract void simulateAutomaton(FiniteAutomaton automaton, String[] input, Integer simulationId);

    public int loadAutomaton(FiniteAutomaton automaton, String[] input) {
        Simulation simulation = new Simulation();
        simulatedAutomatonStore.addSimulation(simulation.getId(), simulation);
        return simulation.getId();
    }

}
