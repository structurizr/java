package com.structurizr.io.websequencediagrams;

import com.structurizr.Workspace;
import com.structurizr.io.WorkspaceWriter;
import com.structurizr.io.WorkspaceWriterException;
import com.structurizr.model.InteractionStyle;
import com.structurizr.model.Relationship;
import com.structurizr.view.DynamicView;
import com.structurizr.view.RelationshipView;

import java.io.Writer;
import java.util.Set;
import java.util.TreeSet;

/**
 * A simple writer that outputs a diagram definition that can be copy-pasted
 * into https://www.websequencediagrams.com
 *
 * This implementation only supports a basic sequence of interactions,
 * both synchronous and asynchronous. It doesn't support return messages,
 * parallel behaviour, etc.
 */
public final class WebSequenceDiagramsWriter implements WorkspaceWriter {

    private static final String SYNCHRONOUS_INTERACTION = "->";
    private static final String ASYNCHRONOUS_INTERACTION = "->>";

    @Override
    public void write(Workspace workspace, Writer writer) throws WorkspaceWriterException {
        if (workspace != null && writer != null) {
            try {
                for (DynamicView view : workspace.getViews().getDynamicViews()) {
                    write(view, writer);
                }
            } catch (Exception e) {
                throw new WorkspaceWriterException("There was an error creating a websequencediagram", e);
            }
        }
    }

    private void write(DynamicView view, Writer writer) throws Exception {
        writer.write("title " + view.getName() + " - " + view.getKey());
        writer.write(System.lineSeparator());
        writer.write(System.lineSeparator());

        Set<RelationshipView> relationships = new TreeSet<>((rv1, rv2) -> rv1.getOrder().compareTo(rv2.getOrder()));
        relationships.addAll(view.getRelationships());

        for (RelationshipView relationshipView : relationships) {
            Relationship r = relationshipView.getRelationship();
            // Thing A->Thing B: Description
            writer.write(String.format("%s%s%s: %s",
                    r.getSource().getName(),
                    r.getInteractionStyle() == InteractionStyle.Synchronous ? SYNCHRONOUS_INTERACTION : ASYNCHRONOUS_INTERACTION,
                    r.getDestination().getName(),
                    relationshipView.getDescription()
                    ));
            writer.write(System.lineSeparator());
        }

        writer.write(System.lineSeparator());
    }

}