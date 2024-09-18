package com.structurizr.component;

import java.util.*;

final class DiscoveredComponent {

    private final Type primaryType;
    private final String name;
    private String description;
    private String technology;
    private String url;
    private final List<String> tags = new ArrayList<>();
    private final Map<String, String> properties = new HashMap<>();
    private final Set<Type> supportingTypes = new HashSet<>();

    private ComponentFinderStrategy componentFinderStrategy;

    DiscoveredComponent(String name, Type primaryType) {
        this.name = name;
        this.primaryType = primaryType;
    }

    void addSupportingTypes(Set<Type> types) {
        supportingTypes.addAll(types);
    }

    Type getPrimaryType() {
        return primaryType;
    }

    String getName() {
        return this.name;
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    String getTechnology() {
        return technology;
    }

    void setTechnology(String technology) {
        this.technology = technology;
    }

    String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    void addTags(List<String> tags) {
        this.tags.addAll(tags);
    }

    List<String> getTags() {
        return List.copyOf(tags);
    }

    void addProperty(String key, String value) {
        properties.put(key, value);
    }

    Map<String, String> getProperties() {
        return Map.copyOf(properties);
    }

    Set<Type> getSupportingTypes() {
        return new HashSet<>(supportingTypes);
    }

    Set<Type> getAllTypes() {
        Set<Type> types = new HashSet<>();

        types.add(primaryType);
        types.addAll(supportingTypes);

        return types;
    }

    Set<Type> getAllDependencies() {
        Set<Type> dependencies = new HashSet<>();

        for (Type type : getAllTypes()) {
            dependencies.addAll(type.getDependencies());
        }

        return dependencies;
    }

    ComponentFinderStrategy getComponentFinderStrategy() {
        return componentFinderStrategy;
    }

    void setComponentFinderStrategy(ComponentFinderStrategy componentFinderStrategy) {
        this.componentFinderStrategy = componentFinderStrategy;
    }

    @Override
    public String toString() {
        return "DiscoveredComponent{" +
                "name='" + name + '\'' +
                '}';
    }

}