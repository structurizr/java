package com.structurizr.componentfinder.func;

import com.structurizr.model.Component;

import static org.assertj.core.api.Assertions.assertThat;

public class TagValidator {
    private TagValidator() {
    }


    static void validateTags(Component component, String... tagNames) {
        for (String tag : tagNames) {
            assertThat(component.getTags().contains(tag)).isTrue();
        }
    }

    static void validateTagsExactly(Component component, String... tagNames) {
        validateTags(component, tagNames);
        assertThat(nrOfAddedTags(component)).isEqualTo(tagNames.length);
    }

    private static int nrOfAddedTags(Component component) {
        final String[] allTags = component.getTags().split(",");
        return allTags.length - 2;
    }
}
