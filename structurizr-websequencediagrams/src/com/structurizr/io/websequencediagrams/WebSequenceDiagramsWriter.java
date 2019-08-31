package com.structurizr.io.websequencediagrams;

import com.structurizr.Workspace;
import com.structurizr.model.InteractionStyle;
import com.structurizr.model.Relationship;
import com.structurizr.view.DynamicView;
import com.structurizr.view.RelationshipView;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * A simple writer that outputs dynamic diagram definitions that can be copy-pasted
 * into https://www.websequencediagrams.com
 *
 * This implementation only supports a basic sequence of interactions,
 * both synchronous and asynchronous. It doesn't support return messages,
 * parallel behaviour, etc.
 */
public final class WebSequenceDiagramsWriter {

    private static final String SYNCHRONOUS_INTERACTION = "->";
    private static final String ASYNCHRONOUS_INTERACTION = "->>";

    /**
     * Writes the dynamic views in the given workspace as WebSequenceDiagrams definitions, to the specified writer.
     *
     * @param workspace     the workspace containing the views to be written
     * @param writer        the Writer to write to
     */
    public void write(Workspace workspace, Writer writer) {
        if (workspace != null && writer != null) {
            for (DynamicView view : workspace.getViews().getDynamicViews()) {
                write(view, writer);
            }
        }
    }

    /**
     * Writes the dynamic views in the given workspace as WebSequenceDiagrams definitions, to stdout.
     *
     * @param workspace     the workspace containing the views to be written
     */
    public void write(Workspace workspace) {
        StringWriter stringWriter = new StringWriter();
        write(workspace, stringWriter);
        System.out.println(stringWriter.toString());
    }

    private void write(DynamicView view, Writer writer) {
        try {
            writer.write("title " + view.getName() + " - " + view.getKey());
            writer.write(System.lineSeparator());
            writer.write(System.lineSeparator());

            for (RelationshipView relationshipView : view.getRelationships()) {
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
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}