package com.dex.mobassist.server.controllers;

import com.dex.mobassist.server.model.GameScore;
import com.dex.mobassist.server.model.TeamIdentifier;
import com.dex.mobassist.server.repository.GameScoreRepository;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import reactor.adapter.rxjava.RxJava3Adapter;
import reactor.core.publisher.Flux;

@Controller
@CrossOrigin
public class GameScoreController {
    private final GameScoreRepository repository;

    public GameScoreController(GameScoreRepository repository) {
        this.repository = repository;
    }

    @QueryMapping
    public GameScore getCurrentScore() {
        return repository.getCurrentScore();
    }

    @SubscriptionMapping
    public Flux<GameScore> gameScore() {
        return RxJava3Adapter.observableToFlux(repository.scoreObservable(), BackpressureStrategy.LATEST);
    }

    @MutationMapping
    public GameScore addScore(@Argument("team") String team) {
        return repository.addScore(TeamIdentifier.lookup(team));
    }

    @MutationMapping
    public GameScore addScoreTwo(@Argument("team") String team) {
        return repository.addScoreTwo(TeamIdentifier.lookup(team));
    }

    @MutationMapping
    public GameScore addScoreThree(@Argument("team") String team) {
        return repository.addScoreThree(TeamIdentifier.lookup(team));
    }

    @MutationMapping
    public GameScore subtractScore(@Argument("team") String team) {
        return repository.subtractScore(TeamIdentifier.lookup(team));
    }

    @MutationMapping
    public GameScore subtractScoreTwo(@Argument("team") String team) {
        return repository.subtractScoreTwo(TeamIdentifier.lookup(team));
    }

    @MutationMapping
    public GameScore subtractScoreThree(@Argument("team") String team) {
        return repository.subtractScoreThree(TeamIdentifier.lookup(team));
    }

    @MutationMapping
    public GameScore scoreSet(@Argument("team") String team, @Argument("score") int score) {
        return repository.scoreSet(TeamIdentifier.lookup(team), score);
    }

    @MutationMapping
    public GameScore addFoul(@Argument("team") String team) {
        return repository.addFoul(TeamIdentifier.lookup(team));
    }

    @MutationMapping
    public GameScore subtractFoul(@Argument("team") String team) {
        return repository.subtractFoul(TeamIdentifier.lookup(team));
    }

    @MutationMapping
    public GameScore foulsSet(@Argument("team") String team, @Argument("fouls") int fouls) {
        return repository.foulsSet(TeamIdentifier.lookup(team), fouls);
    }
}
