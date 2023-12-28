package com.structurizr.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * A client for the Structurizr workspace API that allows you to get and put Structurizr workspaces in a JSON format.
 *
 * @deprecated Use WorkspaceApiClient instead
 */
@Deprecated
public class StructurizrClient extends WorkspaceApiClient {

    private static final String STRUCTURIZR_API_URL = "structurizr.api.url";
    private static final String STRUCTURIZR_API_KEY = "structurizr.api.key";
    private static final String STRUCTURIZR_API_SECRET = "structurizr.api.secret";

    /**
     * Creates a new Structurizr client based upon configuration in a structurizr.properties file
     * on the classpath with the following name-value pairs:
     * - structurizr.api.url
     * - structurizr.api.key
     * - structurizr.api.secret
     *
     * @throws StructurizrClientException   if something goes wrong
     */
    public StructurizrClient() throws StructurizrClientException {
        try (InputStream in =
                     WorkspaceApiClient.class.getClassLoader().getResourceAsStream("structurizr.properties")) {
            Properties properties = new Properties();
            if (in != null) {
                properties.load(in);

                setUrl(properties.getProperty(STRUCTURIZR_API_URL));
                setApiKey(properties.getProperty(STRUCTURIZR_API_KEY));
                setApiSecret(properties.getProperty(STRUCTURIZR_API_SECRET));
            } else {
                throw new StructurizrClientException("Could not find a structurizr.properties file on the classpath.");
            }
        } catch (IOException e) {
            throw new StructurizrClientException(e);
        }
    }

    public StructurizrClient(String apiKey, String apiSecret) {
        super(apiKey, apiSecret);
    }

    public StructurizrClient(String url, String apiKey, String apiSecret) {
        super(url, apiKey, apiSecret);
    }

}