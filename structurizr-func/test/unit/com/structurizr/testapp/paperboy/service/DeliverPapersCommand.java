package com.structurizr.testapp.paperboy.service;

import com.google.common.collect.ImmutableSet;
import com.structurizr.testapp.paperboy.model.PaperBoy;

import java.util.Set;

import static com.google.common.base.Preconditions.checkState;

public class DeliverPapersCommand {
    private final Set<PaperBoy> paperBoys;
    private final Set<String> streets;


    private DeliverPapersCommand(Builder builder) {
        paperBoys = builder.paperBoys;
        streets = builder.streets;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Set<PaperBoy> getPaperBoys() {
        return paperBoys;
    }

    public Set<String> getStreets() {
        return streets;
    }

    public static final class Builder {
        private ImmutableSet.Builder<PaperBoy> paperBoyBuilder = ImmutableSet.builder();
        private ImmutableSet.Builder<String> streetsBuilder = ImmutableSet.builder();
        private Set<PaperBoy> paperBoys;
        private Set<String> streets;

        private Builder() {
        }

        public Builder withPaperBoys(Set<PaperBoy> val) {
            paperBoyBuilder.addAll(val);
            return this;
        }

        public Builder withStreets(Set<String> val) {
            streetsBuilder.addAll(val);
            return this;
        }

        public DeliverPapersCommand build() {
            paperBoys = paperBoyBuilder.build();
            streets = streetsBuilder.build();
            checkState(!paperBoys.isEmpty());
            checkState(!streets.isEmpty());
            return new DeliverPapersCommand(this);
        }
    }
}
