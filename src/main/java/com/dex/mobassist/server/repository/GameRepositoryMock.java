package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.Game;
import com.dex.mobassist.server.model.Team;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository("GameRepository")
@Profile("mock")
public class GameRepositoryMock implements GameRepository {

    private final Game currentGame;

    public GameRepositoryMock() {
        currentGame = Game.createGame(
                Team.createTeam("Grace", "navy"),
                Team.createTeam("Harvest", "Maroon"),
                "Semifinal"
        );
    }

    @Override
    public Game getGame() {
        return currentGame;
    }
}
