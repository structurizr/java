package com.structurizr.api;

/**
 * A client for the Structurizr workspace API that allows you to get and put Structurizr workspaces in a JSON format.
 */
public class StructurizrClient extends WorkspaceApiClient {

    public StructurizrClient() throws StructurizrClientException {
        super();
    }

    public StructurizrClient(String apiKey, String apiSecret) {
        super(apiKey, apiSecret);
    }

    public StructurizrClient(String url, String apiKey, String apiSecret) {
        super(url, apiKey, apiSecret);
    }

}