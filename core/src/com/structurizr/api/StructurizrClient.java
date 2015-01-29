package com.structurizr.api;

import com.structurizr.Workspace;
import com.structurizr.io.json.JsonReader;
import com.structurizr.io.json.JsonWriter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Base64;

public class StructurizrClient {

    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    private static final String HMAC_HEADER_NAME = "hmac";

    private String url;
    private String secret;

    public StructurizrClient(String url, String secret) {
        this.url = url;
        this.secret = secret;
    }

    public Workspace getWorkspace(long workspaceId) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url + "/workspace/" + workspaceId);
        addHmacHeader(httpGet, "" + workspaceId);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            System.out.println(response.getStatusLine());
            System.out.println(response.getEntity().getContentType());

            String json = EntityUtils.toString(response.getEntity());
            System.out.println(json);

            return new JsonReader().read(new StringReader(json));
        }
    }

    public void updateWorkspace(Workspace workspace) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url + "/workspace/" + workspace.getId());
        addHmacHeader(httpPut, "" + workspace.getId());

        JsonWriter jsonWriter = new JsonWriter(true);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(workspace, stringWriter);

        StringEntity stringEntity = new StringEntity(stringWriter.toString(), ContentType.APPLICATION_JSON);
        httpPut.setEntity(stringEntity);
        httpClient.execute(httpPut);

        try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
            System.out.println(response.getStatusLine());
        }
    }

    private void addHmacHeader(HttpRequestBase httpRequest, String data) throws Exception {
        String hmac = calculateHmac(this.secret, data);
        httpRequest.addHeader(HMAC_HEADER_NAME, hmac);
    }

    private String calculateHmac(String secret, String data) throws Exception {
        SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(),	HMAC_SHA256_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(rawHmac);
    }

}
