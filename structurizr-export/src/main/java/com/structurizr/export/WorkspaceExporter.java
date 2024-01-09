package com.structurizr.export;

import com.structurizr.Workspace;

public interface WorkspaceExporter extends Exporter {

    /**
     * Exports the entire workspace to a single String.
     *
     * @param workspace     the workspace to be exported
     * @return  a String export of the workspace
     */
    WorkspaceExport export(Workspace workspace);

}