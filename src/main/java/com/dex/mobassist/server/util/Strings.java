package com.dex.mobassist.server.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Strings {
    public static boolean isNullOrEmpty(String toTest) {
        return toTest == null || toTest.length() == 0;
    }

    public static boolean nonNullOrEmpty(String toTest) {
        return !isNullOrEmpty(toTest);
    }

    public static int compare(String a, String b) {
        if (a == null && b == null) {
            return 0;
        } else if (a == null) {
            return -1;
        } else if (b == null) {
            return 1;
        } else {
            return a.compareTo(b);
        }
    }

    public static String hashString(String stringToHash) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(stringToHash.getBytes());
            return new String(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
