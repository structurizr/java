package com.structurizr.componentfinder.func;

import com.structurizr.model.Component;

import static org.assertj.core.api.Assertions.assertThat;

public class TagValidator {
    private TagValidator() {
    }

    static void validateTag(Component component, String... tagNames) {
        assertThat(nrOfAddedTags(component)).isEqualTo(tagNames.length);
        for (String tag : tagNames) {
            assertThat(component.hasTag(tag)).isTrue();
        }
    }

    private static int nrOfAddedTags(Component component) {
        final String[] allTags = component.getTags().split(",");
        return allTags.length - 2;
    }
}
