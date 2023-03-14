package com.dex.mobassist.server.util;

public final class Strings {
    public static int compare(String a, String b) {
        if (a == null && b == null) {
            return 0;
        } else if (a != null) {
            return a.compareTo(b);
        } else {
            return -1;
        }
    }
}
