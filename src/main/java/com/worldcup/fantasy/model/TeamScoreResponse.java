package com.worldcup.fantasy.model;

import java.util.List;

/**
 * Result of scoring one custom team for a match.
 * Each player shows base points and final points (after captain/vice multiplier).
 */
public record TeamScoreResponse(
        String teamId,
        String teamName,
        Long matchId,
        int totalPoints,
        List<ScoredPlayer> players
) {
    public record ScoredPlayer(
            Long playerId,
            String playerName,
            int basePoints,
            double multiplier,     // 2.0 captain, 1.5 vice, 1.0 normal
            double finalPoints,
            boolean captain,
            boolean viceCaptain
    ) {
    }
}