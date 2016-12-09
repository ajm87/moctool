package com.morgan.andy.service;

import com.morgan.andy.domain.FiniteAutomaton;
import com.morgan.andy.domain.State;
import com.morgan.andy.domain.Transition;
import com.morgan.andy.moc.simulation.DfaStep;
import com.morgan.andy.moc.simulation.NfaStep;
import com.morgan.andy.moc.simulation.SimulatedAutomatonStore;
import com.morgan.andy.moc.simulation.Simulation;
import com.morgan.andy.web.rest.vm.AutomatonVM;
import com.morgan.andy.web.rest.vm.CytoscapeElement;
import com.morgan.andy.web.rest.vm.SimulateVM;

import java.util.*;

public class ModelService {

    private static final String NODE_GROUP = "nodes";
    private static final String EDGE_GROUP = "edges";
    private static final String STATE_NAME_KEY = "name";
    private static final String INITIAL_STATE_KEY = "initial";
    private static final String ACCEPT_STATE_KEY = "accept";
    private static final String X_POS_KEY = "x";
    private static final String Y_POS_KEY = "y";
    private static final String ID_KEY = "id";
    private static final String LABEL_KEY = "label";
    private static final String TARGET_STATE_KEY = "target";
    private static final String SOURCE_STATE_KEY = "source";

    public FiniteAutomaton convertVmToAutomaton(AutomatonVM automatonVM) {
        FiniteAutomaton converted = new FiniteAutomaton();
        Map<String, State> states = new HashMap<>();
        Map<String, ArrayList<Transition>> transitions = new HashMap<>();
        Set<String> alphabet = new HashSet<>();
        State startState = null;

        for (CytoscapeElement e : automatonVM.getElements()) {
            if(e.getGroup().equals(NODE_GROUP)) {
                State newState = new State();
                newState.setId(e.getData().get(ID_KEY));
                newState.setStateName(e.getData().get(STATE_NAME_KEY));
                newState.setInitialState(Boolean.valueOf(e.getData().get(INITIAL_STATE_KEY)));
                newState.setAcceptState(Boolean.valueOf(e.getData().get(ACCEPT_STATE_KEY)));
                newState.setxPos(e.getPosition().get(X_POS_KEY));
                newState.setyPos(e.getPosition().get(Y_POS_KEY));
                transitions.put(newState.getId(), new ArrayList<>());
                states.put(newState.getId(), newState);
                if(newState.isInitialState()) {
                    startState = newState;
                }
            }
        }

        automatonVM.getElements().forEach(e -> {
            if(e.getGroup().equals(EDGE_GROUP)) {
                Transition transition = new Transition();
                transition.setTransitionSymbol(e.getData().get(LABEL_KEY));
                transition.setTargetState(states.get(e.getData().get(TARGET_STATE_KEY)));
                transitions.get(e.getData().get(SOURCE_STATE_KEY)).add(transition);
                if(!transition.getTransitionSymbol().equals("\u03b5")) {
                    alphabet.add(transition.getTransitionSymbol());
                }
            }
        });

        transitions.forEach((k, v) -> {
            State state = states.get(k);
            v.forEach(state::addTransition);
        });

        converted.setAlphabet(alphabet.toArray(new String[alphabet.size()]));
        ArrayList<State> statesToAdd = new ArrayList<>();
        statesToAdd.addAll(states.values());
        converted.setStates(statesToAdd);
        converted.setStartState(startState);

        return converted;
    }

    public AutomatonVM convertAutomatonToVm(FiniteAutomaton automaton) {
        AutomatonVM converted = new AutomatonVM();

        automaton.getStates().forEach(s -> {
            CytoscapeElement element = new CytoscapeElement();
            element.addDataElement(STATE_NAME_KEY, s.getStateName());
            element.addDataElement(ID_KEY, s.getId());
            element.addDataElement(INITIAL_STATE_KEY, Boolean.toString(s.isInitialState()));
            element.addDataElement(ACCEPT_STATE_KEY, Boolean.toString(s.isAcceptState()));
            element.setGroup(NODE_GROUP);
            converted.addElement(element);
            s.getTransitions().forEach(t -> {
                CytoscapeElement transition = new CytoscapeElement();
                transition.addDataElement(SOURCE_STATE_KEY, s.getId());
                transition.addDataElement(TARGET_STATE_KEY, t.getTargetState().getId());
                transition.addDataElement(LABEL_KEY, t.getTransitionSymbol());
                transition.setGroup(EDGE_GROUP);
                converted.addElement(transition);
            });
        });

        return converted;
    }

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
        if(simulation.getSteps().get(0) instanceof DfaStep) {
            simulation.getSteps().forEach(step -> {
                DfaStep dfaStep = (DfaStep) step;
                dfaStep.getStartState().setTransitions(null);
                dfaStep.getFinishState().setTransitions(null);
            });
        } else if(simulation.getSteps().get(0) instanceof NfaStep) {
            simulation.getSteps().forEach(step -> {
                NfaStep nfaStep = (NfaStep) step;
                nfaStep.getStartActiveStates().forEach(sas -> sas.setTransitions(null));
                nfaStep.getFinishActiveStates().forEach(fas -> fas.setTransitions(null));
            });
        }
        return simulation;
    }

}
