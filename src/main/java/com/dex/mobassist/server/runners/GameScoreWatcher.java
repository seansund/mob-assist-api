package com.dex.mobassist.server.runners;

import com.dex.mobassist.server.model.GameScore;
import com.dex.mobassist.server.repository.GameScoreRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class GameScoreWatcher implements ApplicationRunner {
    private GameScoreRepository repository;

    public GameScoreWatcher(GameScoreRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        repository.scoreObservable().subscribe((GameScore gameScore) -> {
            System.out.println("Game score: " + gameScore);
        });

        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
