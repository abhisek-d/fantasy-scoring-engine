package com.worldcup.fantasy.model;

import java.util.List;

/**
 * Top-level response for GET /match-points?matchId=...
 */
public record MatchPointsResponse(
        long matchId,
        List<TeamPoints> teams
) {
}
