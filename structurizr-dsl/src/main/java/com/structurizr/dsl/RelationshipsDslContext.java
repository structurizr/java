package com.structurizr.dsl;

import com.structurizr.model.ModelItem;
import com.structurizr.model.Relationship;

import java.util.Set;
import java.util.stream.Collectors;

class RelationshipsDslContext extends ModelItemsDslContext {

    private final Set<Relationship> relationships;

    RelationshipsDslContext(DslContext parentDslContext, Set<Relationship> relationships) {
        super(parentDslContext);
        this.relationships = relationships;
    }

    Set<Relationship> getRelationships() {
        return relationships;
    }

    @Override
    Set<ModelItem> getModelItems() {
        return relationships.stream().map(e -> (ModelItem)e).collect(Collectors.toSet());
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.TAG_TOKEN,
                StructurizrDslTokens.TAGS_TOKEN,
                StructurizrDslTokens.URL_TOKEN,
                StructurizrDslTokens.PROPERTIES_TOKEN,
                StructurizrDslTokens.PERSPECTIVES_TOKEN
        };
    }

}