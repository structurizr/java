package com.structurizr.util;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Utilities for dealing with URLs.
 */
public class Url {

    private static final String HTTPS_PROTOCOL = "https://";
    private static final String HTTP_PROTOCOL = "http://";

    public static final String INTRA_WORKSPACE_URL_PREFIX = "{workspace}";
    public static final String INTER_WORKSPACE_URL_REGEX = "\\{workspace:\\d+\\}.*";

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

    /**
     * Determines whether the supplied string is a valid HTTPS URL.
     *
     * @param urlAsString       the URL, as a String
     * @return  true if the URL is valid, false otherwise
     */
    public static boolean isHttpsUrl(String urlAsString) {
        return isUrl(urlAsString) && urlAsString.toLowerCase().startsWith(HTTPS_PROTOCOL);
    }

    /**
     * Determines whether the supplied string is a valid HTTP URL.
     *
     * @param urlAsString       the URL, as a String
     * @return  true if the URL is valid, false otherwise
     */
    public static boolean isHttpUrl(String urlAsString) {
        return isUrl(urlAsString) && urlAsString.toLowerCase().startsWith(HTTP_PROTOCOL);
    }

}