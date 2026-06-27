package com.worldcup.fantasy.repository;

import com.worldcup.fantasy.entity.FantasyTeamPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FantasyTeamPlayerRepository extends JpaRepository<FantasyTeamPlayer, Long> {
}