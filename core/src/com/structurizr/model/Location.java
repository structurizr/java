package com.structurizr.model;

public enum Location {

    Internal,
    External;

    private static final String INTERNAL = "i";
    private static final String EXTERNAL = "e";

    public String toChar() {
        if (this == Internal) {
            return INTERNAL;
        } else {
            return EXTERNAL;
        }
    }

    public static Location fromChar(String s) {
        switch (s) {
            case INTERNAL:
                return Internal;
            default:
                return External;
        }
    }

}
