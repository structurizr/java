package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

/**
 * Represents a "component" in the C4 model.
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
    public CodeElement getType() {
        return codeElements.stream().filter(ce -> ce.getRole() == CodeElementRole.Primary).findFirst().orElse(null);
    }

    /**
     * Sets the type of this component (e.g. a fully qualified Java interface/class name).
     *
     * @param type  the fully qualified type name
     * @return  the CodeElement that was created
     * @throws IllegalArgumentException if the specified type is null
     */
    public CodeElement setType(String type) {
        Optional<CodeElement> optional = codeElements.stream().filter(ce -> ce.getRole() == CodeElementRole.Primary).findFirst();
        optional.ifPresent(codeElement -> codeElements.remove(codeElement));

        CodeElement codeElement = new CodeElement(type);
        codeElement.setRole(CodeElementRole.Primary);
        this.codeElements.add(codeElement);

        return codeElement;
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
     * @param type  the fully qualified type name
     * @return  a CodeElement representing the supporting type
     * @throws IllegalArgumentException if the specified type is null
     */
    public CodeElement addSupportingType(String type) {
        CodeElement codeElement = new CodeElement(type);
        codeElement.setRole(CodeElementRole.Supporting);
        this.codeElements.add(codeElement);

        return codeElement;
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

    /**
     * Gets the canonical name of this component, in the form "/Software System/Container/Component".
     *
     * @return  the canonical name, as a String
     */
    @Override
    public String getCanonicalName() {
        return getParent().getCanonicalName() + CANONICAL_NAME_SEPARATOR + formatForCanonicalName(getName());
    }

    @Override
    protected Set<String> getRequiredTags() {
        return new LinkedHashSet<>(Arrays.asList(Tags.ELEMENT, Tags.COMPONENT));
    }

}