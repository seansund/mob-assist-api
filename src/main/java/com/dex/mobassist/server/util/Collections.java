package com.dex.mobassist.server.util;

import java.util.Collection;

public final class Collections {
    public static boolean isNull(Collection<?> collection) {
        return collection == null;
    }

    public static boolean nonNull(Collection<?> collection) {
        return collection != null;
    }

    public static boolean isNullOrEmpty(Collection<?> collection) {
        return isNull(collection) || collection.isEmpty();
    }

    public static boolean nonNullAndEmpty(Collection<?> collection) {
        return nonNull(collection) && !collection.isEmpty();
    }
}
