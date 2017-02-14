package com.morgan.andy.moc.automata;

import com.morgan.andy.domain.FiniteAutomaton;
import com.morgan.andy.domain.State;
import com.morgan.andy.domain.Transition;
import com.morgan.andy.service.util.NfaUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Andy Morgan (ajm87)
 */
public class NfaToReConverter implements Converter<FiniteAutomaton, String> {

    private static String LEFT_PARENTHESIS = "(";
    private static String RIGHT_PARENTHESIS = ")";
    private static String KLEENE_STAR = "*";
    private static String OR = "|";

    @Override
    public String convert(FiniteAutomaton automaton) {
        //convert to gtg
        automaton = prepare(automaton);
        List<State> finalStates;
        finalStates = automaton.getStates().stream().filter(State::isAcceptState).collect(Collectors.toList());
        State chosenFinalState = finalStates.get(0);
        State initial = automaton.getStartState();
        for(Iterator<State> it = automaton.getStates().iterator(); it.hasNext(); ) {
            State s = it.next();
            if(!s.equals(chosenFinalState) && !s.equals(initial)) {
                ArrayList<Transition> transitions = regexTransitionsFromState(s, automaton);
                removeOldTransitions(automaton);
                transitions.forEach(t -> t.getSourceState().addTransition(t));
                it.remove();
            }
        }

        //get regex from gtg
        String ii = getTransitionExpressionsBetweenStates(initial, initial);
        String ij = getTransitionExpressionsBetweenStates(initial, chosenFinalState);
        String jj = getTransitionExpressionsBetweenStates(chosenFinalState, chosenFinalState);
        String ji = getTransitionExpressionsBetweenStates(chosenFinalState, initial);

        String temp = concat(kleeneStar(ii), concat(ij, concat(kleeneStar(jj), ji)));
        String temp2 = concat(kleeneStar(ii), concat(ij, kleeneStar(jj)));
        return concat(kleeneStar(temp), temp2);
    }

    private FiniteAutomaton prepare(FiniteAutomaton automaton) {
        List<State> finalStates = automaton.getStates().stream().filter(State::isAcceptState).collect(Collectors.toList());
        if(finalStates.size() > 1) {
            State newFinal = new State("f", false, true);
            finalStates.forEach(f -> {
                f.addTransition(new Transition(f, newFinal, NfaUtils.EPSILON_TRANSITION_SYMBOL));
                f.setAcceptState(false);
            });
            automaton.addState(newFinal);
        }
        automaton.getStates().forEach(s -> {
            automaton.getStates().forEach(t -> {
                List<Transition> transitions = s.getTransitions().stream().filter(r -> r.getTargetState().equals(t)).collect(Collectors.toList());
                if(transitions.isEmpty()) {
                    s.addTransition(new Transition(s, t, ""));
                }
                if(transitions.size() > 1) {
                    Iterator<Transition> it = transitions.iterator();
                    Transition firstTrans = it.next();
                    String symbol = firstTrans.getTransitionSymbol();
                    it.remove();
                    while(it.hasNext()) {
                        Transition trans = it.next();
                        symbol = or(symbol, trans.getTransitionSymbol());
                        it.remove();
                    }
                    s.addTransition(new Transition(s, t, symbol));
                }
            });
        });
        return automaton;
    }

    private void removeOldTransitions(FiniteAutomaton automaton) {
        automaton.getStates().forEach(s -> {
            for(Iterator<Transition> it = s.getTransitions().iterator(); it.hasNext();) {
                Transition t = it.next();
                t.getTargetState().removeIncomingTransition(t);
                it.remove();
            }
        });
    }

    private ArrayList<Transition> regexTransitionsFromState(State state, FiniteAutomaton automaton) {
        ArrayList<Transition> transitions = new ArrayList<>();
        automaton.getStates().forEach(s -> {
            if(!s.equals(state)) {
                automaton.getStates().forEach(n -> {
                    if(!n.equals(state)) {
                        transitions.add(new Transition(s, n, getRegexFromRemoval(s, n, state)));
                    }
                });
            }
        });
        return transitions;
    }

    /**
     * Uses equation regex(pq) = regex(pq) + regex(pk)regex(kk)*regex(kq)
     * where p = from, q = to, k = remove
     * @param from
     * @param to
     * @param remove
     * @return
     */
    private String getRegexFromRemoval(State from, State to, State remove) {
        String fromTo = getTransitionExpressionsBetweenStates(from, to);
        String fromRemove = getTransitionExpressionsBetweenStates(from, remove);
        String removeRemove = getTransitionExpressionsBetweenStates(remove, remove);
        String removeTo = getTransitionExpressionsBetweenStates(remove, to);

        String kleeneRemove = kleeneStar(removeRemove);
        String fromRemoveKleeneRemove = concat(fromRemove, kleeneRemove);
        String fromRemoveKleeneRemoveRemoveTo = concat(fromRemoveKleeneRemove, removeTo);

        return or(fromTo, fromRemoveKleeneRemoveRemoveTo);
    }

    private String kleeneStar(String regex) {
        if(regex.equals(NfaUtils.EPSILON_TRANSITION_SYMBOL) || regex.isEmpty()) {
            return NfaUtils.EPSILON_TRANSITION_SYMBOL;
        }
        if(regex.length() > 1) {
            regex = LEFT_PARENTHESIS + regex + RIGHT_PARENTHESIS;
        }
        regex = regex + KLEENE_STAR;
        return regex;
    }

    private String concat(String regex1, String regex2) {
        if(regex1.isEmpty() || regex2.isEmpty()) {
            return "";
        }
        if(regex1.equals(NfaUtils.EPSILON_TRANSITION_SYMBOL)) {
            return regex2;
        }
        if(regex2.equals(NfaUtils.EPSILON_TRANSITION_SYMBOL)) {
            return regex1;
        }
        if(regex1.contains(OR)) {
            regex1 = LEFT_PARENTHESIS + regex1 + RIGHT_PARENTHESIS;
        }
        if(regex2.contains(OR)) {
            regex2 = LEFT_PARENTHESIS + regex2 + RIGHT_PARENTHESIS;
        }
        return regex1 + regex2;
    }

    private String or(String regex1, String regex2) {
        if(regex1.equals(NfaUtils.EPSILON_TRANSITION_SYMBOL) && regex2.equals(NfaUtils.EPSILON_TRANSITION_SYMBOL)) {
            return regex1;
        }
        if(regex1.isEmpty()) {
            return regex2;
        }
        if(regex2.isEmpty()) {
            return regex1;
        }
        return regex1 + OR + regex2;
    }

    private String getTransitionExpressionsBetweenStates(State from, State to){
        for (Transition t : from.getTransitions()) {
            if(t.getTargetState().equals(to)) {
                return t.getTransitionSymbol();
            }
        }
        return "";
    }

}
