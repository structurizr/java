package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.structurizr.Workspace;
import com.structurizr.io.WorkspaceWriterException;
import com.structurizr.util.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

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
    public static void loadThemes(Workspace workspace) throws Exception {
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
                String baseUrl = url.substring(0, url.lastIndexOf('/') + 1);

                for (ElementStyle elementStyle : theme.getElements()) {
                    String icon = elementStyle.getIcon();
                    if (!StringUtils.isNullOrEmpty(icon)) {
                        if (icon.startsWith("http")) {
                            // okay, image served over HTTP
                        } else if (icon.startsWith("data:image")) {
                            // also okay, data URI
                        } else {
                            // convert the relative icon filename into a full URL
                            elementStyle.setIcon(baseUrl + icon);
                        }
                    }
                }

                workspace.getViews().getConfiguration().getStyles().addStylesFromTheme(theme);
            }

            httpClient.close();
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

}