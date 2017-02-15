package com.structurizr.util;

public class Colour {

    public static boolean isHexColourCode(String colorAsString) {
        return colorAsString != null && colorAsString.matches("^#[A-Fa-f0-9]{6}");
    }

}