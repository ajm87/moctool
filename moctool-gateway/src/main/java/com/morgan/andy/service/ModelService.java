package com.morgan.andy.service;

import com.morgan.andy.domain.FiniteAutomaton;
import com.morgan.andy.domain.State;
import com.morgan.andy.domain.Transition;
import com.morgan.andy.web.rest.vm.ModelVM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ModelService {

    /**
     * Convert a model of computation view model to a concrete automata data structure.
     * @param modelVM The view model to convert
     */
    public FiniteAutomaton vmToAutomataStructure(ModelVM modelVM) {
        FiniteAutomaton automaton = new FiniteAutomaton();
        HashMap<String, State> states = new HashMap<>();

        // construct our states
        Arrays.stream(modelVM.getStateVMs()).forEach(s -> {
            State state = new State(s.getStateName());
            state.setStartState(s.isStart());
            state.setFinalState(s.isFinal());
            states.put(s.getId(), state);
        });

        // construct our transitions and add them to appropriate states
        Arrays.stream(modelVM.getTransitionVMs()).forEach(t -> {
            Transition transition = new Transition(states.get(t.getTargetId()), t.getLabel());
            states.get(t.getSourceId()).addTransition(transition);
        });

        automaton.setStates(states);

        return automaton;

    }

    public ModelVM automataStructureToVm(FiniteAutomaton finiteAutomaton) {
        ModelVM modelVM = new ModelVM();
        ArrayList<ModelVM.StateVM> stateVMs = new ArrayList<>();
        ArrayList<ModelVM.TransitionVM> transitionVMs = new ArrayList<>();
        finiteAutomaton.getStates().forEach((k, v) -> {
            stateVMs.add(new ModelVM.StateVM(k, "-1", "-1", "", false, false));
            v.getTransitions().forEach(t -> {
                transitionVMs.add(new ModelVM.TransitionVM(t.getTransitionSymbol(), k, t.getTargetState().getStateName(), t.getTransitionSymbol()));
            });
        });
        modelVM.setStateVMs(stateVMs.toArray(new ModelVM.StateVM[0]));
        modelVM.setTransitionVMs(transitionVMs.toArray(new ModelVM.TransitionVM[0]));
        return modelVM;
    }

}
