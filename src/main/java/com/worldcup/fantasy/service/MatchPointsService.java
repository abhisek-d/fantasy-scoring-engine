package com.worldcup.fantasy.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.worldcup.fantasy.client.ApiFootballClient;
import com.worldcup.fantasy.model.MatchPointsResponse;
import com.worldcup.fantasy.model.PlayerPoints;
import com.worldcup.fantasy.model.TeamPoints;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Orchestrates: fetch raw player stats for a match -> score every player ->
 * roll up per-team totals -> assemble the response.
 */
@Service
public class MatchPointsService {

    private final ApiFootballClient client;
    private final ScoringService scoringService;

    public MatchPointsService(ApiFootballClient client, ScoringService scoringService) {
        this.client = client;
        this.scoringService = scoringService;
    }

    public MatchPointsResponse computeMatchPoints(long matchId) {
        JsonNode root = client.getPlayerStats(matchId);
        JsonNode response = root.path("response");

        List<TeamPoints> teams = new ArrayList<>();

        for (JsonNode teamNode : response) {
            JsonNode team = teamNode.path("team");
            List<PlayerPoints> players = new ArrayList<>();
            int teamTotal = 0;

            for (JsonNode playerNode : teamNode.path("players")) {
                PlayerPoints pp = scoringService.scorePlayer(playerNode);
                players.add(pp);
                teamTotal += pp.totalPoints();
            }

            teams.add(new TeamPoints(
                    team.path("id").asLong(),
                    team.path("name").asText(""),
                    team.path("logo").asText(""),
                    teamTotal,
                    players
            ));
        }

        return new MatchPointsResponse(matchId, teams);
    }
}
