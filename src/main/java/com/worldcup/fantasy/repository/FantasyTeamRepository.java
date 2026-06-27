package com.worldcup.fantasy.repository;

import com.worldcup.fantasy.entity.FantasyTeam;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FantasyTeamRepository extends JpaRepository<FantasyTeam, String> {

    // all teams submitted for a given match
    List<FantasyTeam> findByMatchId(Long matchId);
}