package com.structurizr.dsl;

abstract class ArchetypeDslContext extends DslContext {

    private final Archetype archetype;

    ArchetypeDslContext(Archetype archetype) {
        this.archetype = archetype;
    }

    Archetype getArchetype() {
        return archetype;
    }

}