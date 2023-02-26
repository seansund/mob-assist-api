package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.GameClock;
import io.reactivex.rxjava3.core.Observable;

public interface GameClockRepository {
    Observable<GameClock> clockObservable();

    GameClock start();

    GameClock stop();

    GameClock reset();

    GameClock getGameClock();

    GameClock periodAdd();

    GameClock periodSubtract();

    GameClock periodSet(int period);

    GameClock timeMinuteAdd();

    GameClock timeMinuteSubtract();

    GameClock timeSecondAdd();

    GameClock timeSecondSubtract();

    GameClock timeSet(int minute, int second);
}
