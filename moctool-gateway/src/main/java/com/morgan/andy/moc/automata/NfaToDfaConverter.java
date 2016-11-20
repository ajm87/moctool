package com.morgan.andy.moc.automata;

import com.morgan.andy.domain.FiniteAutomaton;
import com.morgan.andy.domain.State;
import com.morgan.andy.domain.Transition;
import com.morgan.andy.moc.Converter;
import com.morgan.andy.web.rest.vm.ModelVM;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Convert NFA to DFA
 */
public class NfaToDfaConverter implements Converter {

    @Override
    public FiniteAutomaton convert(FiniteAutomaton nfa) {
        ModelVM converted = new ModelVM();
        return nfa;
    }

    /**
     * Compute the epsilon closure
     */
    private ArrayList<State> epsilonClosure(ArrayList<State> states) {
        ArrayList<State> closure = new ArrayList<>();
        // a closure contains all of the states we wish to compute it on
        closure.addAll(states);

        while(true) {
            ArrayList<State> toAdd = new ArrayList<>();
            for (State state : states) {
                for (Transition transition : state.getTransitions()) {
                    if (transition.getTransitionSymbol().equals(" ")) {
                        if (!closure.containsAll(toAdd)) {
                            toAdd.add(transition.getTargetState());
                        }

                    }
                }
            }
            if(toAdd.isEmpty()) {
                break;
            }
            Set<State> tempSet = new HashSet<>();
            tempSet.addAll(closure);
            tempSet.addAll(toAdd);
            closure = new ArrayList<>(tempSet);
        }
        return closure;
    }

}
