package com.structurizr.component.provider;

/**
 * Cleans up Javadoc comments for inclusion in the software architecture model.
 */
class JavadocCommentFilter {

    String filter(String s) {
        if (s == null) {
            return null;
        }

        s = s.replaceAll("\\n", " ");
        s = s.replaceAll("(?s)<.*?>", "");
        s = s.replaceAll("\\{@link (\\S*)\\}", "$1");
        s = s.replaceAll("\\{@link (\\S*) (.*?)\\}", "$2");

        return s;
    }

}