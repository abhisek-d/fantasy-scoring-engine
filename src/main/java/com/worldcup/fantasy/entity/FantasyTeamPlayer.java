package com.worldcup.fantasy.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "fantasy_team_player", schema = "fantasy_league")
public class FantasyTeamPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "player_id")
    private Long playerId;

    @Column(name = "player_name")
    private String playerName;

    @Column(name = "number")
    private Integer number;

    @Column(name = "pos")
    private String pos;

    @Column(name = "grid")
    private String grid;

    @Column(name = "is_captain")
    private Boolean isCaptain;

    @Column(name = "is_vice_captain")
    private Boolean isViceCaptain;

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTeamId() { return teamId; }
    public void setTeamId(String teamId) { this.teamId = teamId; }
    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public Integer getNumber() { return number; }
    public void setNumber(Integer number) { this.number = number; }
    public String getPos() { return pos; }
    public void setPos(String pos) { this.pos = pos; }
    public String getGrid() { return grid; }
    public void setGrid(String grid) { this.grid = grid; }
    public Boolean getIsCaptain() { return isCaptain; }
    public void setIsCaptain(Boolean isCaptain) { this.isCaptain = isCaptain; }
    public Boolean getIsViceCaptain() { return isViceCaptain; }
    public void setIsViceCaptain(Boolean isViceCaptain) { this.isViceCaptain = isViceCaptain; }
}