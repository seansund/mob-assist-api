package com.dex.mobassist.server.repository.mock;

import com.dex.mobassist.server.repository.BaseRepository;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

public abstract class AbstractRepositoryMock<T> implements BaseRepository<T> {
    private final BehaviorSubject<List<? extends T>> subject;

    private int nextId = 1;

    protected AbstractRepositoryMock() {
        subject = BehaviorSubject.createDefault(List.of());
    }

    @Override
    public List<? extends T> list() {
        return subject.getValue();
    }

    @Override
    public <A extends T> A getById(@NonNull String id) {
        return (A) subject.getValue()
                .stream()
                .filter(compareById(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Value not found: " + id));
    }

    @Override
    public <A extends T> A addUpdate(@NonNull A newValue) {
        final List<? extends T> values = subject.getValue();

        final Predicate<? super T> matchExisting = compareById(getId(newValue));

        final Optional<? extends T> existingValue = values
                .stream()
                .filter(matchExisting)
                .findFirst();

        final List<? extends T> updatedValues = existingValue.isPresent()
                ? values.stream().map((T val) -> matchExisting.test(val) ? newValue : val).toList()
                : concat(values.stream(), of(updateValueWithId(newValue, nextId++))).toList();

        onNext(updatedValues);

        return newValue;
    }

    @Override
    public boolean delete(@NonNull String id) {
        final List<? extends T> values = subject.getValue();

        final List<? extends T> result = values
                .stream()
                .filter(compareById(id))
                .toList();

        final boolean changed = values.size() != result.size();
        if (changed) {
            onNext(result);
        }

        return changed;
    }

    @Override
    public Observable<List<? extends T>> observable() {
        return subject;
    }

    protected void onNext(List<? extends T> value) {
        subject.onNext(preOnList(value));
    }

    protected <A extends T> Predicate<A> compareById(@NonNull String id) {
        return  (A set) -> id.equals(getId(set));
    }

    protected List<? extends T> preOnList(List<? extends T> value) {
        return value;
    }

    protected abstract <A extends T> A updateValueWithId(A value, int id);

    protected abstract <A extends T> String getId(A value);
}
