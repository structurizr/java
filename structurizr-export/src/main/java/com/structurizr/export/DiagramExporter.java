package com.structurizr.export;

import com.structurizr.Workspace;

import java.util.Collection;

public interface DiagramExporter extends Exporter {

    /**
     * Exports all views in the workspace.
     *
     * @param workspace     the workspace containing the views to be written
     * @return  a collection of diagram definitions, one per view
     */
    Collection<Diagram> export(Workspace workspace);

}