package com.morgan.andy.config.metrics;

import com.morgan.andy.service.ModelService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfiguration {

    @Bean
    public ModelService modelService() {
        return new ModelService();
    }
}
