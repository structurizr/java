package com.structurizr.util;

import org.apache.commons.logging.Log;

/**
 * @author Klaus Lehner, Catalysts GmbH
 */
public final class Validate {

    private Validate() {
    }

    public static void notNullOrEmpty(String value, String fieldName) {
        if (value == null || value.length() == 0) {
            throw new IllegalArgumentException(fieldName + " must not be null or empty");
        }
    }

    public static void warnIfNullOrEmpty(Log log, String value, String fieldName) {
        if (value == null || value.length() == 0) {
            log.warn(fieldName + " should not be null or empty");
        }
    }
}
