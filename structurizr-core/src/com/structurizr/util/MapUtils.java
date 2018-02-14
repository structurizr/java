package com.structurizr.util;

import java.util.HashMap;
import java.util.Map;

public final class MapUtils {

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
