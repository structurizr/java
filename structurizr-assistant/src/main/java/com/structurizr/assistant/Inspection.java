package com.structurizr.assistant;

import com.structurizr.PropertyHolder;
import com.structurizr.Workspace;

public abstract class Inspection {

    private static final String STRUCTURIZR_RECOMMENDATIONS_PREFIX = "structurizr.recommendations.";

    private final Workspace workspace;

    protected Inspection(Workspace workspace) {
        this.workspace = workspace;
    }

    protected abstract String getType();

    protected Workspace getWorkspace() {
        return workspace;
    }

    protected boolean isEnabled(String type, PropertyHolder... propertyHolders) {
        String value = "true";

        for (PropertyHolder propertyHolder : propertyHolders) {
            if (propertyHolder != null) {
                value = propertyHolder.getProperties().getOrDefault(STRUCTURIZR_RECOMMENDATIONS_PREFIX + type, value);
            }
        }

        return !value.equalsIgnoreCase("false");
    }

    protected Recommendation noRecommendation() {
        return null;
    }

    protected Recommendation lowPriorityRecommendation(String description) {
        return new Recommendation(STRUCTURIZR_RECOMMENDATIONS_PREFIX + getType(), Recommendation.Priority.Low, description);
    }

    protected Recommendation mediumPriorityRecommendation(String description) {
        return new Recommendation(STRUCTURIZR_RECOMMENDATIONS_PREFIX + getType(), Recommendation.Priority.Medium, description);
    }

    protected Recommendation highPriorityRecommendation(String description) {
        return new Recommendation(STRUCTURIZR_RECOMMENDATIONS_PREFIX + getType(), Recommendation.Priority.High, description);
    }

}