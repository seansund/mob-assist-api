package com.dex.mobassist.server.repository;

import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T> {
    List<? extends T> findAll();

    <A extends T> Optional<? extends A> findById(String id);

    <A extends T> A save(@NonNull A newMember);

    boolean deleteById(@NonNull String id);

    Observable<List<? extends T>> observable();
}
