package com.betxi.backend.Pick;

import com.betxi.backend.Player.Player;
import jakarta.persistence.*;

@Entity
public class Pick {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Player player;

    private String statistic;

    private double line;

    private String prediction;

    private Integer actualValue;

    private String result = "PENDING";

    public Pick() {
    }

    public Pick(Player player, String statistic, double line, String prediction) {
        this.player = player;
        this.statistic = statistic;
        this.line = line;
        this.prediction = prediction;
        this.result = "PENDING";
    }

    public Long getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public String getStatistic() {
        return statistic;
    }

    public double getLine() {
        return line;
    }

    public String getPrediction() {
        return prediction;
    }

    public Integer getActualValue() {
        return actualValue;
    }

    public String getResult() {
        return result;
    }

    public void setActualValue(Integer actualValue) {
        this.actualValue = actualValue;
    }

    public void setResult(String result) {
        this.result = result;
    }
}