package com.structurizr.http;

/**
 * Wrapper for remote content loaded via HTTP.
 */
public final class RemoteContent {

    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_PLAIN_TEXT = "text/plain";

    private final String content;
    private final byte[] bytes;
    private final String contentType;

    RemoteContent(String content, String contentType) {
        this.content = content;
        this.bytes = null;
        this.contentType = contentType;
    }

    RemoteContent(byte[] content, String contentType) {
        this.content = null;
        this.bytes = content;
        this.contentType = contentType;
    }

    public String getContentAsString() {
        return content;
    }

    public byte[] getContentAsBytes() {
        return bytes;
    }

    public String getContentType() {
        return contentType;
    }

}