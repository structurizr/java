package com.structurizr.util;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Utilities for dealing with URLs.
 */
public class Url {

    public static final String WORKSPACE_URL_PREFIX = "{workspace}";

    /**
     * Determines whether the supplied string is a valid URL.
     *
     * @param urlAsString       the URL, as a String
     * @return  true if the URL is valid, false otherwise
     */
    public static boolean isUrl(String urlAsString) {
        if (!StringUtils.isNullOrEmpty(urlAsString)) {
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