package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A component (a grouping of related functionality behind an interface that runs inside a container).
 */
public class Component extends Element {

    private Container parent;

    private String technology = "";
    private String type;
    private Set<String> supportingTypes = new HashSet<>();
    private String sourcePath;
    private long size;

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
     * Gets the type of this component (e.g. a fully qualified Java interface/class name).
     *
     * @return  the type, as a String
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonIgnore
    public Set<String> getSupportingTypes() {
        return supportingTypes;
    }

    void setSupportingTypes(Set<String> supportingTypes) {
        this.supportingTypes = supportingTypes;
    }

    public void addSupportingType(String type) {
        if (type != null && !supportingTypes.contains(type) && !type.equals(this.getType())) {
            this.supportingTypes.add(type);
        }
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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @JsonIgnore
    public String getPackage() {
        if (getType() != null) {
            try {
                return Class.forName(getType()).getPackage().getName();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return null;
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