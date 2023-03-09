package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.Member;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;

import java.util.List;

public interface BaseRepository<T> {
    List<? extends T> list();

    <A extends T> A getById(String id);

    <A extends T> A addUpdate(@NonNull A newMember);

    boolean delete(@NonNull String id);

    Observable<List<? extends T>> observable();
}
