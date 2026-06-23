package com.worldcup.fantasy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${apifootball.base-url}")
    private String baseUrl;

    @Value("${apifootball.api-key}")
    private String apiKey;

    @Bean
    public RestClient apiFootballRestClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("x-apisports-key", apiKey)
                .build();
    }
}
