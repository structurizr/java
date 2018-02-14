package com.structurizr.model;

import com.structurizr.util.Url;

import java.util.HashMap;
import java.util.Map;

/**
 * Describes a HTTP based health check.
 */
public final class HttpHealthCheck {

    /** a name for the health check */
    private String name;

    /** the health check URL/endpoint */
    private String url;

    /** the headers that should be sent in the HTTP request */
    private Map<String,String> headers = new HashMap<>();

    /** the polling interval, in seconds */
    private int interval = 60;

    /** the timeout after which a health check is deemed as failed, in milliseconds */
    private long timeout = 0;

    HttpHealthCheck() {
    }

    HttpHealthCheck(String name, String url) {
        setName(name);
        setUrl(url);
    }

    /**
     * Gets the name of this health check.
     *
     * @return  the name, as a String
     */
    public String getName() {
        return name;
    }

    void setName(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("The name must not be null or empty.");
        }

        this.name = name;
    }

    /**
     * Gets the URL for this health check.
     *
     * @return  the URL, as a String
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL for this health check.
     *
     * @param url   the URL as a String
     * @throws IllegalArgumentException     if the URL is not a well-formed URL
     */
    void setUrl(String url) {
        if (url == null || url.trim().length() == 0) {
            throw new IllegalArgumentException("The URL must not be null or empty.");
        }

        if (Url.isUrl(url)) {
            this.url = url;
        } else {
            throw new IllegalArgumentException(url + " is not a valid URL.");
        }
    }

    /**
     * Adds a HTTP header, which will be sent with the HTTP request to the health check URL.
     *
     * @param name      the name of the header
     * @param value     the value of the header
     */
    public void addHeader(String name, String value) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("The header name must not be null or empty.");
        }

        if (value == null) {
            throw new IllegalArgumentException("The header value must not be null.");
        }

        this.headers.put(name, value);
    }

    /**
     * Gets a the HTTP headers associated with this health check.
     *
     * @return  a Map (name=value)
     */
    public Map<String, String> getHeaders() {
        return new HashMap<>(headers);
    }

    /**
     * Gets the polling interval of this health check.
     *
     * @return  the polling interval (in seconds), as an integer
     */
    public int getInterval() {
        return interval;
    }

    /**
     * Sets the polling interval, in seconds.
     *
     * @param interval      the polling interval, in seconds
     */
    public void setInterval(int interval) {
        if (interval < 0) {
            throw new IllegalArgumentException("The polling interval must be zero or a positive integer.");
        }

        this.interval = interval;
    }

    /**
     * Gets the timeout associated with this health check.
     *
     * @return  the timeout (in milliseconds)
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * Sets the timeout associated with this health check, in milliseconds.
     *
     * @param timeout       the timeout, in milliseconds
     */
    public void setTimeout(long timeout) {
        if (timeout < 0) {
            throw new IllegalArgumentException("The timeout must be zero or a positive integer.");
        }

        this.timeout = timeout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpHealthCheck that = (HttpHealthCheck) o;

        if (!name.equals(that.name)) return false;
        return url.equals(that.url);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + url.hashCode();
        return result;
    }

}