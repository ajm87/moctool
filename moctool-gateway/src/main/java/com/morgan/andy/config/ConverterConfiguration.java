package com.morgan.andy.config;

import com.morgan.andy.moc.automata.NfaToDfaConverter;
import com.morgan.andy.moc.automata.REToNfaConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConverterConfiguration {
    @Bean
    public NfaToDfaConverter nfaToDfaConverter() {
        return new NfaToDfaConverter();
    }

    @Bean
    public REToNfaConverter reToNfaConverter() {
        return new REToNfaConverter();
    }
}
