package com.structurizr.inspection;

public final class Violation {

    private final String type;
    private Severity severity;
    private final String message;

    Violation(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return type + " | " + severity + " | " + message;
    }

    public Violation withSeverity(Severity severity) {
        this.severity = severity;

        return this;
    }

}