package com.structurizr.inspection;

public final class Violation {

    private Inspection inspection;
    private Severity severity;
    private final String message;

    Violation(Inspection inspection, String message) {
        this.inspection = inspection;
        this.message = message;
    }

    public String getType() {
        return inspection.getType();
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return inspection.getType() + " | " + severity + " | " + message;
    }

    public Violation withSeverity(Severity severity) {
        this.severity = severity;

        return this;
    }

}