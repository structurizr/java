package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

/**
 * A component (a grouping of related functionality behind an interface that runs inside a container).
 */
public class Component extends Element {

    private Container parent;

    private String technology = "";
    private Set<CodeElement> codeElements = new HashSet<>();
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
    @JsonIgnore
    public String getType() {
        Optional<CodeElement> optional = codeElements.stream().filter(ce -> ce.getRole() == CodeElementRole.Primary).findFirst();
        if (optional.isPresent()) {
            return optional.get().getType();
        } else {
            return null;
        }
    }

    public void setType(String type) {
        CodeElement codeElement = new CodeElement(type);
        codeElement.setRole(CodeElementRole.Primary);
        this.codeElements.add(codeElement);
    }

    public Set<CodeElement> getCode() {
        return codeElements;
    }

    void setCodeElements(Set<CodeElement> codeElements) {
        this.codeElements = codeElements;
    }

    public void addSupportingType(String type) {
        CodeElement codeElement = new CodeElement(type);
        codeElement.setRole(CodeElementRole.Supporting);
        this.codeElements.add(codeElement);
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