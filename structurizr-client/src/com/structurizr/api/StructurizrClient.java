package com.structurizr.api;

import com.structurizr.Workspace;
import com.structurizr.encryption.EncryptedWorkspace;
import com.structurizr.encryption.EncryptionLocation;
import com.structurizr.encryption.EncryptionStrategy;
import com.structurizr.io.json.EncryptedJsonReader;
import com.structurizr.io.json.EncryptedJsonWriter;
import com.structurizr.io.json.JsonReader;
import com.structurizr.io.json.JsonWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;

/**
 * A client for the Structurizr API (https://api.structurizr.com)
 * that allows you to get and put Structurizr workspaces in a JSON format.
 */
public final class StructurizrClient {

    private static final Log log = LogFactory.getLog(StructurizrClient.class);

    private static final String VERSION = Package.getPackage("com.structurizr.api").getImplementationVersion();
    private static final String STRUCTURIZR_FOR_JAVA_AGENT = "structurizr-java/" + VERSION;

    private static final String STRUCTURIZR_CLOUD_API_URL = "https://api.structurizr.com";

    private static final String STRUCTURIZR_API_URL = "structurizr.api.url";
    private static final String STRUCTURIZR_API_KEY = "structurizr.api.key";
    private static final String STRUCTURIZR_API_SECRET = "structurizr.api.secret";

    private static final String WORKSPACE_PATH = "/workspace/";

    private String url;
    private String apiKey;
    private String apiSecret;

    private EncryptionStrategy encryptionStrategy;

    private boolean mergeFromRemote = true;
    private File workspaceArchiveLocation = new File(".");

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
                     StructurizrClient.class.getClassLoader().getResourceAsStream("structurizr.properties")) {
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
            log.error(e);
            throw new StructurizrClientException(e);
        }
    }

    /**
     * Creates a new Structurizr API client with the specified API key and secret,
     * for the default API URL (https://api.structurizr.com).
     *
     * @param apiKey    the API key of your workspace
     * @param apiSecret the API secret of your workspace
     */
    public StructurizrClient(String apiKey, String apiSecret) {
        this(STRUCTURIZR_CLOUD_API_URL, apiKey, apiSecret);
    }

    /**
     * Creates a new Structurizr client with the specified API URL, key and secret.
     *
     * @param url       the URL of your structurizr web instance
     * @param apiKey    the API key of your workspace
     * @param apiSecret the API secret of your workspace
     */
    public StructurizrClient(String url, String apiKey, String apiSecret) {
        setUrl(url);
        setApiKey(apiKey);
        setApiSecret(apiSecret);
    }

    /**
     * Gets the API URL that this client is for.
     *
     * @return  the API URL, as a String
     */
    public String getUrl() {
        return this.url;
    }

    private void setUrl(String url) {
        if (url == null || url.trim().length() == 0) {
            throw new IllegalArgumentException("The API URL must not be null or empty.");
        }

        if (url.endsWith("/")) {
            this.url = url.substring(0, url.length() - 1);
        } else {
            this.url = url;
        }
    }

    String getApiKey() {
        return apiKey;
    }

    private void setApiKey(String apiKey) {
        if (apiKey == null || apiKey.trim().length() == 0) {
            throw new IllegalArgumentException("The API key must not be null or empty.");
        }

        this.apiKey = apiKey;
    }

    String getApiSecret() {
        return apiSecret;
    }

    private void setApiSecret(String apiSecret) {
        if (apiSecret == null || apiSecret.trim().length() == 0) {
            throw new IllegalArgumentException("The API secret must not be null or empty.");
        }

        this.apiSecret = apiSecret;
    }

    /**
     * Gets the location where a copy of the workspace is archived when it is retrieved from the server.
     *
     * @return a File instance representing a directory, or null if this client instance is not archiving
     */
    public File getWorkspaceArchiveLocation() {
        return this.workspaceArchiveLocation;
    }

    /**
     * Sets the location where a copy of the workspace will be archived whenever it is retrieved from
     * the server. Set this to null if you don't want archiving.
     *
     * @param workspaceArchiveLocation a File instance representing a directory, or null if
     *                                 you don't want archiving
     */
    public void setWorkspaceArchiveLocation(File workspaceArchiveLocation) {
        this.workspaceArchiveLocation = workspaceArchiveLocation;
    }

    /**
     * Sets the encryption strategy for use when getting or putting workspaces.
     *
     * @param encryptionStrategy an EncryptionStrategy implementation
     */
    public void setEncryptionStrategy(EncryptionStrategy encryptionStrategy) {
        this.encryptionStrategy = encryptionStrategy;
    }

    /**
     * Specifies whether the layout of diagrams from a remote workspace should be retained when putting
     * a new version of the workspace.
     *
     * @param mergeFromRemote   true if layout information should be merged from the remote workspace, false otherwise
     */
    public void setMergeFromRemote(boolean mergeFromRemote) {
        this.mergeFromRemote = mergeFromRemote;
    }

    /**
     * Locks the workspace with the given ID.
     *
     * @param   workspaceId     the ID of your workspace
     * @return                  true if the workspace could be locked, false otherwise
     * @throws StructurizrClientException   if there are problems related to the network, authorization, etc
     */
    public boolean lockWorkspace(long workspaceId) throws StructurizrClientException {
        return manageLockForWorkspace(workspaceId, true);
    }

    /**
     * Unlocks the workspace with the given ID.
     *
     * @param   workspaceId     the ID of your workspace
     * @return                  true if the workspace could be unlocked, false otherwise
     * @throws StructurizrClientException   if there are problems related to the network, authorization, etc
     */
    public boolean unlockWorkspace(long workspaceId) throws StructurizrClientException {
        return manageLockForWorkspace(workspaceId, false);
    }

    private boolean manageLockForWorkspace(long workspaceId, boolean lock) throws StructurizrClientException {
        if (workspaceId <= 0) {
            throw new IllegalArgumentException("The workspace ID must be a positive integer.");
        }

        try (CloseableHttpClient httpClient = HttpClients.createSystem()) {
            HttpUriRequestBase httpRequest;

            if (lock) {
                log.info("Locking workspace with ID " + workspaceId);
                httpRequest = new HttpPut(url + WORKSPACE_PATH + workspaceId + "/lock?user=" + System.getProperty("user.name") + "&agent=" + STRUCTURIZR_FOR_JAVA_AGENT);
            } else {
                log.info("Unlocking workspace with ID " + workspaceId);
                httpRequest = new HttpDelete(url + WORKSPACE_PATH + workspaceId + "/lock?user=" + System.getProperty("user.name") + "&agent=" + STRUCTURIZR_FOR_JAVA_AGENT);
            }

            addHeaders(httpRequest, "", "");
            debugRequest(httpRequest, null);

            try (CloseableHttpResponse response = httpClient.execute(httpRequest)) {
                debugResponse(response);

                String responseText = EntityUtils.toString(response.getEntity());
                if (response.getCode() == HttpStatus.SC_OK) {
                    ApiResponse apiResponse = ApiResponse.parse(responseText);
                    log.info(responseText);
                    return apiResponse.getMessage().equals("OK");
                } else {
                    ApiError apiError = ApiError.parse(responseText);
                    throw new StructurizrClientException(apiError.getMessage());
                }
            }
        } catch (Exception e) {
            log.error(e);
            throw new StructurizrClientException(e);
        }
    }

    /**
     * Gets the workspace with the given ID.
     *
     * @param workspaceId the ID of your workspace
     * @return a Workspace instance
     * @throws StructurizrClientException   if there are problems related to the network, authorization, JSON deserialization, etc
     */
    public Workspace getWorkspace(long workspaceId) throws StructurizrClientException {
        if (workspaceId <= 0) {
            throw new IllegalArgumentException("The workspace ID must be a positive integer.");
        }

        try (CloseableHttpClient httpClient = HttpClients.createSystem()) {
            log.info("Getting workspace with ID " + workspaceId);
            HttpGet httpGet = new HttpGet(url + WORKSPACE_PATH + workspaceId);
            addHeaders(httpGet, "", "");
            debugRequest(httpGet, null);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                debugResponse(response);

                String json = EntityUtils.toString(response.getEntity());
                if (response.getCode() == HttpStatus.SC_OK) {
                    archiveWorkspace(workspaceId, json);

                    if (encryptionStrategy == null) {
                        return new JsonReader().read(new StringReader(json));
                    } else {
                        EncryptedWorkspace encryptedWorkspace = new EncryptedJsonReader().read(new StringReader(json));

                        if (encryptedWorkspace.getEncryptionStrategy() != null) {
                            encryptedWorkspace.getEncryptionStrategy().setPassphrase(encryptionStrategy.getPassphrase());
                            return encryptedWorkspace.getWorkspace();
                        } else {
                            // this workspace isn't encrypted, even though the client has an encryption strategy set
                            return new JsonReader().read(new StringReader(json));
                        }
                    }
                } else {
                    ApiError apiError = ApiError.parse(json);
                    throw new StructurizrClientException(apiError.getMessage());
                }
            }
        } catch (Exception e) {
            log.error(e);
            throw new StructurizrClientException(e);
        }
    }

    /**
     * Updates the given workspace.
     *
     * @param workspaceId the ID of your workspace
     * @param workspace   the workspace instance to update
     * @throws StructurizrClientException   if there are problems related to the network, authorization, JSON serialization, etc
     */
    public void putWorkspace(long workspaceId, Workspace workspace) throws StructurizrClientException {
        if (workspace == null) {
            throw new IllegalArgumentException("The workspace must not be null.");
        } else if (workspaceId <= 0) {
            throw new IllegalArgumentException("The workspace ID must be a positive integer.");
        }

        try (CloseableHttpClient httpClient = HttpClients.createSystem()) {
            if (mergeFromRemote) {
                Workspace remoteWorkspace = getWorkspace(workspaceId);
                if (remoteWorkspace != null) {
                    workspace.getViews().copyLayoutInformationFrom(remoteWorkspace.getViews());
                    workspace.getViews().getConfiguration().copyConfigurationFrom(remoteWorkspace.getViews().getConfiguration());
                }
            }

            workspace.setId(workspaceId);
            workspace.setThumbnail(null);
            workspace.setLastModifiedDate(new Date());
            workspace.setLastModifiedAgent(STRUCTURIZR_FOR_JAVA_AGENT);
            workspace.setLastModifiedUser(System.getProperty("user.name"));

            workspace.countAndLogWarnings();

            HttpPut httpPut = new HttpPut(url + WORKSPACE_PATH + workspaceId);

            StringWriter stringWriter = new StringWriter();
            if (encryptionStrategy == null) {
                JsonWriter jsonWriter = new JsonWriter(false);
                jsonWriter.write(workspace, stringWriter);
            } else {
                EncryptedWorkspace encryptedWorkspace = new EncryptedWorkspace(workspace, encryptionStrategy);
                encryptionStrategy.setLocation(EncryptionLocation.Client);
                EncryptedJsonWriter jsonWriter = new EncryptedJsonWriter(false);
                jsonWriter.write(encryptedWorkspace, stringWriter);
            }

            StringEntity stringEntity = new StringEntity(stringWriter.toString(), ContentType.APPLICATION_JSON);
            httpPut.setEntity(stringEntity);
            addHeaders(httpPut, EntityUtils.toString(stringEntity), ContentType.APPLICATION_JSON.toString());

            debugRequest(httpPut, EntityUtils.toString(stringEntity));

            log.info("Putting workspace with ID " + workspaceId);
            try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
                String json = EntityUtils.toString(response.getEntity());
                if (response.getCode() == HttpStatus.SC_OK) {
                    debugResponse(response);
                    log.info(json);
                } else {
                    ApiError apiError = ApiError.parse(json);
                    throw new StructurizrClientException(apiError.getMessage());
                }
            }
        } catch (Exception e) {
            log.error(e);
            throw new StructurizrClientException(e);
        }
    }

    private void debugRequest(HttpUriRequestBase httpRequest, String content) {
        if (log.isDebugEnabled()) {
            log.debug(httpRequest.getMethod() + " " + httpRequest.getPath());
            Header[] headers = httpRequest.getHeaders();
            for (Header header : headers) {
                log.debug(header.getName() + ": " + header.getValue());
            }
            if (content != null) {
                log.debug(content);
            }
        }
    }

    private void debugResponse(CloseableHttpResponse response) {
        log.debug(response.getCode());
    }

    private void addHeaders(HttpUriRequestBase httpRequest, String content, String contentType) throws Exception {
        String httpMethod = httpRequest.getMethod();
        String path = httpRequest.getPath();
        String contentMd5 = new Md5Digest().generate(content);
        String nonce = "" + System.currentTimeMillis();

        HashBasedMessageAuthenticationCode hmac = new HashBasedMessageAuthenticationCode(apiSecret);
        HmacContent hmacContent = new HmacContent(httpMethod, path, contentMd5, contentType, nonce);
        httpRequest.addHeader(HttpHeaders.USER_AGENT, STRUCTURIZR_FOR_JAVA_AGENT);
        httpRequest.addHeader(HttpHeaders.AUTHORIZATION, new HmacAuthorizationHeader(apiKey, hmac.generate(hmacContent.toString())).format());
        httpRequest.addHeader(HttpHeaders.NONCE, nonce);

        if (httpMethod.equals("PUT")) {
            httpRequest.addHeader(HttpHeaders.CONTENT_MD5, Base64.getEncoder().encodeToString(contentMd5.getBytes("UTF-8")));
            httpRequest.addHeader(HttpHeaders.CONTENT_TYPE, contentType);
        }
    }

    private void archiveWorkspace(long workspaceId, String json) {
        if (this.workspaceArchiveLocation == null) {
            return;
        }

        File archiveFile = new File(workspaceArchiveLocation, createArchiveFileName(workspaceId));
        try (FileWriter fileWriter = new FileWriter(archiveFile)) {
            fileWriter.write(json);
            fileWriter.flush();

            debugArchivedWorkspaceLocation(archiveFile);
        } catch (Exception e) {
            log.warn("Could not archive JSON to " + archiveFile.getAbsolutePath());
        }
    }

    private void debugArchivedWorkspaceLocation(File archiveFile) {
        if (log.isDebugEnabled()) {
            try {
                log.debug("Workspace from server archived to " + archiveFile.getCanonicalPath());
            } catch (IOException ioe) {
                log.debug("Workspace from server archived to " + archiveFile.getAbsolutePath());
            }
        }
    }

    private String createArchiveFileName(long workspaceId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return "structurizr-" + workspaceId + "-" + sdf.format(new Date()) + ".json";
    }

}