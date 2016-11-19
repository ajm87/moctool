package com.morgan.andy.moc.automata;

import com.morgan.andy.domain.FiniteAutomaton;
import com.morgan.andy.moc.Converter;
import com.morgan.andy.web.rest.vm.ModelVM;

/**
 * Convert NFA to DFA
 */
public class NfaToDfaConverter implements Converter {

    @Override
    public FiniteAutomaton convert(FiniteAutomaton nfa) {
        ModelVM converted = new ModelVM();
        return nfa;
    }

}
