package com.structurizr.model;

public class Enterprise {

    private String name;

    Enterprise() {
    }

    public Enterprise(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("Name must be specified.");
        }

        this.name = name;
    }

    public String getName() {
        return name;
    }

}
