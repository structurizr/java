package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.structurizr.Workspace;
import com.structurizr.http.HttpClient;
import com.structurizr.http.RemoteContent;
import com.structurizr.io.WorkspaceWriterException;
import com.structurizr.util.ImageUtils;
import com.structurizr.util.StringUtils;
import com.structurizr.util.Url;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Some utility methods for exporting themes to JSON.
 */
public final class ThemeUtils {

    private static final int DEFAULT_TIMEOUT_IN_MILLISECONDS = 10000;

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
     * Loads the element and relationship styles from the themes defined in the workspace, into the workspace itself.
     * This implementation simply copies the styles from all themes into the workspace.
     * This uses a default timeout value of 10000ms.
     *
     * @param workspace     a Workspace object
     * @throws Exception    if something goes wrong
     */
    public static void loadThemes(Workspace workspace) throws Exception {
        loadThemes(workspace, DEFAULT_TIMEOUT_IN_MILLISECONDS);
    }

    /**
     * Loads the element and relationship styles from the themes defined in the workspace, into the workspace itself.
     * This implementation simply copies the styles from all themes into the workspace.
     *
     * @param workspace                 a Workspace object
     * @param timeoutInMilliseconds     the timeout in milliseconds
     * @throws Exception    if something goes wrong
     */
    public static void loadThemes(Workspace workspace, int timeoutInMilliseconds) throws Exception {
        HttpClient httpClient = new HttpClient();
        httpClient.setTimeout(timeoutInMilliseconds);

        loadThemes(workspace, httpClient);
    }

    public static void loadThemes(Workspace workspace, HttpClient httpClient) throws Exception {
        for (String themeLocation : workspace.getViews().getConfiguration().getThemes()) {
            if (Url.isUrl(themeLocation)) {
                RemoteContent remoteContent = httpClient.get(themeLocation);
                if (remoteContent.getContentType().startsWith(RemoteContent.CONTENT_TYPE_JSON) || remoteContent.getContentType().startsWith(RemoteContent.CONTENT_TYPE_PLAIN_TEXT)) {
                    Theme theme = fromJson(remoteContent.getContentAsString());
                    String baseUrl = themeLocation.substring(0, themeLocation.lastIndexOf('/') + 1);

                    for (ElementStyle elementStyle : theme.getElements()) {
                        String icon = elementStyle.getIcon();
                        if (!StringUtils.isNullOrEmpty(icon)) {
                            if (Url.isHttpUrl(icon) || Url.isHttpsUrl(icon)) {
                                // okay, image served over HTTP or HTTPS
                            } else if (icon.startsWith("data:image")) {
                                // also okay, data URI
                            } else {
                                // convert the relative icon filename into a full URL
                                elementStyle.setIcon(baseUrl + icon);
                            }
                        }
                    }

                    workspace.getViews().getConfiguration().getStyles().addStylesFromTheme(theme);
                } else {
                    throw new RuntimeException(String.format("%s - expected content type of %s, actual content type is %s", themeLocation, RemoteContent.CONTENT_TYPE_JSON, remoteContent.getContentType()));
                }
            }
        }
    }

    /**
     * Inlines the element and relationship styles from the specified file, adding the styles into the workspace
     * and overriding any properties already set.
     *
     * @param workspace     the Workspace to load the theme into
     * @param file          a File object representing a theme (a JSON file)
     * @throws Exception    if something goes wrong
     */
    public static void inlineTheme(Workspace workspace, File file) throws Exception {
        String json = Files.readString(file.toPath());
        Theme theme = fromJson(json);

        for (ElementStyle elementStyle : theme.getElements()) {
            String icon = elementStyle.getIcon();
            if (!StringUtils.isNullOrEmpty(icon)) {
                if (icon.startsWith("http")) {
                    // okay, image served over HTTP
                } else if (icon.startsWith("data:image")) {
                    // also okay, data URI
                } else {
                    // convert the relative icon filename into a data URI
                    elementStyle.setIcon(ImageUtils.getImageAsDataUri(new File(file.getParentFile(), icon)));
                }
            }
        }

        workspace.getViews().getConfiguration().getStyles().inlineTheme(theme);
    }

    private static Theme fromJson(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper.readValue(json, Theme.class);
    }

    private static void write(Workspace workspace, Writer writer) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

            Theme theme = new Theme(
                    workspace.getName(),
                    workspace.getDescription(),
                    workspace.getViews().getConfiguration().getStyles().getElements(),
                    workspace.getViews().getConfiguration().getStyles().getRelationships()
            );
            theme.setFont(workspace.getViews().getConfiguration().getBranding().getFont());
            theme.setLogo(workspace.getViews().getConfiguration().getBranding().getLogo());

            writer.write(objectMapper.writeValueAsString(theme));
        } catch (IOException ioe) {
            throw new WorkspaceWriterException("Could not write the theme as JSON", ioe);
        }

        writer.flush();
        writer.close();
    }

}