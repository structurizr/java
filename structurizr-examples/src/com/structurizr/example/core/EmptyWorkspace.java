package com.structurizr.example.core;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;

/**
 * An example of uploading an empty workspace, for when you want to use
 * Structurizr's local storage mode.
 */
public class EmptyWorkspace {

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        StructurizrClient client = new StructurizrClient("key", "secret");
        client.putWorkspace(1234, workspace);
    }

}
