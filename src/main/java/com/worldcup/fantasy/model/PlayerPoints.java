package com.worldcup.fantasy.model;

import java.util.Map;

/**
 * Fantasy points for a single player in a match.
 * breakdown holds the per-rule contribution, e.g. {"goals":40,"passes":30,"clean_sheet":25}
 */
public record PlayerPoints(
        long playerId,
        String playerName,
        String photo,
        String position,
        int minutes,
        boolean substitute,
        int totalPoints,
        Map<String, Integer> breakdown
) {
}
