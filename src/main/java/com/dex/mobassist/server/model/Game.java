package com.dex.mobassist.server.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Game {
    private Team home;
    private Team away;
    private String title;

    public static Game createGame(@NonNull Team home, @NonNull Team away) {
        return createGame(home, away, "");
    }

    public static Game createGame(@NonNull Team home, @NonNull Team away, @NonNull String title) {
        final Game newGame = new Game();

        newGame.home = home;
        newGame.away = away;
        newGame.title = title;

        return newGame;
    }
}
