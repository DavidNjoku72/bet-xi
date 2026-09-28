package com.betxi.backend.Formation;

import jakarta.persistence.*;

@Entity
public class Formation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int goalkeepers;
    private int defenders;
    private int midfielders;
    private int forwards;

    public Formation() {
    }

    public Formation(
            String name,
            int goalkeepers,
            int defenders,
            int midfielders,
            int forwards) {

        this.name = name;
        this.goalkeepers = goalkeepers;
        this.defenders = defenders;
        this.midfielders = midfielders;
        this.forwards = forwards;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getGoalkeepers() {
        return goalkeepers;
    }

    public int getDefenders() {
        return defenders;
    }

    public int getMidfielders() {
        return midfielders;
    }

    public int getForwards() {
        return forwards;
    }
}