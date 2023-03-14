package com.dex.mobassist.server.service;

import com.dex.mobassist.server.model.Signup;
import com.dex.mobassist.server.model.SignupOptionSet;
import com.dex.mobassist.server.model.SignupQueryScope;

import java.util.List;

public interface SignupService extends BaseService<Signup> {
    List<? extends Signup> listByScope(SignupQueryScope scope);

    Signup getCurrent();
}
