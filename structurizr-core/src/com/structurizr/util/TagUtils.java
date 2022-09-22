package com.structurizr.util;

import java.util.Collection;

public class TagUtils {

    public static String toString(Collection<String> tags) {
        if (tags.isEmpty()) {
            return "";
        }

        StringBuilder buf = new StringBuilder();
        for (String tag : tags) {
            buf.append(tag);
            buf.append(",");
        }

        String tagsAsString = buf.toString();
        return tagsAsString.substring(0, tagsAsString.length()-1);
    }

}
