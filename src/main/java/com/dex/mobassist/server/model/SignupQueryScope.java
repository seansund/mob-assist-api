package com.dex.mobassist.server.model;

import java.util.Arrays;

import static java.util.Arrays.stream;

public enum SignupQueryScope {
    all,
    upcoming,
    future;

    public static SignupQueryScope lookup(String scope) {
        return lookup(scope, SignupQueryScope.upcoming);
    }

    public static SignupQueryScope lookup(String scope, SignupQueryScope defaultScope) {
        return stream(values())
                .filter((SignupQueryScope scopeVal) -> scopeVal.name().equals(scope))
                .findFirst()
                .orElse(defaultScope);
    }
}
