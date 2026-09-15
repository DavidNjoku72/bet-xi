# Bet XI

Bet XI is a football prediction application.

A user chooses a Premier League gameweek and creates a starting XI
using players from matches taking place during that gameweek.

For each player, the user predicts whether they will go over or under
a particular statistical line.

Example:
David Raya - Over 3.5 saves
Declan Rice - Over 2.5 tackles
Bukayo Saka - Over 2.5 shots

V1 Requirements

- A gameweek contains multiple matches.
- Matches contain two teams.
- Teams have players.
- A user can build one XI for a gameweek.
- An XI contains 11 different players.
- V1 uses a 4-3-3 formation.
- Each player has one statistical prediction.
- Predictions can be OVER or UNDER.
- The lineup can be saved.
- Actual player statistics can be entered after matches.
- The application marks predictions as correct or incorrect.
- The application gives the XI a score out of 11.

## Data Design

### Gameweek
- id
- gameweekNumber
- startDate
- endDate

### Match
- id
- homeTeam
- awayTeam
- kickoffTime
- gameweek

### Team
- id
- name

### Player
- id
- name
- position
- team

### Lineup
- id
- gameweek
- picks
- score

### Pick
- id
- player
- statistic
- line
- prediction
- actualValue
- result