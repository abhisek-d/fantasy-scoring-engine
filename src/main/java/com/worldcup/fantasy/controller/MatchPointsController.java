package com.worldcup.fantasy.controller;

import com.worldcup.fantasy.model.MatchPointsResponse;
import com.worldcup.fantasy.service.MatchPointsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The frontend sends a matchId; we return fantasy points for every player on
 * both teams plus each team's total.
 *
 * GET /match-points?matchId=1489391
 */
@RestController
@CrossOrigin(origins = "*")
public class MatchPointsController {

    private final MatchPointsService matchPointsService;

    public MatchPointsController(MatchPointsService matchPointsService) {
        this.matchPointsService = matchPointsService;
    }

    @GetMapping("/match-points")
    public MatchPointsResponse getMatchPoints(@RequestParam long matchId) {
        return matchPointsService.computeMatchPoints(matchId);
    }
}
