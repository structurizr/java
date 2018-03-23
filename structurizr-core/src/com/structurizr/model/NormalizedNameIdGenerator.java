package com.structurizr.model;

import java.util.HashSet;
import java.util.Set;

/**
 * An ID generator that uses the element name to generate IDs for model elements and relationships.
 * The canonical name will be normalized by removing any characters other than letters and numbers.
 */
public class NormalizedNameIdGenerator implements IdGenerator {
    
    private final boolean canonical;

    private Set<String> usedIds = new HashSet<>();

    /**
     * Creates a new ID generator, where element IDs are based upon the canonical name of the element.
     */
    public NormalizedNameIdGenerator() {
        this(true);
    }

    /**
     * Creates a new ID generator.
     *
     * @param canonical     {@code true} will use the {@link Element#getCanonicalName()}, {@code false} will use {@link Element#getName()}
     */
    public NormalizedNameIdGenerator(boolean canonical) {
        this.canonical = canonical;
    }

    @Override
    public void found(String id) {
        usedIds.add(id); // Remember as used to avoid clashes
    }

    @Override
    public String generateId(Element element) {
        if (!canonical && element instanceof ContainerInstance) { // Special treatment
            ContainerInstance containerInstance = (ContainerInstance) element;
            return generateId(containerInstance.getContainerId() + containerInstance.getInstanceId());
        } else {
            return generateId(canonical ? element.getCanonicalName() : element.getName());
        }
    }

    @Override
    public String generateId(Relationship relationship) {
        return generateId(
            canonical ? relationship.getSource().getCanonicalName() : relationship.getSource().getName(),
            relationship.getDescription(),
            canonical ? relationship.getDestination().getCanonicalName() : relationship.getDestination().getName());
    }

    private String generateId(final String...terms) {
        final StringBuilder sb = new StringBuilder();
        for (final String term : terms) {
            if (term != null) {
                sb.append(term.replaceAll("[^a-zA-Z0-9]", ""));
            }
        }

        final String id = sb.toString();
        if (usedIds.contains(id)) {
            throw new IllegalArgumentException("Non-unique ID generated: " + id);
        } else {
            usedIds.add(id);
            return id;
        }
    }
}