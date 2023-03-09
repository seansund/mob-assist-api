package com.dex.mobassist.server.service;

import com.dex.mobassist.server.model.SignupOption;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;

import java.util.List;

public interface SignupOptionService extends BaseService<SignupOption> {
    @Override
    List<? extends SignupOption> list();

    @Override
    SignupOption getById(String id);

    @Override
    SignupOption addUpdate(@NonNull SignupOption newMember);

    @Override
    boolean delete(@NonNull String id);

    @Override
    Observable<List<? extends SignupOption>> observable();
}
