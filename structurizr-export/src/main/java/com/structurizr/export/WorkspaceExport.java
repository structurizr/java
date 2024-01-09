package com.structurizr.export;

public abstract class WorkspaceExport {

    private String definition;

    public WorkspaceExport(String definition) {
        this.definition = definition;
    }

    public String getDefinition() {
        return definition;
    }

    public abstract String getFileExtension();

}