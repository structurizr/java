package com.structurizr.documentation;

class FormattedContent {

    private String content;
    private Format format;

    FormattedContent(String content, Format format) {
        this.content = content;
        this.format = format;
    }

    String getContent() {
        return content;
    }

    Format getFormat() {
        return format;
    }

}
