package com.dex.mobassist.server.service;

import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;

import java.util.List;

public interface BaseService<T> {
    List<? extends T> list();

    T getById(String id);

    T addUpdate(@NonNull T newMember);

    boolean delete(@NonNull String id);

    Observable<List<? extends T>> observable();
}
