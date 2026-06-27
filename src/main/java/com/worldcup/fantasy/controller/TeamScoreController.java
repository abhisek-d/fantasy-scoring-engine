package com.worldcup.fantasy.controller;

import com.worldcup.fantasy.model.TeamScoreResponse;
import com.worldcup.fantasy.service.TeamScoreService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Scores a saved custom team for a match.
 *
 * GET /team-score?teamId=MyTeam21988&matchId=1489413
 *
 * Returns the team total plus a per-player breakdown with captain/vice
 * multipliers applied.
 */
@RestController
@CrossOrigin(origins = "*")
public class TeamScoreController {

    private final TeamScoreService teamScoreService;

    public TeamScoreController(TeamScoreService teamScoreService) {
        this.teamScoreService = teamScoreService;
    }

    @GetMapping("/team-score")
    public ResponseEntity<?> getTeamScore(@RequestParam String teamId,
                                          @RequestParam Long matchId) {
        try {
            TeamScoreResponse result = teamScoreService.scoreTeam(teamId, matchId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", ex.getMessage()
            ));
        }
    }
}