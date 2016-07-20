package com.structurizr.api;

import com.structurizr.Workspace;
import com.structurizr.encryption.*;
import com.structurizr.io.json.JsonReader;
import com.structurizr.io.json.JsonWriter;
import com.structurizr.view.View;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;

public class StructurizrClient {

    private static final Log log = LogFactory.getLog(StructurizrClient.class);

    public static final String STRUCTURIZR_API_URL = "structurizr.api.url";
    public static final String STRUCTURIZR_API_KEY = "structurizr.api.key";
    public static final String STRUCTURIZR_API_SECRET = "structurizr.api.secret";

    private static final String WORKSPACE_PATH = "/workspace/";

    private static final String VERSION = Package.getPackage("com.structurizr.api").getImplementationVersion();

    private String url;
    private String apiKey;
    private String apiSecret;

    private EncryptionStrategy encryptionStrategy;

    /**
     * the location where a copy of the workspace will be archived when it is retrieved from the server
     */
    private File workspaceArchiveLocation = new File(".");

    /**
     * Creates a new Structurizr client based upon configuration in a structurizr.properties file
     * on the classpath with the following name-value pairs:
     * - structurizr.api.url
     * - structurizr.api.key
     * - structurizr.api.secret
     */
    public StructurizrClient() {
        try {
            Properties properties = new Properties();
            InputStream in = StructurizrClient.class.getClassLoader().getResourceAsStream("structurizr.properties");
            if (in != null) {
                properties.load(in);
                setUrl(properties.getProperty(STRUCTURIZR_API_URL));
                this.apiKey = properties.getProperty(STRUCTURIZR_API_KEY);
                this.apiSecret = properties.getProperty(STRUCTURIZR_API_SECRET);
                in.close();
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    /**
     * Creates a new Structurizr client at https://api.structurizr.com with the specified API key and secret.
     *
     * @param apiKey    the API key of your workspace
     * @param apiSecret the API secret of your workspace
     */
    public StructurizrClient(String apiKey, String apiSecret) {
        setUrl("https://api.structurizr.com");
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
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
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if (url != null) {
            if (url.endsWith("/")) {
                this.url = url.substring(0, url.length() - 1);
            } else {
                this.url = url;
            }
        }
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
     * Gets the workspace with the given ID.
     *
     * @param workspaceId the ID of your workspace
     * @return a Workspace instance
     * @throws Exception if there are problems related to the network, authorization, JSON deserialization, etc
     */
    public Workspace getWorkspace(long workspaceId) throws Exception {
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
    }

    /**
     * Updates the given workspace.
     *
     * @param workspaceId the ID of your workspace
     * @param workspace   the workspace instance to update
     * @throws Exception if there are problems related to the network, authorization, JSON serialization, etc
     */
    public void putWorkspace(long workspaceId, Workspace workspace) throws Exception {
        log.info("Putting workspace with ID " + workspaceId);
        if (workspace == null) {
            throw new IllegalArgumentException("A workspace must be supplied");
        } else if (workspaceId <= 0) {
            throw new IllegalArgumentException("The workspace ID must be set");
        }

        workspace.setId(workspaceId);

        // output warnings if diagram keys have not been specified
        workspace.getViews().getSystemContextViews().stream().filter(v -> v.getKey() == null).forEach(v -> log.warn("Key not set for view \"" + v.getName() + "\""));
        workspace.getViews().getContainerViews()    .stream().filter(v -> v.getKey() == null).forEach(v -> log.warn("Key not set for view \"" + v.getName() + "\""));
        workspace.getViews().getComponentViews()    .stream().filter(v -> v.getKey() == null).forEach(v -> log.warn("Key not set for view \"" + v.getName() + "\""));
        workspace.getViews().getDynamicViews()      .stream().filter(v -> v.getKey() == null).forEach(v -> log.warn("Key not set for view \"" + v.getName() + "\""));

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
    }

    /**
     * Fetches the workspace with the given workspaceId from the server and merges its layout information with
     * the given workspace. All models from the the new workspace are taken, only the old layout information is preserved.
     *
     * @param workspaceId the ID of your workspace
     * @param workspace   the new workspace
     * @throws Exception if you are not allowed to update the workspace with the given ID or there are any network troubles
     */
    public void mergeWorkspace(long workspaceId, Workspace workspace) throws Exception {
        log.info("Merging workspace with ID " + workspaceId);

        Workspace currentWorkspace = getWorkspace(workspaceId);
        if (currentWorkspace != null) {
            workspace.getViews().copyLayoutInformationFrom(currentWorkspace.getViews());
        }
        putWorkspace(workspaceId, workspace);
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
        httpRequest.addHeader(HttpHeaders.CONTENT_MD5, Base64.getEncoder().encodeToString(contentMd5.getBytes("UTF-8")));
        httpRequest.addHeader(HttpHeaders.CONTENT_TYPE, contentType);
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
    
    /**
     * Uploads a backup json
     *
     * @param workspaceId the ID of your workspace
     * @param backupFilename backup file to upload
     * @throws Exception if there are problems related to the network, authorization, JSON serialization, etc
     */
    public void putBackup(long workspaceId, String backupFilename) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createSystem();
        HttpPut httpPut = new HttpPut(url + WORKSPACE_PATH + workspaceId);

        byte[] fileContentBytes = Files.readAllBytes(Paths.get(backupFilename));
        String fileContents = new String(fileContentBytes, StandardCharsets.UTF_8);

        StringEntity stringEntity = new StringEntity(fileContents, ContentType.APPLICATION_JSON);
        httpPut.setEntity(stringEntity);
        addHeaders(httpPut, EntityUtils.toString(stringEntity), ContentType.APPLICATION_JSON.toString());

        debugRequest(httpPut, EntityUtils.toString(stringEntity));

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
    }
}
