package com.structurizr.example.structurizr;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;

public class UploadToStructurizr extends AbstractStructurizrWorkspace {

    public static void main(String[] args) throws Exception {
        new UploadToStructurizr().run();
    }

    void run() throws Exception {
        Workspace workspace = readFromFile();
        StructurizrClient structurizrClient = new StructurizrClient();
        structurizrClient.mergeWorkspace(121, workspace);
    }

}