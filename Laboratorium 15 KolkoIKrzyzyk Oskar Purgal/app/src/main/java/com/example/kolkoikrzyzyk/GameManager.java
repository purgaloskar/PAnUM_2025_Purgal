package com.example.kolkoikrzyzyk;

import java.io.Serializable;

public class GameManager implements Serializable {

    private int totalGames;
    private int playedGames;

    private int xWins;
    private int oWins;
    private int draws;

    private double xPoints;
    private double oPoints;

    public GameManager(int totalGames) {
        this.totalGames = totalGames;
    }

    public void addXWin() {
        xWins++;
        playedGames++;
        xPoints += 1;
    }

    public void addOWin() {
        oWins++;
        playedGames++;
        oPoints += 1;
    }

    public void addDraw() {
        draws++;
        playedGames++;
        xPoints += 0.5;
        oPoints += 0.5;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public int getPlayedGames() {
        return playedGames;
    }

    public int getRemainingGames() {
        return totalGames - playedGames;
    }

    public int getXWins() {
        return xWins;
    }

    public int getOWins() {
        return oWins;
    }

    public int getDraws() {
        return draws;
    }

    public double getXPoints() {
        return xPoints;
    }

    public double getOPoints() {
        return oPoints;
    }
}