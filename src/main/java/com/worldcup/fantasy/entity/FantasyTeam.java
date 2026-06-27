package com.worldcup.fantasy.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fantasy_team", schema = "fantasy_league")
public class FantasyTeam {

    @Id
    @Column(name = "team_id")
    private String teamId;

    @Column(name = "match_id")
    private Long matchId;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "email")
    private String email;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "teamId", fetch = FetchType.EAGER)
    @OrderBy("grid ASC")
    private List<FantasyTeamPlayer> players = new ArrayList<>();

    // getters and setters
    public String getTeamId() { return teamId; }
    public void setTeamId(String teamId) { this.teamId = teamId; }
    public Long getMatchId() { return matchId; }
    public void setMatchId(Long matchId) { this.matchId = matchId; }
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public List<FantasyTeamPlayer> getPlayers() { return players; }
    public void setPlayers(List<FantasyTeamPlayer> players) { this.players = players; }
}