package com.morgan.andy.service;

import com.morgan.andy.domain.*;
import com.morgan.andy.moc.automata.NfaToDfaConverter;
import com.morgan.andy.moc.automata.REToNfaConverter;
import com.morgan.andy.moc.simulation.*;
import com.morgan.andy.service.util.NfaUtils;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;

/**
 * @author Andy Morgan (ajm87)
 */
@Service
public class HomeworkService {

    @Inject
    private ModelService modelService;

    @Inject
    private DFASimulator dfaSimulator;

    @Inject
    private NFASimulator nfaSimulator;

    @Inject
    private NfaToDfaConverter nfaToDfaConverter;

    @Inject
    private REToNfaConverter reToNfaConverter;

    public boolean isAnswerCorrect(HomeworkQuestions homeworkQuestions, String answer) {
        Long questionRefId = homeworkQuestions.getQuestionsRef().getId();
        if(questionRefId.equals(QuestionsRef.ACCEPTS_STRING_QUESTION_ID)) {
            return markAcceptsStringQuestion(homeworkQuestions, answer);
        } else if(questionRefId.equals(QuestionsRef.ACCEPTS_REGEX_QUESTION_ID)) {
            return markAcceptsRegexQuestion(homeworkQuestions, answer);
        } else if(questionRefId.equals(QuestionsRef.EQUIVALENT_FA_QUESTION_ID)) {
            return markEquivalentFaQuestion(homeworkQuestions, answer);
        }
        throw new IllegalArgumentException("Invalid question for homework!");
    }

    private boolean markAcceptsStringQuestion(HomeworkQuestions question, String answer) {
        FiniteAutomaton fa = modelService.convertJsonStringToAutomaton(answer);

        if(!Arrays.asList(fa.getAlphabet()).containsAll(Arrays.asList(question.getContext().split("")))) {
            return false;
        }

        Simulator simulator;

        if (isNfa(fa)) {
            simulator = nfaSimulator;
        } else {
            simulator = dfaSimulator;
        }

        Simulation sim = simulator.loadAutomaton(fa, question.getContext().split(""));
        simulator.simulateAutomaton(fa, question.getContext().split(""), sim.getId());
        return sim.getFinalState().equals(Simulation.SimulationState.ACCEPT);
    }

    private boolean markAcceptsRegexQuestion(HomeworkQuestions question, String answer) {
        return false;
    }

    private boolean markEquivalentFaQuestion(HomeworkQuestions question, String answer) {
        FiniteAutomaton automaton1 = modelService.convertJsonStringToAutomaton(answer);
        FiniteAutomaton automaton2 = modelService.convertJsonStringToAutomaton(question.getContext());

        // need to convert to dfa for comparison
        automaton1 = nfaToDfaConverter.convert(automaton1);
        automaton2 = nfaToDfaConverter.convert(automaton2);
        DFASimulator.addTrapState(automaton1);
        DFASimulator.addTrapState(automaton2);

        return areAutomataEquivalent(automaton1, automaton2);
    }

    /**
     * Hopcroft Karp algorithm
     * @param automaton1
     * @param automaton2
     * @return
     */
    private boolean areAutomataEquivalent(FiniteAutomaton automaton1, FiniteAutomaton automaton2) {
        if(!Arrays.equals(automaton1.getAlphabet(), automaton2.getAlphabet())) {
            return false;
        }
        Map<State, Set<State>> setCollection = new HashMap<>();
        Deque<Set<State>> stack = new ArrayDeque<>();

        automaton1.getStates().forEach(s -> {
            Set<State> automaton1Initial = new HashSet<>();
            automaton1Initial.add(s);
            setCollection.put(s, automaton1Initial);
        });
        automaton2.getStates().forEach(s -> {
            Set<State> automaton2Initial = new HashSet<>();
            automaton2Initial.add(s);
            setCollection.put(s, automaton2Initial);
        });
        merge(setCollection, automaton1.getStartState(), automaton2.getStartState(), automaton2.getStartState());
        stack.push(setCollection.get(automaton2.getStartState()));

        while(!stack.isEmpty()) {
            Set<State> set = stack.pop();
            for (String l : automaton1.getAlphabet()) {
                State r1 = find(setCollection, NfaUtils.move((State) set.toArray()[0], l).get(0));
                State r2 = find(setCollection, NfaUtils.move((State) set.toArray()[1], l).get(0));
                if(!r1.equals(r2)) {
                    merge(setCollection, r1, r2, r2);
                    Set<State> toPush = new HashSet<>();
                    toPush.add(r1);
                    toPush.add(r2);
                    stack.push(toPush);
                }
            }
        }

        boolean equivalent = true;
        for (Map.Entry<State, Set<State>> stateSetEntry : setCollection.entrySet()) {
            boolean foundNonAccept = false;
            boolean foundAccept = false;
            for (State state : stateSetEntry.getValue()) {
                if(state.isAcceptState()) {
                    foundAccept = true;
                } else {
                    foundNonAccept = true;
                }
            }
            if(foundNonAccept && foundAccept) {
                equivalent = false;
            }
        }

        return equivalent;
    }

    private State find(Map<State, Set<State>> setCollection, State contains) {
        for (Map.Entry<State, Set<State>> stateSetEntry : setCollection.entrySet()) {
            if(stateSetEntry.getValue().contains(contains)) {
                return stateSetEntry.getKey();
            }
        }
        return null;
    }

    private void merge(Map<State, Set<State>> setCollection, State set1Name, State set2Name, State newSetName) {
        Set<State> set1 = setCollection.get(set1Name);
        Set<State> set2 = setCollection.get(set2Name);

        setCollection.remove(set1Name);
        setCollection.remove(set2Name);

        set1.addAll(set2);
        setCollection.put(newSetName, set1);
    }

    private boolean isNfa(FiniteAutomaton fa) {
        for (State state : fa.getStates()) {
            Set<String> symbolsSeen = new HashSet<>();
            for (Transition transition : state.getTransitions()) {
                // has an epsilon transition
                if(transition.getTransitionSymbol().equals(NfaUtils.EPSILON_TRANSITION_SYMBOL)) {
                    return true;
                }
                // has multiple transitions with the same symbol
                if(symbolsSeen.contains(transition.getTransitionSymbol())) {
                    return true;
                }
                symbolsSeen.add(transition.getTransitionSymbol());
            }
        }
        return false;
    }

}
