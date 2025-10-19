package com.structurizr.util;

public final class FeatureNotEnabledException extends RuntimeException {

    public FeatureNotEnabledException(String feature) {
        super("Feature " + feature + " is not enabled");
    }

    public FeatureNotEnabledException(String feature, String message) {
        super(String.format("%s (feature %s is not enabled)", message, feature));
    }

}