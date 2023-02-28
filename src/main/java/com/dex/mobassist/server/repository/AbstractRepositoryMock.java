package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.SignupOptionSet;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

public abstract class AbstractRepositoryMock<T> implements BaseRepository<T> {
    private final BehaviorSubject<List<T>> subject;

    protected AbstractRepositoryMock() {
        subject = BehaviorSubject.createDefault(List.of());
    }

    @Override
    public List<T> list() {
        return subject.getValue();
    }

    @Override
    public T getById(@NonNull String id) {
        return subject.getValue()
                .stream()
                .filter(compareById(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Value not found: " + id));
    }

    @Override
    public T addUpdate(@NonNull T newValue) {
        final List<T> values = subject.getValue();

        final Predicate<T> isExisting = compareById(getId(newValue));

        final Optional<T> existingValue = values
                .stream()
                .filter(isExisting)
                .findFirst();

        final List<T> updatedValues = existingValue.isPresent()
                ? values.stream().map((T val) -> isExisting.test(val) ? generateIdForValue(newValue) : val).toList()
                : concat(values.stream(), of(newValue)).toList();

        onNext(updatedValues);

        return newValue;
    }

    @Override
    public boolean delete(@NonNull String id) {
        final List<T> values = subject.getValue();

        final List<T> result = values
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
    public Observable<List<T>> observable() {
        return subject;
    }

    protected void onNext(List<T> value) {
        subject.onNext(preOnList(value));
    }

    protected Predicate<T> compareById(@NonNull String id) {
        return (T set) -> id.equals(getId(set));
    }

    protected List<T> preOnList(List<T> value) {
        return value;
    }

    protected abstract T generateIdForValue(T value);


    protected abstract String getId(T value);
}