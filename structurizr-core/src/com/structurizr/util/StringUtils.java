package com.structurizr.util;

public final class StringUtils {

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

}