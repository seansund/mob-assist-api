package com.dex.mobassist.server.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class GameScore {
    private TeamScore home;
    private TeamScore away;

    public static GameScore createGameScore() {
        return createGameScore(TeamScore.createTeamScore(), TeamScore.createTeamScore());
    }

    public static GameScore createGameScore(@NonNull TeamScore home, @NonNull TeamScore away) {
        final GameScore score = new GameScore();

        score.home = home;
        score.away = away;

        return score;
    }

    public GameScore scoreUpdate(@NonNull TeamIdentifier id, int value) {
        if (id == TeamIdentifier.HOME) {
            return createGameScore(home.scoreUpdate(value), away);
        } else {
            return createGameScore(home, away.scoreUpdate(value));
        }
    }

    public GameScore scoreSet(@NonNull TeamIdentifier id, int value) {
        if (id == TeamIdentifier.HOME) {
            return createGameScore(home.scoreSet(value), away);
        } else {
            return createGameScore(home, away.scoreSet(value));
        }
    }

    public GameScore foulsUpdate(@NonNull TeamIdentifier id, int value) {
        if (id == TeamIdentifier.HOME) {
            return createGameScore(home.foulsUpdate(value), away);
        } else {
            return createGameScore(home, away.foulsUpdate(value));
        }
    }

    public GameScore foulsSet(@NonNull TeamIdentifier id, int value) {
        if (id == TeamIdentifier.HOME) {
            return createGameScore(home.foulsSet(value), away);
        } else {
            return createGameScore(home, away.foulsSet(value));
        }
    }
}
