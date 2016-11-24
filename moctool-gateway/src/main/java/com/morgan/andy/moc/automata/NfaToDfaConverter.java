package com.morgan.andy.moc.automata;

import com.morgan.andy.domain.FiniteAutomaton;
import com.morgan.andy.domain.State;
import com.morgan.andy.domain.Transition;
import com.morgan.andy.moc.Converter;
import com.morgan.andy.web.rest.vm.ModelVM;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Convert NFA to DFA
 */
public class NfaToDfaConverter implements Converter {

    @Override
    public FiniteAutomaton convert(FiniteAutomaton nfa) {
        // any state that contains a final state should itself be final
        FiniteAutomaton converted = new FiniteAutomaton();

        ArrayList<ArrayList<State>> closures = new ArrayList<>();
        ArrayList<State> states = new ArrayList<>();
        states.addAll(nfa.getStates().values());

        ArrayList<State> initial;
        initial = states.stream().filter(State::isStartState).collect(Collectors.toCollection(ArrayList::new));

        ArrayList<State> closure = epsilonClosure(initial);
        closures.add(closure);
        // add initial state to converted automaton as start state
        State prevState = new State("0", true);
        converted.addState("0", prevState);

        ArrayList<State> computed = lad(nfa.getAlphabet(), closure, prevState);
        int sName = 1;
        for (State s : computed) {
            s.setStateName(String.valueOf(sName));
            converted.addState(s.getStateName(), s);
            sName++;
        }

        return converted;
    }

    private ArrayList<State> lad(String[] alphabet, ArrayList<State> closure, State prevState) {
        ArrayList<State> computedStates = new ArrayList<>();
        int stateNum = Integer.valueOf(prevState.getStateName()) + 1;
        for (String a : alphabet) {
            ArrayList<State> newClosure = move(closure, a);
            if(newClosure.isEmpty()) {
                continue;
            }
            if(newClosure.equals(closure)) {
                prevState.addTransition(new Transition(prevState, a));
                continue;
            }
            State stateAfterMove = new State(String.valueOf(stateNum));
            computedStates.add(stateAfterMove);
            prevState.addTransition(new Transition(stateAfterMove, a));
            computedStates.addAll(lad(alphabet, newClosure, stateAfterMove));
            stateNum++;
        }
        return computedStates;
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
            for (State state : closure) {
                for (Transition transition : state.getTransitions()) {
                    if (transition.getTransitionSymbol().equals("&epsilon;")) {
//                        if (!closure.containsAll(toAdd)) {
                            toAdd.add(transition.getTargetState());
//                        }
                    }
                }
                if(closure.containsAll(toAdd)) {
                    continue;
                } else {
                    Set<State> tempSet = new HashSet<>();
                    tempSet.addAll(closure);
                    tempSet.addAll(toAdd);
                    closure = new ArrayList<>(tempSet);
                    toAdd.clear();
                }
            }
            if(toAdd.isEmpty()) {
                break;
            }
        }
        return closure;
    }

    private ArrayList<State> move(ArrayList<State> inputStates, String symbol) {
        ArrayList<State> out = new ArrayList<>();
        inputStates.forEach(s -> s.getTransitions().forEach(t -> {
            if(t.getTransitionSymbol().equals(symbol)) {
                out.add(t.getTargetState());
            }
        }));
        return epsilonClosure(out);
    }

}
