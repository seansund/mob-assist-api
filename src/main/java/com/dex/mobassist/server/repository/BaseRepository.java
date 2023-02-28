package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.Member;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;

import java.util.List;

public interface BaseRepository<T> {
    List<T> list();

    T getById(String id);

    T addUpdate(@NonNull T newMember);

    boolean delete(@NonNull String id);

    Observable<List<T>> observable();
}
