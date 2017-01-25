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

        State initial = new State("0", true);
        converted.addState(initial);
        converted.setStartState(initial);
        converted.setCurrentAcceptState(initial);

        State currentBranch = initial;
        State currentState = initial;
        String prevExpression = "";
        int i = 0;
        int nameNumber = 1;
        Stack<Character> bracketStack = new Stack<>();
        FiniteAutomaton subFA = null;

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
                subFA = parseRegex(subRegex.toString());
                //connect current state to beginning of subfa

                for (State state : subFA.getStates()) {
                    state.setStateName(String.valueOf(nameNumber++));
                }

                currentState.addTransition(new Transition(currentState, subFA.getStartState(), NfaUtils.EPSILON_TRANSITION_SYMBOL));

                subFA.getStates().forEach(converted::addState);
                subFA.getStartState().setInitialState(false);
                i = j;
            }

            if(regex.charAt(i) == '*') {
                //kleene star

                State kleeneInitial;
                State kleeneAccept;


                State newInitial = new State(String.valueOf(nameNumber++));
                State newAccept = new State(String.valueOf(nameNumber++), false, true);

                if(subFA == null) {
                    kleeneAccept = currentState;
                    kleeneInitial = currentState.stepBackwards(prevExpression);
                    converted.addState(newInitial);
                    newInitial.addTransition(new Transition(newInitial, kleeneInitial, NfaUtils.EPSILON_TRANSITION_SYMBOL));
                } else {
                    kleeneInitial = subFA.getStartState();
                    kleeneAccept = subFA.getCurrentAcceptState();
                    newInitial = currentState;
                }

                converted.addState(newAccept);
                converted.setCurrentAcceptState(newAccept);

                newInitial.addTransition(new Transition(newInitial, newAccept, NfaUtils.EPSILON_TRANSITION_SYMBOL));
                kleeneAccept.addTransition(new Transition(kleeneAccept, newAccept, NfaUtils.EPSILON_TRANSITION_SYMBOL));
                kleeneAccept.addTransition(new Transition(kleeneAccept, kleeneInitial, NfaUtils.EPSILON_TRANSITION_SYMBOL));

                kleeneAccept.setAcceptState(false);
                if(kleeneInitial.isInitialState()) {
                    newInitial.setInitialState(true);
                    kleeneInitial.setInitialState(false);
                    converted.setStartState(newInitial);
                }

                currentState = converted.getCurrentAcceptState();
            } else if(regex.charAt(i) == '|') {
                //alternative branch
                State newBranch = new State(String.valueOf(nameNumber++));
                converted.addState(newBranch);
                currentBranch.addTransition(new Transition(currentBranch, newBranch, NfaUtils.EPSILON_TRANSITION_SYMBOL));
                currentBranch = newBranch;
                currentState = newBranch;
            } else if(Character.isLetterOrDigit(regex.charAt(i))){
                prevExpression = String.valueOf(regex.charAt(i));
                State newState = new State(String.valueOf(nameNumber++), false, true);
                converted.setCurrentAcceptState(newState);
                converted.addState(newState);
                currentState.addTransition(new Transition(currentState, newState, String.valueOf(regex.charAt(i))));
                currentState.setAcceptState(false);

                currentState = newState;
            }
            i++;

        }

        return converted;
    }

}
