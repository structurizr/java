package com.structurizr.example.client;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;

public class StructurizrApiClientExample {

    public static void main(String[] args) throws Exception {
        StructurizrClient structurizrClient = new StructurizrClient("https://structurizr-api.cfapps.io", "key", "secret");
        Workspace workspace = structurizrClient.getWorkspace(1);
        System.out.println(workspace.getName());
    }

}
