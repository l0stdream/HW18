package org.javapro.hw18.config;

import org.javapro.hw18.dto.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {
    @Bean
    public Map<Integer, Order> orderMap() {
        return new HashMap<>();
    }
}