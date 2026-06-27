package com.worldcup.fantasy.service;

import com.worldcup.fantasy.entity.FantasyTeam;
import com.worldcup.fantasy.entity.FantasyTeamPlayer;
import com.worldcup.fantasy.model.CreateTeamRequest;
import com.worldcup.fantasy.repository.FantasyTeamPlayerRepository;
import com.worldcup.fantasy.repository.FantasyTeamRepository;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeamSaveService {

    private final FantasyTeamRepository teamRepository;
    private final FantasyTeamPlayerRepository playerRepository;

    public TeamSaveService(FantasyTeamRepository teamRepository,
                           FantasyTeamPlayerRepository playerRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional
    public String saveTeam(Long matchId, CreateTeamRequest request) {
        if (request.getResponse() == null || request.getResponse().isEmpty()) {
            throw new IllegalArgumentException("response is empty");
        }
        CreateTeamRequest.ResponseItem item = request.getResponse().get(0);
        CreateTeamRequest.TeamInfo teamInfo = item.getTeam();

        if (teamInfo == null || teamInfo.getName() == null || teamInfo.getName().isBlank()) {
            throw new IllegalArgumentException("team name is required");
        }
        if (item.getStartXI() == null || item.getStartXI().size() != 11) {
            throw new IllegalArgumentException("startXI must contain exactly 11 players");
        }

        // Generate team id: name + random 5-digit number
        String teamId = generateTeamId(teamInfo.getName());

        // Save the parent team row
        FantasyTeam team = new FantasyTeam();
        team.setTeamId(teamId);
        team.setMatchId(matchId);
        team.setTeamName(teamInfo.getName());
        team.setEmail(teamInfo.getEmail());
        teamRepository.save(team);

        // Save the 11 player rows
        for (CreateTeamRequest.StartXIItem xi : item.getStartXI()) {
            CreateTeamRequest.PlayerInfo p = xi.getPlayer();
            FantasyTeamPlayer row = new FantasyTeamPlayer();
            row.setTeamId(teamId);
            row.setPlayerId(p.getId());
            row.setPlayerName(p.getName());
            row.setNumber(p.getNumber());
            row.setPos(p.getPos());
            row.setGrid(p.getGrid());
            row.setIsCaptain("true".equalsIgnoreCase(p.getCaptain()));
            row.setIsViceCaptain("true".equalsIgnoreCase(p.getViceCaptain()));
            playerRepository.save(row);
        }

        return teamId;
    }

    /** name + random 5-digit number, e.g. "Abteam00001" -> "Abteam54321" */
    private String generateTeamId(String name) {
        int random = ThreadLocalRandom.current().nextInt(10000, 100000); // 5 digits
        // strip spaces from the name so the id is clean
        String cleanName = name.replaceAll("\\s+", "");
        return cleanName + random;
    }
}