package com.structurizr.dsl;

import com.structurizr.PerspectivesHolder;
import com.structurizr.model.ModelItem;

import java.util.ArrayList;
import java.util.Collection;

final class PerspectivesDslContext extends DslContext {

    private final Collection<PerspectivesHolder> perspectivesHolders = new ArrayList<>();

    PerspectivesDslContext(PerspectivesHolder perspectivesHolder) {
        this.perspectivesHolders.add(perspectivesHolder);
    }

    PerspectivesDslContext(Collection<? extends PerspectivesHolder> perspectivesHolders) {
        this.perspectivesHolders.addAll(perspectivesHolders);
    }

    Collection<PerspectivesHolder> getPerspectivesHolders() {
        return this.perspectivesHolders;
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[0];
    }

}