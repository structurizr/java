package com.structurizr.util;

import javax.annotation.Nullable;

public final class StringUtils {

    public static boolean isNullOrEmpty(@Nullable String s) {
        return s == null || s.trim().isEmpty();
    }

}
