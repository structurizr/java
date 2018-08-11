package com.structurizr.documentation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Element;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.util.StringUtils;

import javax.annotation.Nonnull;
import java.util.Date;
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
    private Set<Decision> decisions = new HashSet<>();
    private Set<Image> images = new HashSet<>();
    private TemplateMetadata template;

    Documentation() {
    }

    Documentation(@Nonnull Model model) {
        this.model = model;
    }

    @Nonnull
    final Section addSection(Element element, String title, Format format, String content) {
        if (element != null && !model.contains(element)) {
            throw new IllegalArgumentException("The element named " + element.getName() + " does not exist in the model associated with this documentation.");
        }

        checkTitleIsSpecified(title);
        checkSectionIsUnique(element, title);
        checkFormatIsSpecified(format);

        Section section = new Section(element, title, calculateOrder(), format, content);
        sections.add(section);
        return section;
    }

    private void checkTitleIsSpecified(String title) {
        if (StringUtils.isNullOrEmpty(title)) {
            throw new IllegalArgumentException("A title must be specified.");
        }
    }

    private void checkFormatIsSpecified(Format format) {
        if (format == null) {
            throw new IllegalArgumentException("A format must be specified.");
        }
    }

    private void checkSectionIsUnique(Element element, String title) {
        if (element == null) {
            for (Section section : sections) {
                if (section.getElement() == null && title.equals(section.getTitle())) {
                    throw new IllegalArgumentException("A section with a title of " + title + " already exists for this workspace.");
                }
            }
        } else {
            for (Section section : sections) {
                if (element.getId().equals(section.getElementId()) && title.equals(section.getTitle())) {
                    throw new IllegalArgumentException("A section with a title of " + title + " already exists for the element named " + element.getName() + ".");
                }
            }
        }
    }

    private int calculateOrder() {
        return sections.size() + 1;
    }

    /**
     * Gets the set of {@link Section}s.
     *
     * @return a Set of {@link Section} objects
     */
    public Set<Section> getSections() {
        return new HashSet<>(sections);
    }

    void setSections(Set<Section> sections) {
        if (sections != null) {
            this.sections.addAll(sections);
        }
    }

    /**
     * Gets the set of decisions associated with this workspace.
     *
     * @return  a Set of Decision objects
     */
    public Set<Decision> getDecisions() {
        return new HashSet<>(decisions);
    }

    void setDecisions(Set<Decision> decisions) {
        if (decisions != null) {
            this.decisions.addAll(decisions);
        }
    }

    /**
     * Adds a new decision to this workspace.
     *
     * @param id        the ID of the decision
     * @param date      the date of the decision
     * @param title     the title of the decision
     * @param status    the status of the decision
     * @param format    the format of the decision content
     * @param content   the content of the decision
     * @return  a Decision object
     */
    public Decision addDecision(String id, Date date, String title, DecisionStatus status, Format format, String content) {
        return addDecision(null, id, date, title, status, format, content);
    }

    /**
     * Adds a new decision to this workspace.
     *
     * @param softwareSystem    the SoftwareSystem to associate the decision with
     * @param id                the ID of the decision
     * @param date              the date of the decision
     * @param title             the title of the decision
     * @param status            the status of the decision
     * @param format            the format of the decision content
     * @param content           the content of the decision
     * @return  a Decision object
     */
    public Decision addDecision(SoftwareSystem softwareSystem, String id, Date date, String title, DecisionStatus status, Format format, String content) {
        checkIdIsSpecified(id);
        checkTitleIsSpecified(title);
        checkDecisionStatusIsSpecified(status);
        checkFormatIsSpecified(format);
        checkDecisionIsUnique(softwareSystem, id);

        Decision decision = new Decision(softwareSystem, id, date, title, status, format, content);
        this.decisions.add(decision);

        return decision;
    }

    private void checkIdIsSpecified(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            throw new IllegalArgumentException("An ID must be specified.");
        }
    }

    private void checkDecisionStatusIsSpecified(DecisionStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("A status must be specified.");
        }
    }

    private void checkDecisionIsUnique(Element element, String id) {
        if (element == null) {
            for (Decision decision : decisions) {
                if (decision.getElement() == null && id.equals(decision.getId())) {
                    throw new IllegalArgumentException("A decision with an ID of " + id + " already exists for this workspace.");
                }
            }
        } else {
            for (Decision decision : decisions) {
                if (element.getId().equals(decision.getElementId()) && id.equals(decision.getId())) {
                    throw new IllegalArgumentException("A decision with an ID of " + id + " already exists for the element named " + element.getName() + ".");
                }
            }
        }
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
        if (images != null) {
            this.images.addAll(images);
        }
    }

    void hydrate(Model model) {
        this.model = model;

        for (Section section : sections) {
            if (!StringUtils.isNullOrEmpty(section.getElementId())) {
                section.setElement(model.getElement(section.getElementId()));
            }
        }

        for (Decision decision : decisions) {
            if (!StringUtils.isNullOrEmpty(decision.getElementId())) {
                decision.setElement(model.getElement(decision.getElementId()));
            }
        }
    }

    @JsonIgnore
    public boolean isEmpty() {
        return sections.isEmpty() && images.isEmpty() && decisions.isEmpty();
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