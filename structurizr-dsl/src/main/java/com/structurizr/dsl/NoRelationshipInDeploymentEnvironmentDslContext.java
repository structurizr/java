package com.structurizr.dsl;

import com.structurizr.model.Relationship;

final class NoRelationshipInDeploymentEnvironmentDslContext extends DeploymentEnvironmentDslContext {

    private final Relationship relationship;

    NoRelationshipInDeploymentEnvironmentDslContext(DeploymentEnvironmentDslContext parent, Relationship relationship) {
        super(parent.getEnvironment().getName(), parent.getGroup());

        this.relationship = relationship;
    }

    Relationship getRelationship() {
        return relationship;
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.RELATIONSHIP_TOKEN
        };
    }

}