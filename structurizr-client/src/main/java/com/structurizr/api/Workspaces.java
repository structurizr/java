package com.structurizr.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Workspaces {

    private List<WorkspaceMetadata> workspaces;

    Workspaces() {
    }

    List<WorkspaceMetadata> getWorkspaces() {
        return new ArrayList<>(workspaces);
    }

    void setWorkspaces(List<WorkspaceMetadata> workspaces) {
        if (workspaces == null) {
            this.workspaces = new ArrayList<>();
        } else {
            this.workspaces = workspaces;
        }

        this.workspaces.sort(Comparator.comparingInt(WorkspaceMetadata::getId));
    }

}