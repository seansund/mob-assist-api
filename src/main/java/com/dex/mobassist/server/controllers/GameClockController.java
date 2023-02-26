package com.dex.mobassist.server.controllers;

import com.dex.mobassist.server.model.GameClock;
import com.dex.mobassist.server.repository.GameClockRepository;
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
public class GameClockController {
    private final GameClockRepository repository;

    public GameClockController(GameClockRepository repository) {
        this.repository = repository;
    }

    @QueryMapping
    public GameClock getGameClock() {
        return repository.getGameClock();
    }

    @SubscriptionMapping
    public Flux<GameClock> gameClock() {
        return RxJava3Adapter.observableToFlux(repository.clockObservable(), BackpressureStrategy.LATEST);
    }

    @MutationMapping
    public GameClock startClock() {
        return repository.start();
    }

    @MutationMapping
    public GameClock stopClock() {
        return repository.stop();
    }

    @MutationMapping
    public GameClock resetClock() {
        return repository.reset();
    }

    @MutationMapping
    public GameClock periodAdd() {
        return repository.periodAdd();
    }

    @MutationMapping
    public GameClock periodSubtract() {
        return repository.periodSubtract();
    }

    @MutationMapping
    public GameClock periodSet(@Argument("period") int period) {
        return repository.periodSet(period);
    }

    @MutationMapping
    public GameClock timeMinuteAdd() {
        return repository.timeMinuteAdd();
    }

    @MutationMapping
    public GameClock timeMinuteSubtract() {
        return repository.timeMinuteSubtract();
    }

    @MutationMapping
    public GameClock timeSecondAdd() {
        return repository.timeSecondAdd();
    }

    @MutationMapping
    public GameClock timeSecondSubtract() {
        return repository.timeSecondSubtract();
    }

    @MutationMapping
    public GameClock timeSet(@Argument("minute") int minute, @Argument("second") int second) {
        return repository.timeSet(minute, second);
    }
}
