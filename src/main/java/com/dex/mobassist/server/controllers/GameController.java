package com.dex.mobassist.server.controllers;

import com.dex.mobassist.server.model.Game;
import com.dex.mobassist.server.repository.GameRepository;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin
public class GameController {
    private final GameRepository repository;

    public GameController(GameRepository repository) {
        this.repository = repository;
    }

    @QueryMapping
    public Game getGame() {
        return repository.getGame();
    }
}
