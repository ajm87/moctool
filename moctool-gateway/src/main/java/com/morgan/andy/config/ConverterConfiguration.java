package com.morgan.andy.config;

import com.morgan.andy.moc.automata.NfaToDfaConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConverterConfiguration {
    @Bean
    public NfaToDfaConverter nfaToDfaConverter() {
        return new NfaToDfaConverter();
    }
}
