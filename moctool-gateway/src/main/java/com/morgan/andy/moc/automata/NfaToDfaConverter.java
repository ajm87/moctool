package com.morgan.andy.moc.automata;

import com.morgan.andy.domain.FiniteAutomaton;
import com.morgan.andy.domain.State;
import com.morgan.andy.domain.Transition;
import com.morgan.andy.service.util.NfaUtils;
import org.springframework.security.access.method.P;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Converts an NFA to a DFA using subset construction.
 *
 * @author Andy Morgan (ajm87)
 */
public class NfaToDfaConverter implements Converter<FiniteAutomaton> {

    private boolean hasAddedInitial = false;

    @Override
    public FiniteAutomaton convert(FiniteAutomaton nfa) {
        hasAddedInitial = false;
        // any state that contains a final state should itself be final
        FiniteAutomaton converted = new FiniteAutomaton();

        ArrayList<State> uninitialisedDfa = new ArrayList<>();

        ArrayList<State> toBeComputed = new ArrayList<>();

        ArrayList<State> states = new ArrayList<>();
        states.addAll(nfa.getStates());
        toBeComputed.add(constructStateFromSet(NfaUtils.epsilonClosure(states
                                                                        .stream()
                                                                        .filter(State::isInitialState)
                                                                        .collect(Collectors.toCollection(ArrayList::new))
                                                                        .get(0))));

        uninitialisedDfa.add(new State(toBeComputed.get(0).getStateName(), toBeComputed.get(0).isInitialState(), toBeComputed.get(0).isAcceptState()));

        while(!toBeComputed.isEmpty()) {
            ArrayList<State> toAdd = new ArrayList<>();
            Iterator iterator = toBeComputed.iterator();
            while(iterator.hasNext()) {
                State state = (State) iterator.next();
                for (String s : nfa.getAlphabet()) {
                    ArrayList<State> reachable = NfaUtils.move(getSetFromState(state, states), s);
                    if(!reachable.isEmpty()) {
                        //if not already added to dfa, then add to toAdd, else add transition from state to reachable
                        State existingState = getStateForSet(reachable, uninitialisedDfa);
                        if(existingState == null) {
                            existingState = constructStateFromSet(reachable);
                            uninitialisedDfa.add(existingState);
                            toAdd.add(existingState);
                        }
                        State dfaFromNfa = getDfaStateFromNfaState(state, uninitialisedDfa);
                        dfaFromNfa.addTransition(new Transition(dfaFromNfa, existingState, s));
                    }
                }
                iterator.remove();
            }
            toBeComputed.addAll(toAdd);
        }

        int stateId = 0;
        for (State state : uninitialisedDfa) {
            state.setId(Integer.toString(stateId));
            converted.addState(state);
            stateId++;
        }
        converted.setAlphabet(nfa.getAlphabet());

        return converted;
    }

    private State getDfaStateFromNfaState(State nfaState, ArrayList<State> dfa) {
        Optional<State> found = dfa.stream().filter(s -> s.getStateName().equals(nfaState.getStateName())).findFirst();
        if(found.isPresent()) {
            return found.get();
        }
        return nfaState;
    }

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

    private State getStateForSet(ArrayList<State> states, ArrayList<State> toLook) {
        State combinedState = constructStateFromSet(states);
        Optional<State> found = toLook.stream().filter(s -> compareSetAndCombinedState(states, s)).findFirst();
        if(found.isPresent()) {
            return found.get();
        }
        return null;
    }

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
