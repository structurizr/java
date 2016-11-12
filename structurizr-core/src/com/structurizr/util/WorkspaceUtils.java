package com.structurizr.util;

import com.structurizr.Workspace;
import com.structurizr.io.json.JsonReader;
import com.structurizr.io.json.JsonWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

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
     * @param file a File representing the JSON definition
     * @throws Exception if something goes wrong
     */
    public static void saveWorkspaceToJson(Workspace workspace, File file) throws Exception {
        if (workspace == null) {
            throw new IllegalArgumentException("A workspace must be provided.");
        } else if (file == null) {
            throw new IllegalArgumentException("The path to a JSON file must be specified.");
        }

        FileWriter writer = new FileWriter(file);
        new JsonWriter(true).write(workspace, writer);
        writer.flush();
        writer.close();
    }

}