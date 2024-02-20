package com.structurizr.dsl;

final class RemoteContent {

    static final String CONTENT_TYPE_JSON = "application/json";
    static final String TEXT_PLAIN_JSON = "text/plain";

    private final String content;
    private final String contentType;

    RemoteContent(String content, String contentType) {
        this.content = content;
        this.contentType = contentType;
    }

    String getContent() {
        return content;
    }

    String getContentType() {
        return contentType;
    }

}