package com.dex.mobassist.server.model;

import lombok.NonNull;

public enum TeamIdentifier {
    HOME,
    AWAY;

    public static TeamIdentifier lookup(@NonNull String value) {
        return TeamIdentifier.valueOf(value.toUpperCase());
    }
}
