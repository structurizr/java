package com.structurizr.documentation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.util.StringUtils;

import java.util.*;

/**
 * Represents the documentation within a workspace, software system, container, or component;
 *  a collection of content in Markdown or AsciiDoc format, optionally with attached images.
 *
 * See <a href="https://docs.structurizr.com/ui/documentation/">Documentation</a>
 * and <a href="https://docs.structurizr.com/ui/decisions/">Decisions</a> for more details.
 */
public final class Documentation {

    private List<Section> sections = new ArrayList<>();
    private Set<Decision> decisions = new TreeSet<>();
    private Set<Image> images = new TreeSet<>();

    public Documentation() {
    }

    /**
     * Adds a section to this documentation.
     *
     * @param section   a Section object
     */
    public void addSection(Section section) {
        checkFormatIsSpecified(section.getFormat());

        section.setOrder(calculateOrder());
        sections.add(section);
    }

    private void checkTitleIsSpecified(String title) {
        if (StringUtils.isNullOrEmpty(title)) {
            throw new IllegalArgumentException("A title must be specified.");
        }
    }

    private void checkContentIsSpecified(String content) {
        if (StringUtils.isNullOrEmpty(content)) {
            throw new IllegalArgumentException("Content must be specified.");
        }
    }

    private void checkFormatIsSpecified(Format format) {
        if (format == null) {
            throw new IllegalArgumentException("A format must be specified.");
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
    public Collection<Section> getSections() {
        return new ArrayList<>(sections);
    }

    void setSections(Collection<Section> sections) {
        if (sections != null) {
            this.sections = new ArrayList<>(sections);
        }
    }

    /**
     * Gets the set of decisions associated with this workspace.
     *
     * @return  a Set of Decision objects
     */
    public Set<Decision> getDecisions() {
        return new TreeSet<>(decisions);
    }

    void setDecisions(Set<Decision> decisions) {
        if (decisions != null) {
            this.decisions = new TreeSet<>(decisions);
        }
    }

    /**
     * Adds a new decision to this documentation.
     *
     * @param decision      the Decision object
     */
    public void addDecision(Decision decision) {
        checkIdIsSpecified(decision.getId());
        checkTitleIsSpecified(decision.getTitle());
        checkContentIsSpecified(decision.getContent());
        checkDecisionStatusIsSpecified(decision.getStatus());
        checkFormatIsSpecified(decision.getFormat());
        checkDecisionIsUnique(decision.getId());

        this.decisions.add(decision);
    }

    private void checkIdIsSpecified(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            throw new IllegalArgumentException("An ID must be specified.");
        }
    }

    private void checkDecisionStatusIsSpecified(String status) {
        if (status == null) {
            throw new IllegalArgumentException("A status must be specified.");
        }
    }

    private void checkDecisionIsUnique(String id) {
        for (Decision decision : decisions) {
            if (id.equals(decision.getId())) {
                throw new IllegalArgumentException("A decision with an ID of " + id + " already exists in this scope.");
            }
        }
    }

    /**
     * Adds an image to the documentation.
     *
     * @param image     an Image object
     */
    public void addImage(Image image) {
        images.add(image);
    }

    /**
     * Gets the set of {@link Image}s in this workspace.
     *
     * @return  a Set of {@link Image} objects
     */
    public Set<Image> getImages() {
        return new TreeSet<>(images);
    }

    void setImages(Set<Image> images) {
        if (images != null) {
            this.images = new TreeSet<>(images);
        }
    }

    @JsonIgnore
    public boolean isEmpty() {
        return sections.isEmpty() && images.isEmpty() && decisions.isEmpty();
    }

    /**
     * Removes all documentation, decisions, and images.
     */
    public void clear() {
        sections = new ArrayList<>();
        decisions = new TreeSet<>();
        images = new TreeSet<>();
    }

}