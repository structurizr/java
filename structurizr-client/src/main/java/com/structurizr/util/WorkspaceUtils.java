package com.structurizr.util;

import com.structurizr.Workspace;
import com.structurizr.io.json.JsonReader;
import com.structurizr.io.json.JsonWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Some utility methods related to workspaces.
 */
public final class WorkspaceUtils {

    /**
     * Loads a workspace from a JSON definition saved as a file.
     *
     * @param file a File representing the JSON definition
     * @return a Workspace object
     * @throws Exception if something goes wrong
     */
    public static Workspace loadWorkspaceFromJson(File file) throws Exception {
        if (file == null) {
            throw new IllegalArgumentException("The path to a JSON file must be specified.");
        } else if (!file.exists()) {
            throw new IllegalArgumentException("The specified JSON file does not exist.");
        }

        return new JsonReader().read(new FileReader(file));
    }

    /**
     * Saves a workspace to a JSON definition as a file.
     *
     * @param workspace     a Workspace object
     * @param file          a File representing the JSON definition
     * @throws Exception    if something goes wrong
     */
    public static void saveWorkspaceToJson(Workspace workspace, File file) throws Exception {
        if (workspace == null) {
            throw new IllegalArgumentException("A workspace must be provided.");
        } else if (file == null) {
            throw new IllegalArgumentException("The path to a JSON file must be specified.");
        }

        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        new JsonWriter(true).write(workspace, writer);
        writer.flush();
        writer.close();
    }

    /**
     * Prints a workspace as JSON to stdout - useful for debugging purposes.
     *
     * @param workspace     the workspace to print
     */
    public static void printWorkspaceAsJson(Workspace workspace) {
        if (workspace == null) {
            throw new IllegalArgumentException("A workspace must be provided.");
        }

        try {
            System.out.println(toJson(workspace, true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Serializes the specified workspace to a JSON string.
     *
     * @param workspace     a Workspace instance
     * @param indentOutput  whether to indent the output (prettify)
     * @return  a JSON string
     * @throws Exception if something goes wrong
     */
    public static String toJson(Workspace workspace, boolean indentOutput) throws Exception {
        if (workspace == null) {
            throw new IllegalArgumentException("A workspace must be provided.");
        }

        JsonWriter jsonWriter = new JsonWriter(indentOutput);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(workspace, stringWriter);
        stringWriter.flush();
        stringWriter.close();

        return stringWriter.toString();
    }

    /**
     * Converts the specified JSON string to a Workspace instance.
     *
     * @param json      the JSON definition of the workspace
     * @return  a Workspace instance
     * @throws Exception    if the JSON can not be deserialized
     */
    public static Workspace fromJson(String json) throws Exception {
        if (json == null || json.trim().length() == 0) {
            throw new IllegalArgumentException("A JSON string must be provided.");
        }

        StringReader stringReader = new StringReader(json);
        return new JsonReader().read(stringReader);
    }

}