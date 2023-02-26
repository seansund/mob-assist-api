package com.dex.mobassist.server.model;

import lombok.Data;

@Data
public class TeamScore {
    private int score;
    private int fouls;

    public static TeamScore createTeamScore() {
        return createTeamScore(0, 0);
    }

    public static TeamScore createTeamScore(int score, int fouls) {
        final TeamScore teamScore = new TeamScore();

        teamScore.score = score;
        teamScore.fouls = fouls;

        return teamScore;
    }

    public TeamScore scoreUpdate(int value) {
        final int newScore = score + value;
        if (newScore < 0) {
            return this;
        }

        return createTeamScore(newScore, fouls);
    }

    public TeamScore scoreSet(int newScore) {
        if (newScore < 0) {
            return this;
        }

        return createTeamScore(newScore, fouls);
    }

    public TeamScore foulsUpdate(int value) {
        final int newFouls = fouls + value;
        if (newFouls < 0) {
            return this;
        }

        return createTeamScore(score, newFouls);
    }

    public TeamScore foulsSet(int newFouls) {
        if (newFouls < 0) {
            return this;
        }

        return createTeamScore(score, newFouls);
    }
}
