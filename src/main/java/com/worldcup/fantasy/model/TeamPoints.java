package com.worldcup.fantasy.model;

import java.util.List;

/**
 * Fantasy points for one team in a match: every player plus the team total.
 */
public record TeamPoints(
        long teamId,
        String teamName,
        String teamLogo,
        int teamTotalPoints,
        List<PlayerPoints> players
) {
}
