package com.structurizr.http;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Wrapper for the HTTPClient in Apache HttpComponents, with optional caching and allowed URLs (via regexes).
 */
public class HttpClient {

    public static final String CONTENT_TYPE_IMAGE_PNG = "image/png";

    private static final int HTTP_OK_STATUS = 200;

    private int timeout = 10000; // milliseconds
    private final Set<String> allowedUrlRegexes = new HashSet<>();

    private final Map<String,RemoteContent> contentCache = new HashMap<>();

    public HttpClient() {
    }

    /**
     * Sets the timeout in milliseconds.
     *
     * @param timeoutInMilliseconds     the timeout in milliseconds
     */
    public void setTimeout(int timeoutInMilliseconds) {
        if (timeoutInMilliseconds < 0) {
            throw new IllegalArgumentException("Timeout must be a positive integer");
        }

        this.timeout = timeoutInMilliseconds;
    }

    /**
     * HTTP GET of a URL, without caching.
     *
     * @param url       the URL, as a String
     * @return          a RemoteContent object representing the response
     */
    public RemoteContent get(String url) {
        return get(url, false);
    }

    /**
     * HTTP GET of a URL.
     *
     * @param url       the URL, as a String
     * @param cache     true if the result should be cached, false otherwise
     * @return          a RemoteContent object representing the response
     */
    public RemoteContent get(String url, boolean cache) {
        if (!isAllowed(url)) {
            throw new HttpClientException("Access to " + url + " is not permitted");
        }

        RemoteContent remoteContent = contentCache.get(url);
        if (remoteContent == null) {
            ConnectionConfig connectionConfig = ConnectionConfig.custom()
                    .setConnectTimeout(timeout, TimeUnit.MILLISECONDS)
                    .setSocketTimeout(timeout, TimeUnit.MILLISECONDS)
                    .build();

            BasicHttpClientConnectionManager cm = new BasicHttpClientConnectionManager();
            cm.setConnectionConfig(connectionConfig);

            try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .useSystemProperties()
                    .setConnectionManager(cm)
                    .build()) {

                HttpGet httpGet = new HttpGet(url);
                CloseableHttpResponse response = httpClient.execute(httpGet);

                int httpStatus = response.getCode();
                if (httpStatus == HTTP_OK_STATUS) {
                    String contentType = response.getEntity().getContentType();
                    if (CONTENT_TYPE_IMAGE_PNG.equals(contentType)) {
                        remoteContent = new RemoteContent(EntityUtils.toByteArray(response.getEntity()), contentType);
                    } else {
                        remoteContent = new RemoteContent(EntityUtils.toString(response.getEntity()), contentType);
                    }

                    if (cache) {
                        contentCache.put(url, remoteContent);
                    }
                } else {
                    throw new HttpClientException("The content from " + url + " could not be loaded: HTTP status=" + httpStatus);
                }
            } catch (Exception ioe) {
                throw new HttpClientException("The content from " + url + " could not be loaded: " + ioe.getMessage());
            }
        }

        return remoteContent;
    }

    /**
     * Adds an allowed URL regex.
     *
     * @param regex     the regex to allow
     */
    public void allow(String regex) {
        allowedUrlRegexes.add(regex);
    }

    private boolean isAllowed(String url) {
        for (String regex : allowedUrlRegexes) {
            if (url.matches(regex)) {
                return true;
            }
        }

        return false;
    }

}