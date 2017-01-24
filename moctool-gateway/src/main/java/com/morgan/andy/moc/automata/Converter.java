package com.morgan.andy.moc.automata;
import com.morgan.andy.domain.FiniteAutomaton;

public interface Converter<T> {

    FiniteAutomaton convert(T automaton);

}
