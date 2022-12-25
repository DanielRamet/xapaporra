package com.xapaya.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@AllArgsConstructor
@Slf4j
public class WebClientConfig {

    private final AppProperties properties;

    @Bean
    WebClient webClientFootballDataOrg() {
        final int size = 10 * 1024 * 1024;
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();
        return WebClient.builder()
                .baseUrl(properties.getFootballData().getBaseUrl())
                .defaultHeader("X-Auth-Token", properties.getFootballData().getApiToken())
                .exchangeStrategies(strategies)
                .build();
    }

    @Bean
    WebClient webClientQuinicheck() {
        final int size = 10 * 1024 * 1024;
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();
        return WebClient.builder()
                .baseUrl(properties.getQuinicheck().getBaseUrl())
                .exchangeStrategies(strategies)
                .build();
    }
}
