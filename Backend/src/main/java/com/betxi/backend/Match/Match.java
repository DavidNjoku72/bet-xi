package com.betxi.backend.Match;

import com.betxi.backend.Gameweek.Gameweek;
import com.betxi.backend.Team.Team;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Team homeTeam;

    @ManyToOne
    private Team awayTeam;

    private LocalDateTime kickoffTime;

    @ManyToOne
    private Gameweek gameweek;

    public Match() {
    }

    public Match(Team homeTeam, Team awayTeam, LocalDateTime kickoffTime, Gameweek gameweek) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.kickoffTime = kickoffTime;
        this.gameweek = gameweek;
    }

    public Long getId() {
        return id;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public LocalDateTime getKickoffTime() {
        return kickoffTime;
    }

    public void setKickoffTime(LocalDateTime kickoffTime) {
        this.kickoffTime = kickoffTime;
    }

    public Gameweek getGameweek() {
        return gameweek;
    }

    public void setGameweek(Gameweek gameweek) {
        this.gameweek = gameweek;
    }
}