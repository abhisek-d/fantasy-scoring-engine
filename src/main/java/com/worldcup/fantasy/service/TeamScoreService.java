package com.worldcup.fantasy.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.worldcup.fantasy.client.ApiFootballClient;
import com.worldcup.fantasy.entity.FantasyTeam;
import com.worldcup.fantasy.entity.FantasyTeamPlayer;
import com.worldcup.fantasy.model.PlayerPoints;
import com.worldcup.fantasy.model.TeamScoreResponse;
import com.worldcup.fantasy.repository.FantasyTeamRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Scores a saved custom team for a match:
 *  1. load the team (11 players) from DB
 *  2. fetch the match player-stats from API-Football
 *  3. score each picked player with ScoringService (base points)
 *  4. apply captain x2.0 / vice-captain x1.5
 *  5. sum into the team total
 */
@Service
public class TeamScoreService {

    private static final double CAPTAIN_MULTIPLIER = 2.0;
    private static final double VICE_CAPTAIN_MULTIPLIER = 1.5;

    private final FantasyTeamRepository teamRepository;
    private final ApiFootballClient apiFootballClient;
    private final ScoringService scoringService;

    public TeamScoreService(FantasyTeamRepository teamRepository,
                            ApiFootballClient apiFootballClient,
                            ScoringService scoringService) {
        this.teamRepository = teamRepository;
        this.apiFootballClient = apiFootballClient;
        this.scoringService = scoringService;
    }

    @Transactional(readOnly = true)
    public TeamScoreResponse scoreTeam(String teamId, Long matchId) {
        // 1. load the saved team
        FantasyTeam team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + teamId));

        // 2. fetch match stats and index base points by playerId
        JsonNode root = apiFootballClient.getPlayerStats(matchId);
        Map<Long, PlayerPoints> basePointsByPlayer = buildBasePointsMap(root);

        // 3. score each picked player, applying captain/vice multipliers
        List<TeamScoreResponse.ScoredPlayer> scored = new ArrayList<>();
        double teamTotal = 0;

        for (FantasyTeamPlayer pick : team.getPlayers()) {
            PlayerPoints bp = basePointsByPlayer.get(pick.getPlayerId());
            int basePoints = (bp != null) ? bp.totalPoints() : 0;

            double multiplier = 1.0;
            boolean isCaptain = Boolean.TRUE.equals(pick.getIsCaptain());
            boolean isVice = Boolean.TRUE.equals(pick.getIsViceCaptain());
            if (isCaptain) {
                multiplier = CAPTAIN_MULTIPLIER;
            } else if (isVice) {
                multiplier = VICE_CAPTAIN_MULTIPLIER;
            }

            double finalPoints = basePoints * multiplier;
            teamTotal += finalPoints;

            scored.add(new TeamScoreResponse.ScoredPlayer(
                    pick.getPlayerId(),
                    pick.getPlayerName(),
                    basePoints,
                    multiplier,
                    finalPoints,
                    isCaptain,
                    isVice
            ));
        }

        return new TeamScoreResponse(
                team.getTeamId(),
                team.getTeamName(),
                matchId,
                (int) Math.round(teamTotal),
                scored
        );
    }

    /** Score every player in the match once, keyed by playerId. */
    private Map<Long, PlayerPoints> buildBasePointsMap(JsonNode root) {
        Map<Long, PlayerPoints> map = new HashMap<>();
        JsonNode response = root.path("response");
        for (JsonNode teamNode : response) {
            for (JsonNode playerNode : teamNode.path("players")) {
                PlayerPoints pp = scoringService.scorePlayer(playerNode);
                map.put(pp.playerId(), pp);
            }
        }
        return map;
    }
}