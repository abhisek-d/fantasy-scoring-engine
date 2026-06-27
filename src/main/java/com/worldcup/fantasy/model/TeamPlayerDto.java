package com.worldcup.fantasy.model;

public record TeamPlayerDto(
        Long playerId,
        String playerName,
        Integer number,
        String pos,
        String grid,
        boolean captain,
        boolean viceCaptain
) {
}