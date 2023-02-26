package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.GameScore;
import com.dex.mobassist.server.model.TeamIdentifier;
import io.reactivex.rxjava3.core.Observable;

public interface GameScoreRepository {
    GameScore getCurrentScore();

    Observable<GameScore> scoreObservable();

    GameScore publishScore(GameScore score);

    GameScore addScore(TeamIdentifier team);

    GameScore addScoreTwo(TeamIdentifier team);

    GameScore addScoreThree(TeamIdentifier team);

    GameScore subtractScore(TeamIdentifier team);

    GameScore subtractScoreTwo(TeamIdentifier team);

    GameScore subtractScoreThree(TeamIdentifier team);

    GameScore scoreSet(TeamIdentifier team, int score);

    GameScore addFoul(TeamIdentifier team);

    GameScore subtractFoul(TeamIdentifier team);

    GameScore foulsSet(TeamIdentifier team, int fouls);
}
