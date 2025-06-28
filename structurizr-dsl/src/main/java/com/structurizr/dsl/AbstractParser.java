package com.structurizr.dsl;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.util.regex.Pattern;

abstract class AbstractParser {

    private static final int HTTP_OK_STATUS = 200;

    private static final Pattern VIEW_KEY_PATTERN = Pattern.compile("[\\w-]+");

    void validateViewKey(String key) {
        if (!VIEW_KEY_PATTERN.matcher(key).matches()) {
            throw new RuntimeException("View keys can only contain the following characters: a-zA-Z0-9_-");
        }
    }

    String removeNonWordCharacters(String name) {
        return name.replaceAll("\\W", "");
    }

    protected RemoteContent readFromUrl(String url) {
        try (CloseableHttpClient httpClient = HttpClients.createSystem()) {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);

            int httpStatus = response.getCode();
            if (httpStatus == HTTP_OK_STATUS) {
                return new RemoteContent(EntityUtils.toString(response.getEntity()), response.getEntity().getContentType());
            } else {
                throw new RuntimeException("The content from " + url + " could not be loaded: HTTP status=" + httpStatus);
            }
        } catch (Exception ioe) {
            throw new RuntimeException("The content from " + url + " could not be loaded: " + ioe.getMessage());
        }
    }

}
