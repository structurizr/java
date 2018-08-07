package com.structurizr.model;

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
    private int interval;

    /** the timeout after which a health check is deemed as failed, in milliseconds */
    private long timeout;

    HttpHealthCheck() {
    }

    HttpHealthCheck(String name, String url, int interval, long timeout) {
        setName(name);
        setUrl(url);
        setInterval(interval);
        setTimeout(timeout);
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

    void setUrl(String url) {
        this.url = url;
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

    void setInterval(int interval) {
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

    void setTimeout(long timeout) {
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