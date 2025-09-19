package com.structurizr.dsl;

class NameValuePair {

    private NameValueType type;
    private final String name;
    private final String value;

    NameValuePair(String name, String value) {
        this.name = name;
        this.value = value;
    }

    NameValueType getType() {
        return type;
    }

    void setType(NameValueType type) {
        this.type = type;
    }

    String getName() {
        return name;
    }

    String getValue() {
        return value;
    }

}

enum NameValueType {

    Constant,
    Variable,
    TextBlock

}