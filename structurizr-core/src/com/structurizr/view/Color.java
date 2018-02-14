package com.structurizr.view;

public class Color {

    public static boolean isHexColorCode(String colorAsString) {
        return colorAsString != null && colorAsString.matches("^#[A-Fa-f0-9]{6}");
    }

}