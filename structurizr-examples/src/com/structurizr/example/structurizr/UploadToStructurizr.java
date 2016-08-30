package com.structurizr.example.structurizr;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;

import java.io.File;

public class UploadToStructurizr extends AbstractStructurizrWorkspace {

    public static void main(String[] args) throws Exception {
        new UploadToStructurizr().run();
    }

    void run() throws Exception {
        Workspace workspace = readFromFile();
        StructurizrClient structurizrClient = new StructurizrClient();
        structurizrClient.setWorkspaceArchiveLocation(new File(System.getProperty("user.home"), "structurizr-workspaces"));
        structurizrClient.mergeWorkspace(121, workspace);
    }

}