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
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
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

    private static final String STRUCTURIZR_CLOUD_API_URL = "https://api.structurizr.com";

    private static final String STRUCTURIZR_API_URL = "structurizr.api.url";
    private static final String STRUCTURIZR_API_KEY = "structurizr.api.key";
    private static final String STRUCTURIZR_API_SECRET = "structurizr.api.secret";

    private static final String WORKSPACE_PATH = "/workspace/";

    private static final String VERSION = Package.getPackage("com.structurizr.api").getImplementationVersion();

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
        try {
            Properties properties = new Properties();
            InputStream in = StructurizrClient.class.getClassLoader().getResourceAsStream("structurizr.properties");
            if (in != null) {
                properties.load(in);

                setUrl(properties.getProperty(STRUCTURIZR_API_URL));
                setApiKey(properties.getProperty(STRUCTURIZR_API_KEY));
                setApiSecret(properties.getProperty(STRUCTURIZR_API_SECRET));

                in.close();
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

    String getUrl() {
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

        try {
            log.info("Getting workspace with ID " + workspaceId);

            CloseableHttpClient httpClient = HttpClients.createSystem();
            HttpGet httpGet = new HttpGet(url + WORKSPACE_PATH + workspaceId);
            addHeaders(httpGet, "", "");
            debugRequest(httpGet, null);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                debugResponse(response);

                String json = EntityUtils.toString(response.getEntity());
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    archiveWorkspace(workspaceId, json);

                    if (encryptionStrategy == null) {
                        return new JsonReader().read(new StringReader(json));
                    } else {
                        EncryptedWorkspace encryptedWorkspace = new EncryptedJsonReader().read(new StringReader(json));
                        encryptedWorkspace.getEncryptionStrategy().setPassphrase(encryptionStrategy.getPassphrase());
                        return encryptedWorkspace.getWorkspace();
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

        try {
            if (mergeFromRemote) {
                Workspace remoteWorkspace = getWorkspace(workspaceId);
                if (remoteWorkspace != null) {
                    workspace.getViews().copyLayoutInformationFrom(remoteWorkspace.getViews());
                    workspace.getViews().getConfiguration().copyConfigurationFrom(remoteWorkspace.getViews().getConfiguration());
                }
            }

            workspace.setId(workspaceId);
            workspace.countAndLogWarnings();

            CloseableHttpClient httpClient = HttpClients.createSystem();
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
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
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

    private void debugRequest(HttpRequestBase httpRequest, String content) {
        log.debug(httpRequest.getMethod() + " " + httpRequest.getURI().getPath());
        Header[] headers = httpRequest.getAllHeaders();
        for (Header header : headers) {
            log.debug(header.getName() + ": " + header.getValue());
        }

        if (content != null) {
            log.debug(content);
        }
    }

    private void debugResponse(CloseableHttpResponse response) {
        log.debug(response.getStatusLine());
    }

    private void addHeaders(HttpRequestBase httpRequest, String content, String contentType) throws Exception {
        String httpMethod = httpRequest.getMethod();
        String path = httpRequest.getURI().getPath();
        String contentMd5 = new Md5Digest().generate(content);
        String nonce = "" + System.currentTimeMillis();

        HashBasedMessageAuthenticationCode hmac = new HashBasedMessageAuthenticationCode(apiSecret);
        HmacContent hmacContent = new HmacContent(httpMethod, path, contentMd5, contentType, nonce);
        httpRequest.addHeader(HttpHeaders.USER_AGENT, "structurizr-java/" + (VERSION != null ? VERSION : "dev"));
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
        try {
            FileWriter fileWriter = new FileWriter(archiveFile);
            fileWriter.write(json);
            fileWriter.flush();
            fileWriter.close();

            try {
                log.debug("Workspace from server archived to " + archiveFile.getCanonicalPath());
            } catch (IOException ioe) {
                log.debug("Workspace from server archived to " + archiveFile.getAbsolutePath());
            }
        } catch (Exception e) {
            log.warn("Could not archive JSON to " + archiveFile.getAbsolutePath());
        }
    }

    private String createArchiveFileName(long workspaceId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return "structurizr-" + workspaceId + "-" + sdf.format(new Date()) + ".json";
    }

}