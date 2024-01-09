package com.structurizr.util;

import java.util.HashMap;
import java.util.Map;

public final class MapUtils {

    /**
     * A helper method to create a Map from an array of Strings ("name=value").
     *
     * @param nameValuePairs    one or more "name=value" pairs
     *
     * @return  a Map
     */
    public static Map<String, String> create(String... nameValuePairs) {
        Map<String, String> map = new HashMap<>();

        if (nameValuePairs != null) {
            for (String nameValuePair : nameValuePairs) {
                String[] tokens = nameValuePair.split("=");
                if (tokens.length == 2) {
                    map.put(tokens[0], tokens[1]);
                }
            }
        }

        return map;
    }

}