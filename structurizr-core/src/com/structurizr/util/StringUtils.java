package com.structurizr.util;

import java.net.MalformedURLException;
import java.net.URL;

public class StringUtils {

    public static boolean isUrl(String urlAsString) {
        if (urlAsString != null && urlAsString.trim().length() > 0) {
            try {
                new URL(urlAsString);
                return true;
            } catch (MalformedURLException murle) {
                return false;
            }
        }

        return false;
    }

    public static boolean isHexColourCode(String colorAsString) {
        return colorAsString != null && colorAsString.matches("^#[A-Fa-f0-9]{6}");
    }

}