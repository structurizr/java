package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.util.Url;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a deployment instance of a {@link Container}, which can be added to a {@link DeploymentNode}.
 */
public final class ContainerInstance extends DeploymentElement {

    private static final int DEFAULT_HEALTH_CHECK_INTERVAL_IN_SECONDS = 60;
    private static final long DEFAULT_HEALTH_CHECK_TIMEOUT_IN_MILLISECONDS = 0;

    private Container container;
    private String containerId;
    private int instanceId;
    private Set<HttpHealthCheck> healthChecks = new HashSet<>();

    ContainerInstance() {
    }

    ContainerInstance(Container container, int instanceId, String environment) {
        setContainer(container);
        addTags(Tags.CONTAINER_INSTANCE);
        setInstanceId(instanceId);
        setEnvironment(environment);
    }

    @JsonIgnore
    public Container getContainer() {
        return container;
    }

    void setContainer(Container container) {
        this.container = container;
    }

    /**
     * Gets the ID of the container that this object represents a deployment instance of.
     *
     * @return  the container ID, as a String
     */
    public String getContainerId() {
        if (container != null) {
            return container.getId();
        } else {
            return containerId;
        }
    }

    void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    /**
     * Gets the instance ID of this container.
     *
     * @return  the instance ID, an integer greater than zero
     */
    public int getInstanceId() {
        return instanceId;
    }

    void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    @JsonIgnore
    protected Set<String> getRequiredTags() {
        return Collections.emptySet();
    }

    @Override
    public void removeTag(String tag) {
        // do nothing ... tags cannot be removed from container instances (they should reflect the container they are based upon)
    }

    @Override
    @JsonIgnore
    public String getCanonicalName() {
        return container.getCanonicalName() + "[" + instanceId + "]";
    }

    @Override
    @JsonIgnore
    public Element getParent() {
        return container.getParent();
    }

    @Override
    @JsonIgnore
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {
        // no-op ... the name of a container instance is taken from the associated Container
    }

    /**
     * Gets the set of health checks associated with this container instance.
     *
     * @return  a Set of HttpHealthCheck instances
     */
    @Nonnull
    public Set<HttpHealthCheck> getHealthChecks() {
        return new HashSet<>(healthChecks);
    }

    void setHealthChecks(Set<HttpHealthCheck> healthChecks) {
        this.healthChecks = healthChecks;
    }

    /**
     * Adds a new health check, with the default interval (60 seconds) and timeout (0 milliseconds).
     *
     * @param name      the name of the health check
     * @param url       the URL of the health check
     * @return  a HttpHealthCheck instance representing the health check that has been added
     * @throws IllegalArgumentException     if the name is empty, or the URL is not a well-formed URL
     */
    @Nonnull
    public HttpHealthCheck addHealthCheck(String name, String url) {
        return addHealthCheck(name, url, DEFAULT_HEALTH_CHECK_INTERVAL_IN_SECONDS, DEFAULT_HEALTH_CHECK_TIMEOUT_IN_MILLISECONDS);
    }

    /**
     * Adds a new health check.
     *
     * @param name      the name of the health check
     * @param url       the URL of the health check
     * @param interval  the polling interval, in seconds
     * @param timeout   the timeout, in milliseconds
     * @return  a HttpHealthCheck instance representing the health check that has been added
     * @throws IllegalArgumentException     if the name is empty, the URL is not a well-formed URL, or the interval/timeout is not zero/a positive integer
     */
    @Nonnull
    public HttpHealthCheck addHealthCheck(String name, String url, int interval, long timeout) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("The name must not be null or empty.");
        }

        if (url == null || url.trim().length() == 0) {
            throw new IllegalArgumentException("The URL must not be null or empty.");
        }

        if (!Url.isUrl(url)) {
            throw new IllegalArgumentException(url + " is not a valid URL.");
        }

        if (interval < 0) {
            throw new IllegalArgumentException("The polling interval must be zero or a positive integer.");
        }

        if (timeout < 0) {
            throw new IllegalArgumentException("The timeout must be zero or a positive integer.");
        }

        HttpHealthCheck healthCheck = new HttpHealthCheck(name, url, interval, timeout);
        healthChecks.add(healthCheck);

        return healthCheck;
    }

}