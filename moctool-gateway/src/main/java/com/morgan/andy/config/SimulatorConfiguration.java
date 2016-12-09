package com.morgan.andy.config;

import com.morgan.andy.moc.simulation.DFASimulator;
import com.morgan.andy.moc.simulation.NFASimulator;
import com.morgan.andy.moc.simulation.SimulatedAutomatonStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class SimulatorConfiguration {

    @Bean
    public DFASimulator dfaSimulator() {
        return new DFASimulator();
    }

    @Bean
    public SimulatedAutomatonStore simulatedAutomatonStore() {
        return new SimulatedAutomatonStore();
    }

    @Bean
    public NFASimulator nfaSimulator(){ return new NFASimulator(); }

}
