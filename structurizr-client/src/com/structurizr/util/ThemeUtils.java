package com.structurizr.util;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.structurizr.Workspace;
import com.structurizr.io.WorkspaceWriterException;
import com.structurizr.view.ElementStyle;
import com.structurizr.view.RelationshipStyle;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Some utility methods for exporting themes to JSON.
 */
public final class ThemeUtils {

    /**
     * Serializes the theme (element and relationship styles) in the specified workspace to a file, as a JSON string.
     *
     * @param workspace     a Workspace object
     * @param file          a File representing the JSON definition
     * @throws Exception    if something goes wrong
     */
    public static void toJson(Workspace workspace, File file) throws Exception {
        if (workspace == null) {
            throw new IllegalArgumentException("A workspace must be provided.");
        } else if (file == null) {
            throw new IllegalArgumentException("The path to a file must be specified.");
        }

        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        write(workspace, writer);
    }

    /**
     * Serializes the theme (element and relationship styles) in the specified workspace to a JSON string.
     *
     * @param workspace     a Workspace instance
     * @return              a JSON string
     * @throws Exception    if something goes wrong
     */
    public static String toJson(Workspace workspace) throws Exception {
        if (workspace == null) {
            throw new IllegalArgumentException("A workspace must be provided.");
        }

        StringWriter writer = new StringWriter();
        write(workspace, writer);

        return writer.toString();
    }

    private static void write(Workspace workspace, Writer writer) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

            writer.write(objectMapper.writeValueAsString(
                    new Theme(
                            workspace.getName(),
                            workspace.getDescription(),
                            workspace.getViews().getConfiguration().getStyles().getElements(),
                            workspace.getViews().getConfiguration().getStyles().getRelationships()
                    )));
        } catch (IOException ioe) {
            throw new WorkspaceWriterException("Could not write the theme as JSON", ioe);
        }

        writer.flush();
        writer.close();
    }

    static class Theme {

        private String name;
        private String description;
        private Collection<ElementStyle> elements = new LinkedList<>();
        private Collection<RelationshipStyle> relationships = new LinkedList<>();

        Theme(String name, String description, Collection<ElementStyle> elements, Collection<RelationshipStyle> relationships) {
            this.name = name;
            this.description = description;
            this.elements = elements;
            this.relationships = relationships;
        }

        @JsonGetter
        public String getName() {
            return name;
        }

        @JsonGetter
        public String getDescription() {
            return description;
        }

        @JsonGetter
        public Collection<ElementStyle> getElements() {
            return elements;
        }

        @JsonGetter
        public Collection<RelationshipStyle> getRelationships() {
            return relationships;
        }
    }

}