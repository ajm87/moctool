package com.morgan.andy.moc.automata;

import com.morgan.andy.domain.FiniteAutomaton;
import com.morgan.andy.domain.State;
import com.morgan.andy.domain.Transition;
import com.morgan.andy.service.util.NfaUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Converts an NFA to a DFA using subset construction.
 *
 * @author Andy Morgan (ajm87)
 */
public class NfaToDfaConverter implements Converter<FiniteAutomaton, FiniteAutomaton> {

    private boolean hasAddedInitial = false;

    /**
     * Converts an NFA to a DFA using subset construction.
     * @param nfa
     * @return
     */
    @Override
    public FiniteAutomaton convert(FiniteAutomaton nfa) {
        hasAddedInitial = false;
        FiniteAutomaton converted = new FiniteAutomaton();
        ArrayList<State> uninitialisedDfa = new ArrayList<>();
        ArrayList<State> toBeComputed = new ArrayList<>();
        ArrayList<State> states = new ArrayList<>();
        states.addAll(nfa.getStates());

        // compute the epsilon closure of the NFA's initial state, then add the set of states to our compute list
        toBeComputed.add(constructStateFromSet(NfaUtils.epsilonClosure(states
                                                                        .stream()
                                                                        .filter(State::isInitialState)
                                                                        .collect(Collectors.toCollection(ArrayList::new))
                                                                        .get(0))));

        // add an initial state to the new DFA
        uninitialisedDfa.add(new State(toBeComputed.get(0).getStateName(), toBeComputed.get(0).isInitialState(), toBeComputed.get(0).isAcceptState()));

        // while we have states to compute
        while(!toBeComputed.isEmpty()) {
            ArrayList<State> toAdd = new ArrayList<>();
            Iterator iterator = toBeComputed.iterator();
            // for each state to compute
            while(iterator.hasNext()) {
                State state = (State) iterator.next();
                for (String s : nfa.getAlphabet()) {
                    // move from this state with the input letter s
                    ArrayList<State> reachable = NfaUtils.move(getSetFromState(state, states), s);
                    if(!reachable.isEmpty()) {
                        // get the state that represents the reachable states after move
                        State existingState = getStateForSet(reachable, uninitialisedDfa);
                        // if there is no state, we haven't added it to our DFA yet. Add it and then add it to our compute list
                        if(existingState == null) {
                            existingState = constructStateFromSet(reachable);
                            uninitialisedDfa.add(existingState);
                            toAdd.add(existingState);
                        }
                        // add a transition from state to reachable
                        State dfaFromNfa = getDfaStateFromNfaState(state, uninitialisedDfa);
                        dfaFromNfa.addTransition(new Transition(dfaFromNfa, existingState, s));
                    }
                }
                iterator.remove();
            }
            toBeComputed.addAll(toAdd);
        }

        // set our state IDs
        int stateId = 0;
        for (State state : uninitialisedDfa) {
            state.setId(Integer.toString(stateId));
            converted.addState(state);
            if(state.isInitialState()) {
                converted.setStartState(state);
            }
            stateId++;
        }
        // set our new DFA alphabet
        converted.setAlphabet(nfa.getAlphabet());

        return converted;
    }

    /**
     * Gets the equivalent DFA state from a provided NFA state
     * @param nfaState the state to look for in the DFA
     * @param dfa the collection of states that represents the DFA
     * @return the state in the DFA that matches nfaState
     */
    private State getDfaStateFromNfaState(State nfaState, ArrayList<State> dfa) {
        Optional<State> found = dfa.stream().filter(s -> s.getStateName().equals(nfaState.getStateName())).findFirst();
        if(found.isPresent()) {
            return found.get();
        }
        return nfaState;
    }

    /**
     * Given a state that represents a set, return the original set of states that combined
     * to make this state.
     * @param state the combined state
     * @param toLook an arraylist of states to look in for the original set
     * @return an arraylist containing all states that make up state
     */
    private ArrayList<State> getSetFromState(State state, ArrayList<State> toLook) {
        String[] individualNames = state.getStateName().split(",");
        ArrayList<State> ret = new ArrayList<>();
        Arrays.stream(individualNames).forEach(n -> toLook.forEach(s -> {
                if(s.getStateName().equals(n)) {
                    ret.add(s);
                }
            }));
        return ret;
    }

    /**
     * Given a set of states, return the state that represents this set in toLook.
     * @param states The set of states to find the single state for
     * @param toLook The arraylist in which to search for the combined state
     * @return the combined state, or null if not found
     */
    private State getStateForSet(ArrayList<State> states, ArrayList<State> toLook) {
        Optional<State> found = toLook.stream().filter(s -> compareSetAndCombinedState(states, s)).findFirst();
        if(found.isPresent()) {
            return found.get();
        }
        return null;
    }

    /**
     * Compares equivalence of a set of states and a combined state.
     * @param states the set of states
     * @param combined the combined state
     * @return true if the set and combined state are equal, false otherwise
     */
    private boolean compareSetAndCombinedState(ArrayList<State> states, State combined) {
        String[] individualStateNames = combined.getStateName().split(",");
        if(individualStateNames.length != states.size()) {
            return false;
        }
        int combinedUnmatched = individualStateNames.length;
        for (State s : states) {
            if(Arrays.asList(individualStateNames).contains(s.getStateName())) {
                combinedUnmatched--;
            }
        }
        return combinedUnmatched == 0;
    }

    /**
     * Given a set of states, create a new state that represents the set.
     * @param states the set of states to combine
     * @return a new state representing the provided set of states
     */
    private State constructStateFromSet(ArrayList<State> states) {
        boolean isInitial = false;
        boolean isAccept = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < states.size(); i++) {
            State s = states.get(i);
            if(!isInitial && !hasAddedInitial && s.isInitialState()) {
                isInitial = s.isInitialState();
                hasAddedInitial = true;
            }
            if(!isAccept) {
                isAccept = s.isAcceptState();
            }

            sb.append(s.getStateName());
            if(i < states.size() - 1) {
                sb.append((","));
            }
        }

        return new State(sb.toString(), isInitial, isAccept);
    }

}
