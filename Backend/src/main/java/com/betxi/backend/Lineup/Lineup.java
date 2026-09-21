package com.betxi.backend.Lineup;

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
    private Gameweek gameweek;

    @OneToMany
    private List<Pick> picks = new ArrayList<>();

    private int score = 0;

    public Lineup() {
    }

    public Lineup(Gameweek gameweek) {
        this.gameweek = gameweek;
    }

    public Long getId() {
        return id;
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
        picks.add(pick);
    }

    public void setScore(int score) {
        this.score = score;
    }
}