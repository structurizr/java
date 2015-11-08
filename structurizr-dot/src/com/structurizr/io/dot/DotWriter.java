package com.structurizr.io.dot;

import com.structurizr.Workspace;
import com.structurizr.io.StructurizrWriter;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;
import com.structurizr.view.ElementView;
import com.structurizr.view.RelationshipView;
import com.structurizr.view.View;
import org.livingdocumentation.dotdiagram.DotGraph;

import java.io.IOException;
import java.io.Writer;

public class DotWriter implements StructurizrWriter {

    @Override
    public void write(Workspace workspace, Writer writer) {
        workspace.getViews().getSystemContextViews().forEach(v -> write(v, writer));
        workspace.getViews().getContainerViews().forEach(v -> write(v, writer));
        workspace.getViews().getComponentViews().forEach(v -> write(v, writer));
    }

    private void write(View view, Writer writer)  {
        try {
            DotGraph graph = new DotGraph(view.getTitle());
            DotGraph.Digraph digraph = graph.getDigraph();

            for (ElementView elementView : view.getElements()) {
                Element element = elementView.getElement();

                digraph.addNode(element.getId()).setLabel(element.getName());
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
