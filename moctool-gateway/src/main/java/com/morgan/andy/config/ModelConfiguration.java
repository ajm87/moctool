package com.morgan.andy.config;

import com.morgan.andy.service.ModelService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfiguration {

    @Bean
    public ModelService getModelService() {
        return new ModelService();
    }

}
