package com.morgan.andy.service.util;

import com.morgan.andy.domain.State;
import com.morgan.andy.domain.Transition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for various useful NFA algorithms.
 *
 * @author Andy Morgan (ajm87)
 */
public class NfaUtils {

    public static final String EPSILON_TRANSITION_SYMBOL = "\u03b5";

    /**
     * Computes the epsilon closure of a given ArrayList of states.
     * @param states The list of states to compute the closure over
     * @return a list of states representing the closure of the given list
     */
    public static ArrayList<State> epsilonClosure(ArrayList<State> states) {
        ArrayList<State> closure = new ArrayList<>();
        closure.addAll(states);

        for (int i = 0; i < closure.size(); i++) {
            State state = closure.get(i);
            List<State> reachable = state.getTransitions().stream().filter(transition -> transition.getTransitionSymbol().equals(EPSILON_TRANSITION_SYMBOL)).map(Transition::getTargetState).collect(Collectors.toList());
            if(!reachable.containsAll(states)) {
                closure.addAll(reachable);
            }
        }

        return closure;
    }

    /**
     * Computes the epsilon closure of a given state.
     * @param state The state to compute the closure over
     * @return a list of states representing the closure of the given list
     */
    public static ArrayList<State> epsilonClosure(State state) {
        ArrayList<State> closure = new ArrayList<>();
        closure.add(state);

//        closure.addAll(state.getTransitions().stream().filter(transition -> transition.getTransitionSymbol().equals(EPSILON_TRANSITION_SYMBOL)).map(Transition::getTargetState).collect(Collectors.toList()));

        return epsilonClosure(closure);
    }

    public static ArrayList<State> move(ArrayList<State> inputStates, String symbol) {
        ArrayList<State> ret = new ArrayList<>();
        ArrayList<State> out = new ArrayList<>();
        inputStates.forEach(s -> s.getTransitions().forEach(t -> {
            if(t.getTransitionSymbol().equals(symbol)) {
                out.add(t.getTargetState());
            }
        }));
        ArrayList<State> closure = epsilonClosure(out);
        closure.forEach(s -> {
            if(!ret.contains(s)) {
                ret.add(s);
            }
        });
        return ret;
    }

    public static ArrayList<State> move(State inputState, String symbol) {
        ArrayList<State> arrayList = new ArrayList<>();
        arrayList.add(inputState);
        return move(arrayList, symbol);
    }

}
