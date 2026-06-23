package com.worldcup.fantasy.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.worldcup.fantasy.model.PlayerPoints;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Encapsulates the World Cup Fantasy Live v1 scoring rules.
 *
 * All rules operate on the "statistics[0]" block of a single player from the
 * API-Football fixtures/players response. Every numeric field can be null in
 * the API, so num() coerces null -> 0.
 */
@Service
public class ScoringService {

    /* ---- Participation ---- */
    private static final int STARTED = 4;            // appeared in the match
    private static final int PLAYED_60 = 10;         // 60+ minutes
    private static final int PLAYED_FULL = 5;        // 90+ minutes

    /* ---- Attack ---- */
    private static final int GOAL = 40;
    private static final int ASSIST = 30;
    private static final int SHOT_ON_TARGET = 8;

    /* ---- Defensive ---- */
    private static final int TACKLE_WON = 4;
    private static final int INTERCEPTION = 4;
    private static final int BLOCK = 5;

    /* ---- Goalkeeper ---- */
    private static final int SAVE = 5;
    private static final int PENALTY_SAVE = 30;
    private static final int CLEAN_SHEET = 25;

    /* ---- Negative ---- */
    private static final int YELLOW_CARD = -5;
    private static final int RED_CARD = -15;
    private static final int OWN_GOAL = -20;
    private static final int PENALTY_MISSED = -20;

    /**
     * Compute fantasy points for one player.
     *
     * @param playerNode the element of team.players[] (has "player" and "statistics")
     */
    public PlayerPoints scorePlayer(JsonNode playerNode) {
        JsonNode player = playerNode.path("player");
        JsonNode stats = playerNode.path("statistics").path(0);

        JsonNode games = stats.path("games");
        int minutes = num(games.path("minutes"));
        String position = games.path("position").asText("");
        boolean substitute = games.path("substitute").asBoolean(false);

        Map<String, Integer> breakdown = new LinkedHashMap<>();
        int total = 0;

        /* ---- Participation ----
           Substitute logic: if the player entered (minutes > 0) he scores
           normally. If he never came on (minutes == 0) he gets nothing. */
        if (minutes > 0) {
            total += add(breakdown, "started", STARTED);
        }
        if (minutes >= 60) {
            total += add(breakdown, "played60", PLAYED_60);
        }
        if (minutes >= 90) {
            total += add(breakdown, "fullMatch", PLAYED_FULL);
        }

        /* ---- Attack ---- */
        int goals = num(stats.path("goals").path("total"));
        if (goals > 0) total += add(breakdown, "goals", GOAL * goals);

        int assists = num(stats.path("goals").path("assists"));
        if (assists > 0) total += add(breakdown, "assists", ASSIST * assists);

        int shotsOn = num(stats.path("shots").path("on"));
        if (shotsOn > 0) total += add(breakdown, "shotsOnTarget", SHOT_ON_TARGET * shotsOn);

        /* ---- Passing (NOT cumulative, highest band only) ---- */
        int passes = num(stats.path("passes").path("total"));
        int passBonus = passBand(passes);
        if (passBonus > 0) total += add(breakdown, "passes", passBonus);

        /* ---- Defensive ---- */
        int tackles = num(stats.path("tackles").path("total"));
        if (tackles > 0) total += add(breakdown, "tackles", TACKLE_WON * tackles);

        int interceptions = num(stats.path("tackles").path("interceptions"));
        if (interceptions > 0) total += add(breakdown, "interceptions", INTERCEPTION * interceptions);

        int blocks = num(stats.path("tackles").path("blocks"));
        if (blocks > 0) total += add(breakdown, "blocks", BLOCK * blocks);

        /* ---- Goalkeeper ---- */
        int saves = num(stats.path("goals").path("saves"));
        if (saves > 0) total += add(breakdown, "saves", SAVE * saves);

        int penSaved = num(stats.path("penalty").path("saved"));
        if (penSaved > 0) total += add(breakdown, "penaltySaved", PENALTY_SAVE * penSaved);

        /* Clean sheet: only goalkeepers and defenders who played a meaningful
           part (60+ minutes) and conceded zero. */
        int conceded = num(stats.path("goals").path("conceded"));
        if (("G".equals(position) || "D".equals(position)) && minutes >= 60 && conceded == 0) {
            total += add(breakdown, "cleanSheet", CLEAN_SHEET);
        }

        /* ---- Negative events ---- */
        int yellow = num(stats.path("cards").path("yellow"));
        if (yellow > 0) total += add(breakdown, "yellowCard", YELLOW_CARD * yellow);

        int red = num(stats.path("cards").path("red"));
        if (red > 0) total += add(breakdown, "redCard", RED_CARD * red);

        int penMissed = num(stats.path("penalty").path("missed"));
        if (penMissed > 0) total += add(breakdown, "penaltyMissed", PENALTY_MISSED * penMissed);

        /* Own goals are not in fixtures/players. They live in fixtures/events
           with detail "Own Goal". Hook left here for when events are wired in. */

        return new PlayerPoints(
                player.path("id").asLong(),
                player.path("name").asText(""),
                player.path("photo").asText(""),
                position,
                minutes,
                substitute,
                total,
                breakdown
        );
    }

    /** Passing bands are NOT cumulative — return the single highest band. */
    private int passBand(int passes) {
        if (passes >= 80) return 40;
        if (passes >= 60) return 30;
        if (passes >= 40) return 20;
        if (passes >= 20) return 10;
        return 0;
    }

    /** Records a non-zero contribution in the breakdown and returns it for summing. */
    private int add(Map<String, Integer> breakdown, String key, int value) {
        breakdown.put(key, value);
        return value;
    }

    /** Coerce a possibly-null / missing JSON numeric to int (0 when absent). */
    private int num(JsonNode node) {
        return (node == null || node.isNull() || node.isMissingNode()) ? 0 : node.asInt(0);
    }
}
