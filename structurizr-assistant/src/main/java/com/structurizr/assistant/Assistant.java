package com.structurizr.assistant;

import java.util.ArrayList;
import java.util.Collection;

abstract class Assistant {

    private final Collection<Recommendation> recommendations = new ArrayList<>();

    public Collection<Recommendation> getRecommendations() {
        return new ArrayList<>(recommendations);
    }

    protected void add(Recommendation recommendation) {
        if (recommendation != null) {
            recommendations.add(recommendation);
        }
    }

}