package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A component (a grouping of related functionality behind an interface that runs inside a container).
 */
public class Component extends Element {

    private Container parent;

    private String technology = "";
    private String interfaceType;
    private String implementationType;
    private String sourcePath;

    public Component() {
    }

    @Override
    @JsonIgnore
    public Element getParent() {
        return parent;
    }

    @JsonIgnore
    public Container getContainer() {
        return parent;
    }

    public void setParent(Container parent) {
        this.parent = parent;
    }

    /**
     * Gets the technology associated with this component (e.g. Spring Bean).
     *
     * @return  the technology, as a String,
     *          or null if no technology has been specified
     */
    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    /**
     * Gets the interface type (a fully qualified Java interface name).
     *
     * @return  the interface type, as a String
     */
    public String getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType;
    }

    /**
     * Gets the implementation type (a fully qualified Java class name).
     *
     * @return  the implementation type, as a String
     */
    public String getImplementationType() {
        return implementationType;
    }

    public void setImplementationType(String implementationType) {
        this.implementationType = implementationType;
    }

    /**
     * Gets the source code path that reflects this component (e.g. a GitHub URL).
     *
     * @return  a path to the source code, as a String
     */
    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    @Override
    public String getName() {
        if (this.name != null) {
            return super.getName();
        } else if (this.interfaceType != null) {
            return interfaceType.substring(interfaceType.lastIndexOf(".") + 1);
        } else if (this.implementationType != null) {
            return implementationType.substring(implementationType.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    @JsonIgnore
    public String getPackage() {
        return interfaceType.substring(0, interfaceType.lastIndexOf("."));
    }

    @Override
    public final ElementType getType() {
        return ElementType.Component;
    }

    @Override
    public String getCanonicalName() {
        return getParent().getCanonicalName() + CANONICAL_NAME_SEPARATOR + formatForCanonicalName(getName());
    }

    @Override
    protected Set<String> getRequiredTags() {
        return new LinkedHashSet<>(Arrays.asList(Tags.ELEMENT, Tags.COMPONENT));
    }

}