package com.morgan.andy.moc.automata;
public interface Converter<T, Q> {

    Q convert(T automaton);

}
