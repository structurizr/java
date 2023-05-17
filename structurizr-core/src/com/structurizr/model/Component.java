package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.documentation.Documentable;
import com.structurizr.documentation.Documentation;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents a "component" in the C4 model.
 */
public final class Component extends StaticStructureElement implements Documentable {

    private Container parent;

    private String technology;

    private Documentation documentation = new Documentation();

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
     * Gets the canonical name of this component, in the form "/Software System/Container/Component".
     *
     * @return  the canonical name, as a String
     */
    @Override
    public String getCanonicalName() {
        return new CanonicalNameGenerator().generate(this);
    }

    @Nonnull
    @Override
    public Set<String> getDefaultTags() {
        return new LinkedHashSet<>(Arrays.asList(Tags.ELEMENT, Tags.COMPONENT));
    }

    /**
     * Gets the documentation associated with this component.
     *
     * @return  a Documentation object
     */
    public Documentation getDocumentation() {
        return documentation;
    }

    /**
     * Sets the documentation associated with this component.
     *
     * @param documentation     a Documentation object
     */
    void setDocumentation(Documentation documentation) {
        this.documentation = documentation;
    }

}
