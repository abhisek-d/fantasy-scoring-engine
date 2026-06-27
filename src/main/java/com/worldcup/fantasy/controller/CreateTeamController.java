package com.worldcup.fantasy.controller;

import com.worldcup.fantasy.model.CreateTeamRequest;
import com.worldcup.fantasy.service.TeamSaveService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Saves a user-submitted fantasy team.
 *
 * POST /create-team?matchId=1489404
 * body = the custom/lineup payload
 *
 * Returns the generated team id.
 */
@RestController
@CrossOrigin(origins = "*")
public class CreateTeamController {

    private final TeamSaveService teamSaveService;

    public CreateTeamController(TeamSaveService teamSaveService) {
        this.teamSaveService = teamSaveService;
    }

    @PostMapping("/create-team")
    public ResponseEntity<?> createTeam(@RequestParam Long matchId,
                                        @RequestBody CreateTeamRequest request) {
        try {
            String teamId = teamSaveService.saveTeam(matchId, request);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "teamId", teamId,
                    "message", "Team saved successfully"
            ));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", ex.getMessage()
            ));
        }
    }
}