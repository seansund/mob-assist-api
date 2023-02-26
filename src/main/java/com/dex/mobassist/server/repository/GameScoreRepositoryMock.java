package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.GameScore;
import com.dex.mobassist.server.model.TeamIdentifier;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository("GameScoreRepository")
@Profile("mock")
public class GameScoreRepositoryMock implements GameScoreRepository {

    private final BehaviorSubject<GameScore> subject;

    public GameScoreRepositoryMock() {
        final GameScore currentScore = GameScore.createGameScore();

        subject = BehaviorSubject.createDefault(currentScore);
    }

    @Override
    public GameScore getCurrentScore() {
        return subject.getValue();
    }

    @Override
    public Observable<GameScore> scoreObservable() {
        return subject;
    }

    @Override
    public GameScore publishScore(GameScore score) {
        subject.onNext(score);

        return score;
    }

    @Override
    public GameScore addScore(TeamIdentifier team) {
        return publishScore(getCurrentScore().scoreUpdate(team, 1));
    }

    @Override
    public GameScore addScoreTwo(TeamIdentifier team) {
        return publishScore(getCurrentScore().scoreUpdate(team, 2));
    }

    @Override
    public GameScore addScoreThree(TeamIdentifier team) {
        return publishScore(getCurrentScore().scoreUpdate(team, 3));
    }

    @Override
    public GameScore subtractScore(TeamIdentifier team) {
        return publishScore(getCurrentScore().scoreUpdate(team, -1));
    }

    @Override
    public GameScore subtractScoreTwo(TeamIdentifier team) {
        return publishScore(getCurrentScore().scoreUpdate(team, -2));
    }

    @Override
    public GameScore subtractScoreThree(TeamIdentifier team) {
        return publishScore(getCurrentScore().scoreUpdate(team, -3));
    }

    @Override
    public GameScore scoreSet(TeamIdentifier team, int score) {
        return publishScore(getCurrentScore().scoreSet(team, score));
    }

    @Override
    public GameScore addFoul(TeamIdentifier team) {
        return publishScore(getCurrentScore().foulsUpdate(team, 1));
    }

    @Override
    public GameScore subtractFoul(TeamIdentifier team) {
        return publishScore(getCurrentScore().foulsUpdate(team, -1));
    }

    @Override
    public GameScore foulsSet(TeamIdentifier team, int fouls) {
        return publishScore(getCurrentScore().foulsSet(team, fouls));
    }
}
