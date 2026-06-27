package com.worldcup.fantasy.model;

import java.util.List;

/**
 * Maps the incoming create-team payload.
 * We only read "parameters.fixture" and "response[0]" — the rest
 * (get, errors, paging) are accepted and ignored.
 */
public class CreateTeamRequest {

    private Parameters parameters;
    private List<ResponseItem> response;

    public Parameters getParameters() { return parameters; }
    public void setParameters(Parameters parameters) { this.parameters = parameters; }
    public List<ResponseItem> getResponse() { return response; }
    public void setResponse(List<ResponseItem> response) { this.response = response; }

    public static class Parameters {
        private String fixture;
        public String getFixture() { return fixture; }
        public void setFixture(String fixture) { this.fixture = fixture; }
    }

    public static class ResponseItem {
        private TeamInfo team;
        private List<StartXIItem> startXI;
        public TeamInfo getTeam() { return team; }
        public void setTeam(TeamInfo team) { this.team = team; }
        public List<StartXIItem> getStartXI() { return startXI; }
        public void setStartXI(List<StartXIItem> startXI) { this.startXI = startXI; }
    }

    public static class TeamInfo {
        private String id;       // null from frontend; we generate it
        private String name;
        private String email;
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class StartXIItem {
        private PlayerInfo player;
        public PlayerInfo getPlayer() { return player; }
        public void setPlayer(PlayerInfo player) { this.player = player; }
    }

    public static class PlayerInfo {
        private Long id;
        private String name;
        private Integer number;
        private String pos;
        private String grid;
        private String captain;       // "true" / "false" as strings
        private String viceCaptain;
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getNumber() { return number; }
        public void setNumber(Integer number) { this.number = number; }
        public String getPos() { return pos; }
        public void setPos(String pos) { this.pos = pos; }
        public String getGrid() { return grid; }
        public void setGrid(String grid) { this.grid = grid; }
        public String getCaptain() { return captain; }
        public void setCaptain(String captain) { this.captain = captain; }
        public String getViceCaptain() { return viceCaptain; }
        public void setViceCaptain(String viceCaptain) { this.viceCaptain = viceCaptain; }
    }
}