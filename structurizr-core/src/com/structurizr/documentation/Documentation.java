package com.structurizr.documentation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Element;
import com.structurizr.model.Model;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents the documentation within a workspace - a collection of
 * content in Markdown or AsciiDoc format, optionally with attached images.
 *
 * See <a href="https://structurizr.com/help/documentation">Documentation</a>
 * on the Structurizr website for more details.
 */
public final class Documentation {

    private Model model;
    private Set<Section> sections = new HashSet<>();
    private Set<Image> images = new HashSet<>();
    private TemplateMetadata template;

    Documentation() {
    }

    public Documentation(Model model) {
        if (model == null) {
            throw new IllegalArgumentException("A model must be specified.");
        }

        this.model = model;
    }

    @JsonIgnore
    Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    final Section addSection(Element element, String type, int group, Format format, String content) {
        if (group < 1) {
            group = 1;
        } else if (group > 5) {
            group = 5;
        }

        Section section = new Section(element, type, calculateOrder(), group, format, content);
        if (!sections.contains(section)) {
            sections.add(section);
            return section;
        } else {
            throw new IllegalArgumentException("A section of type " + type +
                    (element != null ? " for " + element.getName() : "")
                    + " already exists.");
        }
    }

    private int calculateOrder() {
        return sections.size()+1;
    }

    /**
     * Gets the set of {@link Section}s.
     *
     * @return  a Set of {@link Section} objects
     */
    public Set<Section> getSections() {
        return new HashSet<>(sections);
    }

    void setSections(Set<Section> sections) {
        this.sections = sections;
    }

    void addImage(Image image) {
        images.add(image);
    }

    /**
     * Gets the set of {@link Image}s in this workspace.
     *
     * @return  a Set of {@link Image} objects
     */
    public Set<Image> getImages() {
        return new HashSet<>(images);
    }

    void setImages(Set<Image> images) {
        this.images = images;
    }

    public void hydrate() {
        for (Section section : sections) {
            if (section.getElementId() != null && section.getElementId().trim().length() > 0) {
                section.setElement(model.getElement(section.getElementId()));
            }
        }
    }

    @JsonIgnore
    public boolean isEmpty() {
        return sections.isEmpty() && images.isEmpty();
    }

    /**
     * Gets the template metadata associated with this documentation.
     *
     * @return  a TemplateMetadata object, or null if there is none
     */
    public TemplateMetadata getTemplate() {
        return template;
    }

    void setTemplate(TemplateMetadata template) {
        this.template = template;
    }

}