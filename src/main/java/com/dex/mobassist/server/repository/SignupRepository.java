package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.Signup;
import com.dex.mobassist.server.model.SignupQueryScope;

import java.util.List;

public interface SignupRepository extends BaseRepository<Signup> {
    List<? extends Signup> list(SignupQueryScope scope);

    Signup getCurrent();
}
