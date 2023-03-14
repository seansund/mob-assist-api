package com.dex.mobassist.server.service;

import com.dex.mobassist.server.model.Assignment;
import com.dex.mobassist.server.model.SignupOption;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;

import java.util.List;

public interface SignupOptionService extends BaseService<SignupOption> {
    List<? extends SignupOption> findAllById(List<String> optionsIds);
}
