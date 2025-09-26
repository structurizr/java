package com.structurizr.export.plantuml;

import java.util.Objects;

abstract class PlantUMLStyle {

    protected static final int SHADOW_DISTANCE = 10;

    protected final String name;

    PlantUMLStyle(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    abstract String getClassSelector();

    @Override
    public final int hashCode() {
        return Objects.hashCode(getClassSelector());
    }

}