package com.structurizr.io.dot;

import com.structurizr.Workspace;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;
import com.structurizr.view.ElementView;
import com.structurizr.view.RelationshipView;
import com.structurizr.view.View;
import io.github.livingdocumentation.dotdiagram.DotGraph;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * This is a simple implementation of a workspace writer that outputs
 * to the Graphviz DOT format. You will need graphviz installed and
 * correctly configured. See https://github.com/cyriux/dot-diagram
 * for more information.
 */
public class DotWriter {

    /**
     * Writes the views in the given workspace as DOT notation, to the specified Writer.
     *
     * @param workspace     the workspace containing the views to be written
     * @param writer        the Writer to write to
     */
    public void write(Workspace workspace, Writer writer) {
        workspace.getViews().getSystemContextViews().forEach(v -> write(v, null, writer));
        workspace.getViews().getContainerViews().forEach(v -> write(v, v.getSoftwareSystem(), writer));
        workspace.getViews().getComponentViews().forEach(v -> write(v, v.getContainer(), writer));
    }

    /**
     * Write the views in the given workspace as DOT notation, to stdout.
     *
     * @param workspace     the workspace containing the views to be written
     */
    public void write(Workspace workspace) {
        StringWriter stringWriter = new StringWriter();
        write(workspace, stringWriter);
        System.out.println(stringWriter.toString());
    }

    private void write(View view, Element clusterElement, Writer writer)  {
        try {
            DotGraph graph = new DotGraph(view.getName());
            DotGraph.Digraph digraph = graph.getDigraph();
            DotGraph.Cluster cluster = null;

            if (clusterElement != null) {
                cluster = digraph.addCluster(clusterElement.getId());
                cluster.setLabel(clusterElement.getName());
            }

            for (ElementView elementView : view.getElements()) {
                Element element = elementView.getElement();

                if (clusterElement != null && element.getParent() == clusterElement) {
                    cluster.addNode(element.getId()).setLabel(element.getName());
                } else {
                    digraph.addNode(element.getId()).setLabel(element.getName());
                }
            }

            for (RelationshipView relationshipView : view.getRelationships()) {
                Relationship relationship = relationshipView.getRelationship();
                digraph.addAssociation(
                        relationship.getSourceId(),
                        relationship.getDestinationId()).setLabel(relationship.getDescription());
            }

            String output = graph.render().trim();
            writer.write(output);
            writer.write(System.lineSeparator());
            writer.write(System.lineSeparator());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}