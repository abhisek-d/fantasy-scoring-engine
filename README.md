# World Cup Fantasy Live — Match Scoring Service

A small Spring Boot (Java 21) service that computes fantasy points for every
player in a World Cup match. The frontend sends a match id; the service fetches
that match's player stats from API-Football, applies the v1 scoring rules, and
returns per-player points plus each team's total.

## Endpoint

```
GET /match-points?matchId=1489391
```

Response shape:

```json
{
  "matchId": 1489391,
  "teams": [
    {
      "teamId": 2384,
      "teamName": "USA",
      "teamLogo": "https://media.api-sports.io/football/teams/2384.png",
      "teamTotalPoints": 715,
      "players": [
        {
          "playerId": 355994,
          "playerName": "Alexander Freeman",
          "photo": "https://media.api-sports.io/football/players/355994.png",
          "position": "D",
          "minutes": 90,
          "substitute": false,
          "totalPoints": 146,
          "breakdown": {
            "started": 4, "played60": 10, "fullMatch": 5,
            "goals": 40, "shotsOnTarget": 8, "passes": 30,
            "tackles": 16, "interceptions": 8, "cleanSheet": 25
          }
        }
      ]
    }
  ]
}
```

See `sample-response.json` for the full output computed from the sample
`InMatchStat.json` (fixture 1489391, USA vs Australia).

## Run locally

```bash
export API_FOOTBALL_KEY=your_api_football_key
mvn spring-boot:run
```

Then:

```bash
curl "http://localhost:8080/match-points?matchId=1489391"
```

If you don't set `API_FOOTBALL_KEY`, a default key from the sample is used —
replace it for production.

## Scoring rules (v1)

| Category | Rule | Points |
|---|---|---|
| Participation | Started (any minutes) | +4 |
| | Played 60+ minutes | +10 |
| | Played 90+ minutes | +5 |
| Attack | Goal | +40 |
| | Assist | +30 |
| | Shot on target | +8 |
| Passing (highest band only, NOT cumulative) | 20+ passes | +10 |
| | 40+ passes | +20 |
| | 60+ passes | +30 |
| | 80+ passes | +40 |
| Defensive | Tackle won | +4 |
| | Interception | +4 |
| | Block | +5 |
| Goalkeeper | Save | +5 |
| | Penalty save | +30 |
| | Clean sheet (G/D, 60+ min, conceded 0) | +25 |
| Negative | Yellow card | −5 |
| | Red card | −15 |
| | Penalty missed | −20 |
| | Own goal | −20 |

**Substitute logic:** a substitute who comes on (minutes > 0) scores normally.
A player who never enters (minutes == 0) scores 0.

## Notes & limitations

- **Big Chance Created (+10)** and **Clearance (+2)** from the spec are NOT in
  the API-Football `fixtures/players` response, so they are not scored in v1.
  They would need a higher data tier.
- **Own Goal (−20)** is in `fixtures/events` (detail "Own Goal"), not in
  `fixtures/players`. The hook is marked in `ScoringService`; wire in the events
  endpoint to apply it.
- This service computes **base** points (no captain/vice-captain multiplier).
  Apply the 2.0x / 1.5x multipliers per fantasy team when you roll up a user's
  contest score.

## Project structure

```
controller/MatchPointsController   GET /match-points
service/MatchPointsService         fetch -> score -> roll up team totals
service/ScoringService             all the scoring rules
client/ApiFootballClient           calls fixtures/players
config/RestClientConfig            RestClient with x-apisports-key header
model/                             PlayerPoints, TeamPoints, MatchPointsResponse
```
