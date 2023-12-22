package com.structurizr.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.structurizr.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hc.core5.http.HttpStatus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * A client for the Structurizr Admin API.
 */
public class AdminApiClient extends AbstractApiClient {

    private static final Log log = LogFactory.getLog(AdminApiClient.class);

    private final String username;
    private final String apiKey;

    /**
     * Creates a new admin API client.
     *
     * @param url       the URL of your Structurizr instance
     * @param username  the username (only required for the Structurizr cloud service)
     * @param apiKey    the API key of your workspace
     */
    public AdminApiClient(String url, String username, String apiKey) {
        setUrl(url);

        this.username = username;

        if (apiKey == null || apiKey.trim().length() == 0) {
            throw new IllegalArgumentException("The API key must not be null or empty.");
        }

        this.apiKey = apiKey;
    }

    /**
     * Gets a list of all workspaces.
     *
     * @return  a List of WorkspaceMetadata objects
     * @throws StructurizrClientException   if an error occurs
     */
    public List<WorkspaceMetadata> getWorkspaces() throws StructurizrClientException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + WORKSPACE_PATH))
                    .header(HttpHeaders.AUTHORIZATION, createAuthorizationHeader())
                    .header(HttpHeaders.USER_AGENT, agent)
                    .build();
            HttpClient client = HttpClient.newHttpClient();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();

            if (response.statusCode() == HttpStatus.SC_OK) {
                Workspaces workspaces = objectMapper.readValue(response.body(), Workspaces.class);
                return workspaces.getWorkspaces();
            } else {
                ApiResponse apiResponse = ApiResponse.parse(json);
                throw new StructurizrClientException(apiResponse.getMessage());
            }
        } catch (Exception e) {
            log.error(e);
            throw new StructurizrClientException(e);
        }
    }

    /**
     * Creates a new workspace.
     *
     * @return  a WorkspaceMetadata object representing the new workspace
     * @throws StructurizrClientException   if an error occurs
     */
    public WorkspaceMetadata createWorkspace() throws StructurizrClientException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + WORKSPACE_PATH))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .header(HttpHeaders.AUTHORIZATION, createAuthorizationHeader())
                    .header(HttpHeaders.USER_AGENT, agent)
                    .build();
            HttpClient client = HttpClient.newHttpClient();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();

            if (response.statusCode() == HttpStatus.SC_OK) {
                return objectMapper.readValue(json, WorkspaceMetadata.class);
            } else {
                ApiResponse apiResponse = ApiResponse.parse(json);
                throw new StructurizrClientException(apiResponse.getMessage());
            }
        } catch (Exception e) {
            log.error(e);
            throw new StructurizrClientException(e);
        }
    }

    /**
     * Deletes a workspace.
     *
     * @param workspaceId       the ID of the workspace to delete
     * @throws StructurizrClientException   if an error occurs
     */
    public void deleteWorkspace(int workspaceId) throws StructurizrClientException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + WORKSPACE_PATH + "/" + workspaceId))
                    .DELETE()
                    .header(HttpHeaders.AUTHORIZATION, createAuthorizationHeader())
                    .header(HttpHeaders.USER_AGENT, agent)
                    .build();
            HttpClient client = HttpClient.newHttpClient();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();

            if (response.statusCode() == HttpStatus.SC_OK) {
                ApiResponse apiResponse = ApiResponse.parse(json);
                log.debug(apiResponse.getMessage());
            } else {
                ApiResponse apiResponse = ApiResponse.parse(json);
                throw new StructurizrClientException(apiResponse.getMessage());
            }
        } catch (Exception e) {
            log.error(e);
            throw new StructurizrClientException(e);
        }
    }

    private String createAuthorizationHeader() {
        if (StringUtils.isNullOrEmpty(username)) {
            return apiKey;
        } else {
            return username + ":" + apiKey;
        }
    }

}