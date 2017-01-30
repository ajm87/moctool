package com.morgan.andy.moc.automata;

import com.morgan.andy.domain.FiniteAutomaton;
import com.morgan.andy.domain.State;
import com.morgan.andy.domain.Transition;
import com.morgan.andy.service.util.NfaUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * @author Andy Morgan (ajm87)
 */
public class REToNfaConverter implements Converter<String> {

    private static final String EMPTY_STRING_CHARACTER = "!";

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
                currentState.setAcceptState(false);
                subFA.getStates().forEach(converted::addState);
                subFA.getStartState().setInitialState(false);
                if(subFA.getCurrentAcceptStates().size() > 1) {
                    State newAccept = new State(String.valueOf(nameNumber++), false, true);
                    subFA.getCurrentAcceptStates().forEach(c -> {
                        c.addTransition(new Transition(c, newAccept, NfaUtils.EPSILON_TRANSITION_SYMBOL));
                        c.setAcceptState(false);
                    });
                    converted.addState(newAccept);
                    subFA.addState(newAccept);
                    converted.setCurrentAcceptState(newAccept);
                    currentState = newAccept;
                } else {
                    currentState = subFA.getCurrentAcceptStates().get(0);
                }
                i = j;
            }

            if(regex.charAt(i) == '*') {
                //kleene star

                State currentInitial;
                ArrayList<State> currentAccept = new ArrayList<>();

                State newInitial = new State(String.valueOf(nameNumber++));
                State newAccept = new State(String.valueOf(nameNumber++), false, true);

                if(subFA == null) {
                    currentAccept.add(currentState);
                    currentInitial = currentState.stepBackwards(prevExpression);
                } else {
                    currentInitial = subFA.getStartState();
                    currentAccept = subFA.getCurrentAcceptStates();
                }

                converted.addState(newInitial);
                converted.addState(newAccept);
                converted.setCurrentAcceptState(newAccept);

                currentAccept.forEach(c -> c.setAcceptState(false));
                currentAccept.forEach(c -> c.addTransition(new Transition(c, newAccept, NfaUtils.EPSILON_TRANSITION_SYMBOL)));

                newInitial.addTransition(new Transition(newInitial, newAccept, NfaUtils.EPSILON_TRANSITION_SYMBOL));

                for(Iterator<Transition> it = currentInitial.getIncomingTransitions().iterator(); it.hasNext(); ) {
                    Transition t = it.next();
                    t.setTargetState(newInitial);
                    newInitial.addIncomingTransition(t);
                    it.remove();
                }

                newInitial.addTransition(new Transition(newInitial, currentInitial, NfaUtils.EPSILON_TRANSITION_SYMBOL));
                currentAccept.forEach(c -> c.addTransition(new Transition(c, currentInitial, NfaUtils.EPSILON_TRANSITION_SYMBOL)));

                if(currentBranch.equals(currentInitial)) {
                    currentBranch = newInitial;
                }

                if(currentInitial.isInitialState()) {
                    newInitial.setInitialState(true);
                    currentBranch = newInitial;
                    currentInitial.setInitialState(false);
                    converted.setStartState(newInitial);
                }

                currentState = converted.getCurrentAcceptState();
            } else if(regex.charAt(i) == '|') {
                subFA = null;
                //alternative branch
                State newBranch = new State(String.valueOf(nameNumber++));
                converted.addState(newBranch);
                currentBranch.addTransition(new Transition(currentBranch, newBranch, NfaUtils.EPSILON_TRANSITION_SYMBOL));
                currentBranch = newBranch;
                currentState = newBranch;
            } else if(Character.isLetterOrDigit(regex.charAt(i)) || String.valueOf(regex.charAt(i)).equals(EMPTY_STRING_CHARACTER)) {
                subFA = null;
                String symbol = String.valueOf(regex.charAt(i));
                if(symbol.equals(EMPTY_STRING_CHARACTER)) {
                    symbol = NfaUtils.EPSILON_TRANSITION_SYMBOL;
                }
                prevExpression = symbol;
                State newState = new State(String.valueOf(nameNumber++), false, true);
                converted.setCurrentAcceptState(newState);
                converted.addState(newState);
                currentState.addTransition(new Transition(currentState, newState, symbol));
                currentState.setAcceptState(false);

                currentState = newState;
            }
            i++;
        }

        return converted;
    }

}
