package com.structurizr.util;

import java.net.MalformedURLException;
import java.net.URL;

public class Url {

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

}