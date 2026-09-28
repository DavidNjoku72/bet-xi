package com.betxi.backend.Lineup;

import com.betxi.backend.Formation.Formation;
import com.betxi.backend.Gameweek.Gameweek;
import com.betxi.backend.Pick.Pick;
import jakarta.persistence.*;


import java.util.ArrayList;
import java.util.List;

@Entity
public class Lineup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Formation formation;

    @ManyToOne
    private Gameweek gameweek;

    @OneToMany
    private List<Pick> picks = new ArrayList<>();

    private int score = 0;

    public Lineup() {
    }

    public Lineup(Gameweek gameweek, Formation formation) {
    this.gameweek = gameweek;
    this.formation = formation;
    }

    public Long getId() {
        return id;
    }

    public Formation getFormation() {
    return formation;
    }

    public Gameweek getGameweek() {
        return gameweek;
    }

    public List<Pick> getPicks() {
        return picks;
    }

    public int getScore() {
        return score;
    }

   public void addPick(Pick pick) {

    if (picks.size() >= 11) {
        throw new RuntimeException("A lineup cannot have more than 11 players");
    }

    // Stop the same player being selected twice
    for (Pick existingPick : picks) {
        if (existingPick.getPlayer().getId().equals(pick.getPlayer().getId())) {
            throw new RuntimeException("This player is already in the lineup");
        }
    }

    String position = pick.getPlayer().getPosition();

    int positionCount = 0;

    // Count how many players of this position are already selected
    for (Pick existingPick : picks) {
        if (existingPick.getPlayer().getPosition().equals(position)) {
            positionCount++;
        }
    }

    // Get the allowed amount from the selected formation
    int positionLimit;

    switch (position) {
        case "GK":
            positionLimit = formation.getGoalkeepers();
            break;

        case "DEF":
            positionLimit = formation.getDefenders();
            break;

        case "MID":
            positionLimit = formation.getMidfielders();
            break;

        case "FWD":
            positionLimit = formation.getForwards();
            break;

        default:
            throw new RuntimeException("Invalid player position: " + position);
    }

    if (positionCount >= positionLimit) {
        throw new RuntimeException(
                "Formation " + formation.getName()
                + " only allows " + positionLimit
                + " " + position + " players"
        );
    }

    picks.add(pick);

    calculateScore();
}

public void calculateScore() {
    int newScore = 0;

    for (Pick pick : picks) {
        if ("CORRECT".equals(pick.getResult())) {
            newScore++;
        }
    }

    this.score = newScore;
}

    public void setScore(int score) {
        this.score = score;
    }
}