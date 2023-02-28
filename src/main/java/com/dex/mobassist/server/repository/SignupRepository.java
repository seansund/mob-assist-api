package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.Signup;
import com.dex.mobassist.server.model.SignupQueryScope;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;

import java.util.List;

public interface SignupRepository extends BaseRepository<Signup> {
    List<Signup> list(SignupQueryScope scope);
}
