package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

/**
 * The word "component" is a hugely overloaded term in the software development
 * industry, but in this context a component is simply a grouping of related
 * functionality encapsulated behind a well-defined interface. If you're using a
 * language like Java or C#, the simplest way to think of a component is that
 * it's a collection of implementation classes behind an interface. Aspects such
 * as how those components are packaged (e.g. one component vs many components
 * per JAR file, DLL, shared library, etc) is a separate and orthogonal concern.
 */
public final class Component extends StaticStructureElement {

    private Container parent;

    private String technology;
    private Set<CodeElement> codeElements = new HashSet<>();
    private long size;

    Component() {
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

    void setParent(Container parent) {
        this.parent = parent;
    }

    /**
     * Gets the technology associated with this component (e.g. "Spring Bean").
     *
     * @return  the technology, as a String,
     *          or null if no technology has been specified
     */
    public String getTechnology() {
        return technology;
    }

    /**
     * Sets the technology associated with this component (e.g. "Spring Bean").
     *
     * @param technology    the technology, as a String
     */
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
        final CodeElement code = getPrimaryCode();
        if (code != null) {
            return code.getType();
        } else {
            return null;
        }
    }

    /**
     * Sets the type of this component (e.g. a fully qualified Java interface/class name).
     *
     * @param type the primary class for this component
     * @return  the CodeElement that was created
     * @throws IllegalArgumentException if the specified type is null
     */
    public CodeElement setType(Class<?> type) {
        return setType(new CodeElement(type));
    }

    /**
     * Sets the type of this component (e.g. a fully qualified Java interface/class name).
     *
     * @param name  the name
     * @param type  the fully qualified type name
     * @param namespace  the namespace
     * @return  the CodeElement that was created
     * @throws IllegalArgumentException if the specified type is null
     */
    public CodeElement setType(String name, String type, String namespace) {
        return setType(new CodeElement(name, type, namespace));
    }

    /**
     * Sets the type of this component (e.g. a fully qualified Java interface/class name).
     *
     * @param code the primary CodeElement for this component
     * @return the CodeElement that was created
     * @throws IllegalArgumentException if the specified type is null
     */
    public CodeElement setType(CodeElement code) {
        Optional.ofNullable(getPrimaryCode())
                .ifPresent(existing -> codeElements.remove(existing));

        code.setRole(CodeElementRole.Primary);
        this.codeElements.add(code);

        return code;
    }

    public CodeElement getPrimaryCode() {
        return codeElements
                .stream()
                .filter(ce -> ce.getRole() == CodeElementRole.Primary)
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the set of CodeElement objects.
     *
     * @return  a Set, which could be empty
     */
    public Set<CodeElement> getCode() {
        return new HashSet<>(codeElements);
    }

    void setCode(Set<CodeElement> codeElements) {
        this.codeElements = codeElements;
    }

    /**
     * Adds a supporting type to this Component.
     *
     * @param type  a class representing the supporting type
     * @return  a CodeElement representing the supporting type
     * @throws IllegalArgumentException if the specified type is null
     */
    public CodeElement addSupportingType(Class<?> type) {
        CodeElement codeElement = new CodeElement(type);
        return addSupportingType(codeElement);
    }

    /**
     * Adds a supporting type to this Component.
     *
     * @param type  the fully qualified type name
     * @return  a CodeElement representing the supporting type
     * @throws IllegalArgumentException if the specified type is null
     */
    public CodeElement addSupportingType(String name, String type, String namespace) {
        CodeElement codeElement = new CodeElement(name, type, namespace);
        return addSupportingType(codeElement);
    }

    /**
     * Adds a supporting type to this Component.
     *
     * @param code a CodeElement representing the supporting type
     * @return  a CodeElement representing the supporting type
     * @throws IllegalArgumentException if the specified type is null
     */
    public CodeElement addSupportingType(CodeElement code) {
        code.setRole(CodeElementRole.Supporting);
        this.codeElements.add(code);

        return code;
    }

    /**
     * Gets the size of this Component (e.g. number of lines).
     *
     * @return  the size of this component, as a long
     */
    public long getSize() {
        return size;
    }

    /**
     * Sets the size of this component (e.g. number of lines).
     *
     * @param size  the size
     */
    public void setSize(long size) {
        this.size = size;
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