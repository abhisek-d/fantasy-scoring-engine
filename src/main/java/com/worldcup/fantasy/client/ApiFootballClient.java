package com.worldcup.fantasy.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Thin wrapper over the API-Football fixtures/players endpoint.
 * Returns the raw JsonNode so the scoring service can read fields defensively
 * (the API returns many stat fields as null).
 */
@Component
public class ApiFootballClient {

    private final RestClient client;

    public ApiFootballClient(RestClient apiFootballRestClient) {
        this.client = apiFootballRestClient;
    }

    /**
     * GET /fixtures/players?fixture={matchId}
     * Returns the full response body as a JsonNode.
     */
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
