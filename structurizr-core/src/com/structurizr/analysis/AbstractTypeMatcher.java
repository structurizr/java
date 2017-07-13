package com.structurizr.analysis;

public abstract class AbstractTypeMatcher implements TypeMatcher {

    private String description;
    private String technology;

    public AbstractTypeMatcher(String description, String technology) {
        this.description = description;
        this.technology = technology;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getTechnology() {
        return technology;
    }

}
