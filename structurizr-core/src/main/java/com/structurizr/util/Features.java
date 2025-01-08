package com.structurizr.util;

import java.util.HashMap;
import java.util.Map;

public class Features {

    private final Map<String,Boolean> features = new HashMap<>();

    public void enable(String feature) {
        features.put(feature, true);
    }

    public void disable(String feature) {
        features.put(feature, false);
    }

    public void configure(String feature, boolean enabled) {
        features.put(feature, enabled);
    }

    public boolean isEnabled(String feature) {
        return features.getOrDefault(feature, false);
    }

}