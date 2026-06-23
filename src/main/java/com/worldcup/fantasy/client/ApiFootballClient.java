package com.worldcup.fantasy.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ApiFootballClient {

    private final RestClient client;

    public ApiFootballClient(RestClient apiFootballRestClient) {
        this.client = apiFootballRestClient;
    }

    public JsonNode getPlayerStats(long matchId) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/fixtures/players")
                        .queryParam("fixture", matchId)
                        .build())
                .retrieve()
                .body(JsonNode.class);
    }
}