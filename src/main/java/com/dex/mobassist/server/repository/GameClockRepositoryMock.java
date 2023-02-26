package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.GameClock;
import com.dex.mobassist.server.model.ClockControl;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

import static io.reactivex.rxjava3.core.Observable.fromIterable;
import static java.util.Collections.singletonList;

@Repository("GameClockRepository")
@Profile("mock")
public class GameClockRepositoryMock implements GameClockRepository {

    private static final int periodLengthMinutes = 8;

    private static final Subject<GameClock> EMPTY = PublishSubject.create();

    private final Subject<GameClock> subject;
    private final Subject<ClockControl> controlSubject;
    private GameClock currentClock;

    public GameClockRepositoryMock() {
        currentClock = GameClock.createGameClock(periodLengthMinutes * 60, 1);

        subject = BehaviorSubject.createDefault(currentClock);
        controlSubject = PublishSubject.create();

        final Observable<GameClock> timerObs = Observable
                .interval(0, 1, TimeUnit.SECONDS)
                .map((Long tick) -> currentClock = currentClock.tick(true));

        controlSubject
                .switchMap((ClockControl control) -> {
                    switch (control) {
                        case START -> {
                            currentClock = currentClock.start();
                            return timerObs;
                        }
                        case STOP -> {
                            currentClock = currentClock.stop();
                            return fromIterable(singletonList(currentClock));
                        }
                        case RESET -> {
                            currentClock = currentClock.reset(periodLengthMinutes * 60);

                            if (currentClock.isRunning()) {
                                return timerObs;
                            } else {
                                return fromIterable(singletonList(currentClock));
                            }
                        }
                        case UPDATE -> {
                            if (currentClock.isRunning()) {
                                return timerObs;
                            } else {
                                return fromIterable(singletonList(currentClock));
                            }
                        }
                        default -> {
                            return Observable.error(new RuntimeException("Unhandled clock control: " + control));
                        }
                    }
                })
                .subscribe(this.subject);

        controlSubject.onNext(ClockControl.STOP);
    }

    @Override
    public Observable<GameClock> clockObservable() {
        return subject;
    }

    @Override
    public GameClock start() {
        controlSubject.onNext(ClockControl.START);

        return currentClock;
    }

    @Override
    public GameClock stop() {
        controlSubject.onNext(ClockControl.STOP);

        return currentClock;
    }

    @Override
    public GameClock reset() {
        controlSubject.onNext(ClockControl.RESET);

        return currentClock;
    }

    @Override
    public GameClock getGameClock() {
        return currentClock;
    }

    @Override
    public GameClock periodAdd() {
        currentClock = currentClock.periodAdd();
        controlSubject.onNext(ClockControl.UPDATE);

        return currentClock;
    }

    @Override
    public GameClock periodSubtract() {
        currentClock = currentClock.periodSubtract();
        controlSubject.onNext(ClockControl.UPDATE);

        return currentClock;
    }

    @Override
    public GameClock periodSet(int period) {
        currentClock = currentClock.periodSet(period);
        controlSubject.onNext(ClockControl.UPDATE);

        return currentClock;
    }

    @Override
    public GameClock timeMinuteAdd() {
        currentClock = currentClock.timeMinuteChange(1);
        controlSubject.onNext(ClockControl.UPDATE);

        return currentClock;
    }

    @Override
    public GameClock timeMinuteSubtract() {
        currentClock = currentClock.timeMinuteChange(-1);
        controlSubject.onNext(ClockControl.UPDATE);

        return currentClock;
    }

    @Override
    public GameClock timeSecondAdd() {
        currentClock = currentClock.timeSecondChange(1);
        controlSubject.onNext(ClockControl.UPDATE);

        return currentClock;
    }

    @Override
    public GameClock timeSecondSubtract() {
        currentClock = currentClock.timeSecondChange(-1);
        controlSubject.onNext(ClockControl.UPDATE);

        return currentClock;
    }

    @Override
    public GameClock timeSet(int minute, int second) {
        currentClock = currentClock.timeSet(minute, second);
        controlSubject.onNext(ClockControl.UPDATE);

        return currentClock;
    }
}
