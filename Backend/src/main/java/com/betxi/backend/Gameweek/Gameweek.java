package com.betxi.backend.Gameweek;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Gameweek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int gameweekNumber;

    private LocalDate startDate;

    private LocalDate endDate;

    public Gameweek() {
    }

    public Gameweek(int gameweekNumber, LocalDate startDate, LocalDate endDate) {
        this.gameweekNumber = gameweekNumber;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public int getGameweekNumber() {
        return gameweekNumber;
    }

    public void setGameweekNumber(int gameweekNumber) {
        this.gameweekNumber = gameweekNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}