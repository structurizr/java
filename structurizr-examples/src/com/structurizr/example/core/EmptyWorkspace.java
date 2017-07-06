package com.structurizr.example.core;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;

/**
 * An example of uploading an empty workspace, for when you want to use
 * Structurizr's local storage mode.
 */
public class EmptyWorkspace {

    private static final long WORKSPACE_ID = 123456;
    private static final String API_KEY = "";
    private static final String API_SECRET = "";

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}
