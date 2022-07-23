package com.xapaya.xapaporra.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@AllArgsConstructor
@Slf4j
public class WebClientConfig {

    private final AppProperties properties;

    @Bean
    WebClient webClientFootballDataOrg() {
        return WebClient.builder()
                .baseUrl(properties.getFootballData().getBaseUrl())
                .defaultHeader("X-Auth-Token", properties.getFootballData().getApiToken())
                .build();
    }
}
