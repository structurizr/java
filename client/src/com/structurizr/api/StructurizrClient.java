package com.structurizr.api;

import com.structurizr.Workspace;
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

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Properties;

public class StructurizrClient {

    private static final Log log = LogFactory.getLog(StructurizrClient.class);
    private static final String GMT_TIME_ZONE = "GMT";

    public static final String STRUCTURIZR_API_URL = "structurizr.api.url";
    public static final String STRUCTURIZR_API_KEY = "structurizr.api.key";
    public static final String STRUCTURIZR_API_SECRET = "structurizr.api.secret";

    private String url;
    private String apiKey;
    private String apiSecret;

    public StructurizrClient() {
        try {
            Properties properties = new Properties();
            InputStream in = StructurizrClient.class.getClassLoader().getResourceAsStream("structurizr.properties");
            if (in != null) {
                properties.load(in);
                this.url = properties.getProperty(STRUCTURIZR_API_URL);
                this.apiKey = properties.getProperty(STRUCTURIZR_API_KEY);
                this.apiSecret = properties.getProperty(STRUCTURIZR_API_SECRET);
                in.close();
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    public StructurizrClient(String url, String apiKey, String apiSecret) {
        this.url = url;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public Workspace getWorkspace(long workspaceId) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url + "/workspace/" + workspaceId);
        addHeaders(httpGet, "", "");
        debugRequest(httpGet);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            debugResponse(response);

            String json = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return new JsonReader().read(new StringReader(json));
            } else {
                ApiError apiError = ApiError.parse(json);
                throw new StructurizrClientException(apiError.getMessage());
            }
        }
    }

    public void putWorkspace(Workspace workspace) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url + "/workspace/" + workspace.getId());

        JsonWriter jsonWriter = new JsonWriter(true);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(workspace, stringWriter);

        StringEntity stringEntity = new StringEntity(stringWriter.toString(), ContentType.APPLICATION_JSON);
        httpPut.setEntity(stringEntity);
        addHeaders(httpPut, EntityUtils.toString(stringEntity), ContentType.APPLICATION_JSON.toString());
        debugRequest(httpPut);

        try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
            debugResponse(response);
        }
    }

    private void debugRequest(HttpRequestBase httpRequest) {
        log.debug(httpRequest.getMethod() + " " + httpRequest.getURI().getPath());
        Header[] headers = httpRequest.getAllHeaders();
        for (Header header : headers) {
            log.debug(header.getName() + ": " + header.getValue());
        }
    }

    private void debugResponse(CloseableHttpResponse response) {
        log.info(response.getStatusLine());
    }

    private void addHeaders(HttpRequestBase httpRequest, String content, String contentType) throws Exception {
        String httpMethod = httpRequest.getMethod();
        String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of(GMT_TIME_ZONE)));
        String path = httpRequest.getURI().getPath();
        String contentMd5 = new Md5Digest().generate(content);

        HashBasedMessageAuthenticationCode hmac = new HashBasedMessageAuthenticationCode(apiSecret);
        HmacContent hmacContent = new HmacContent(httpMethod, path, contentMd5, contentType, date);
        httpRequest.addHeader(HttpHeaders.AUTHORIZATION, new HmacAuthorizationHeader(apiKey, hmac.generate(hmacContent.toString())).format());
        httpRequest.addHeader(HttpHeaders.DATE, date);
        httpRequest.addHeader(HttpHeaders.CONTENT_MD5, Base64.getEncoder().encodeToString(contentMd5.getBytes()));
        httpRequest.addHeader(HttpHeaders.CONTENT_TYPE, contentType);
    }

}