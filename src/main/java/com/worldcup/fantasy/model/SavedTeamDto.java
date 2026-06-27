package com.worldcup.fantasy.model;

import java.util.List;

public record SavedTeamDto(
        String teamId,
        String teamName,
        String email,
        Long matchId,
        List<TeamPlayerDto> players
) {
}