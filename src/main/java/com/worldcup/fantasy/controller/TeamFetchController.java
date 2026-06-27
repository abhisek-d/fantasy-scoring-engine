package com.worldcup.fantasy.controller;

import com.worldcup.fantasy.model.SavedTeamDto;
import com.worldcup.fantasy.service.TeamFetchService;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The frontend sends a matchId; we return every team submitted for that match,
 * each with its 11 players.
 *
 * GET /teams?matchId=1489417
 */
@RestController
@CrossOrigin(origins = "*")
public class TeamFetchController {

    private final TeamFetchService teamFetchService;

    public TeamFetchController(TeamFetchService teamFetchService) {
        this.teamFetchService = teamFetchService;
    }

    @GetMapping("/teams")
    public List<SavedTeamDto> getTeams(@RequestParam Long matchId) {
        return teamFetchService.getTeamsForMatch(matchId);
    }
}