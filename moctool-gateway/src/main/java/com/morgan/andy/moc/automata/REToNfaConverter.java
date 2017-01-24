package com.morgan.andy.moc.automata;

import com.morgan.andy.domain.FiniteAutomaton;
import com.morgan.andy.domain.State;
import com.morgan.andy.domain.Transition;
import com.morgan.andy.service.util.NfaUtils;

import java.util.Iterator;
import java.util.Stack;

/**
 * @author Andy Morgan (ajm87)
 */
public class REToNfaConverter implements Converter<String> {

    @Override
    public FiniteAutomaton convert(String regex) {
        return parseRegex(regex);
    }

    private FiniteAutomaton parseRegex(String regex) {
        FiniteAutomaton converted = new FiniteAutomaton();

        State initial = new State("0", true, true);
        converted.addState(initial);
        converted.setStartState(initial);
        converted.setCurrentAcceptState(initial);

        State currentBranch = initial;
        State currentState = initial;
        char prevChar = 0;
        int i = 0;
        int nameNumber = 1;
        Stack<Character> bracketStack = new Stack<>();

        while (i < regex.length()) {
            if(regex.charAt(i) == '(') {
                bracketStack.push(regex.charAt(i));
                StringBuilder subRegex = new StringBuilder();
                int j = i + 1;
                while (true) {
                    if(regex.charAt(j) == ')') {
                        bracketStack.pop();
                        if(bracketStack.empty()) {
                            break;
                        }
                    } else if(regex.charAt(j) == '(') {
                        bracketStack.push(regex.charAt(j));
                    }

                    if(!bracketStack.empty()) {
                        subRegex.append(regex.charAt(j));
                    }
                    j++;
                }
                FiniteAutomaton subFA = parseRegex(subRegex.toString());
                //connect current state to beginning of subfa
                //TODO: this should actually be our current state not current branch. Add a getCurrentState method to FA.
                subFA.getStartState().getTransitions().forEach(currentState::addTransition);
                subFA.getStartState().setInitialState(false);
                subFA.setStartState(null);
                i = j;
            }

            if(regex.charAt(i) == '*') {
                //kleene star
                State subNfaState1 = new State(String.valueOf(nameNumber++));
                State subNfaState2 = new State(String.valueOf(nameNumber++));
                converted.addState(subNfaState1);
                converted.addState(subNfaState2);

                currentState.addTransition(new Transition(converted.getCurrentAcceptState(), NfaUtils.EPSILON_TRANSITION_SYMBOL));
                currentState.addTransition(new Transition(subNfaState1, NfaUtils.EPSILON_TRANSITION_SYMBOL));

                for(Iterator<Transition> it = currentState.getTransitions().listIterator(); it.hasNext(); ) {
                    Transition transition = it.next();
                    if(transition.getTransitionSymbol().equals(String.valueOf(prevChar))) {
                        it.remove();
                    }
                }

                subNfaState1.addTransition(new Transition(subNfaState2, String.valueOf(prevChar)));

                subNfaState2.addTransition(new Transition(subNfaState1, NfaUtils.EPSILON_TRANSITION_SYMBOL));
                subNfaState2.addTransition(new Transition(converted.getCurrentAcceptState(), NfaUtils.EPSILON_TRANSITION_SYMBOL));

            } else if(regex.charAt(i) == '|') {
                //alternative branch
                State newBranch = new State(String.valueOf(nameNumber++));
                converted.addState(newBranch);
                currentBranch.addTransition(new Transition(newBranch, NfaUtils.EPSILON_TRANSITION_SYMBOL));
                currentBranch = newBranch;
            } else {
                //TODO: Check for alphanumeric
                prevChar = regex.charAt(i);
                State newState = new State(String.valueOf(nameNumber++), false, true);
                converted.setCurrentAcceptState(newState);
                converted.addState(newState);
                currentState.addTransition(new Transition(newState, String.valueOf(regex.charAt(i))));
                currentState.setAcceptState(false);

                currentState = newState;
            }
            i++;

        }

        return converted;
    }

}
