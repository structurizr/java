package com.structurizr.componentfinder;

/**
 * Cleans up Javadoc comments for inclusion in the software architecture model.
 */
class JavadocCommentFilter {

    private Integer maxCommentLength;

    JavadocCommentFilter(Integer maxCommentLength) {
        if (maxCommentLength != null && maxCommentLength < 1) {
            throw new IllegalArgumentException("Maximum comment length must be greater than 0.");
        }

        this.maxCommentLength = maxCommentLength;
    }

    String filterAndTruncate(String s) {
        if (s == null) {
            return null;
        }

        s = s.replaceAll("\\n", " ");
        s = s.replaceAll("(?s)<.*?>", "");
        s = s.replaceAll("\\{@link (\\S*)\\}", "$1");
        s = s.replaceAll("\\{@link (\\S*) (.*?)\\}", "$2");

        if (maxCommentLength != null && s.length() > maxCommentLength) {
            return s.substring(0, maxCommentLength-3) + "...";
        } else {
            return s;
        }
    }

}
