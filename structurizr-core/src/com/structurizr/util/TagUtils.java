package com.structurizr.util;

import javax.annotation.Nonnull;
import java.util.Collection;

public class TagUtils {

    @Nonnull
    public static String toString(@Nonnull Collection<String> tags) {
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
