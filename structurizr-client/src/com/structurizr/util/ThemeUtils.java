package com.structurizr.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.structurizr.Workspace;
import com.structurizr.io.WorkspaceWriterException;
import com.structurizr.view.ElementStyle;
import com.structurizr.view.RelationshipStyle;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Some utility methods for exporting themes to JSON.
 */
public final class ThemeUtils {

    private static final int HTTP_OK_STATUS = 200;

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

    /**
     * Loads (and inlines) the element and relationship styles from the themes defined in the workspace, into the workspace itself.
     * This implementation simply copies the styles from all themes into the workspace.
     *
     * @param workspace     a Workspace object
     * @throws Exception    if something goes wrong
     */
    public static void loadStylesFromThemes(Workspace workspace) throws Exception {
        if (workspace.getViews().getConfiguration().getThemes() != null) {
            for (String url : workspace.getViews().getConfiguration().getThemes()) {
                CloseableHttpClient httpClient = HttpClients.createSystem();
                HttpGet httpGet = new HttpGet(url);

                CloseableHttpResponse response = httpClient.execute(httpGet);
                if (response.getCode() == HTTP_OK_STATUS) {
                    String json = EntityUtils.toString(response.getEntity());

                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                    Theme theme = objectMapper.readValue(json, Theme.class);

                    for (ElementStyle elementStyle : theme.getElements()) {
                        workspace.getViews().getConfiguration().getStyles().add(elementStyle);
                    }

                    for (RelationshipStyle relationshipStyle : theme.getRelationships()) {
                        workspace.getViews().getConfiguration().getStyles().add(relationshipStyle);
                    }
                }

                httpClient.close();
            }
        }
    }

    private static void write(Workspace workspace, Writer writer) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

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

        Theme() {
        }

        Theme(String name, String description, Collection<ElementStyle> elements, Collection<RelationshipStyle> relationships) {
            this.name = name;
            this.description = description;
            this.elements = elements;
            this.relationships = relationships;
        }

        String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        String getDescription() {
            return description;
        }

        void setDescription(String description) {
            this.description = description;
        }

        Collection<ElementStyle> getElements() {
            return elements;
        }

        void setElements(Collection<ElementStyle> elements) {
            this.elements = elements;
        }

        Collection<RelationshipStyle> getRelationships() {
            return relationships;
        }

        void setRelationships(Collection<RelationshipStyle> relationships) {
            this.relationships = relationships;
        }

    }

}