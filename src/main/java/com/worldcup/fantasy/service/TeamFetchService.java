package com.worldcup.fantasy.service;

import com.worldcup.fantasy.entity.FantasyTeam;
import com.worldcup.fantasy.model.SavedTeamDto;
import com.worldcup.fantasy.model.TeamPlayerDto;
import com.worldcup.fantasy.repository.FantasyTeamRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeamFetchService {

    private final FantasyTeamRepository teamRepository;

    public TeamFetchService(FantasyTeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Transactional(readOnly = true)
    public List<SavedTeamDto> getTeamsForMatch(Long matchId) {
        return teamRepository.findByMatchId(matchId).stream()
                .map(this::toDto)
                .toList();
    }

    private SavedTeamDto toDto(FantasyTeam team) {
        List<TeamPlayerDto> players = team.getPlayers().stream()
                .map(p -> new TeamPlayerDto(
                        p.getPlayerId(),
                        p.getPlayerName(),
                        p.getNumber(),
                        p.getPos(),
                        p.getGrid(),
                        Boolean.TRUE.equals(p.getIsCaptain()),
                        Boolean.TRUE.equals(p.getIsViceCaptain())
                ))
                .toList();

        return new SavedTeamDto(
                team.getTeamId(),
                team.getTeamName(),
                team.getEmail(),
                team.getMatchId(),
                players
        );
    }
}