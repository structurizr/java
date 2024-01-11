package com.structurizr.assistant;

public final class Recommendation {

    private final String type;
    private final Priority priority;
    private final String description;

    Recommendation(String type, Priority priority, String description) {
        this.type = type;
        this.priority = priority;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public Priority getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return type + " | " + priority + " | " + description;
    }

    public enum Priority {
        Low,
        Medium,
        High
    }

}