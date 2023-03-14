package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.Assignment;
import com.dex.mobassist.server.model.SignupOption;

import java.util.List;

public interface SignupOptionRepository extends BaseRepository<SignupOption> {
    List<? extends SignupOption> findAllById(List<String> optionIds);
}
