package com.structurizr.dsl;

import com.structurizr.model.Relationship;
import com.structurizr.view.ModelView;

abstract class ModelViewContentParser extends AbstractParser {

    protected static final String WILDCARD = "*";
    protected static final String WILDCARD_RELUCTANT = "*?";
    protected static final String ELEMENT_WILDCARD = "element==*";

    protected boolean isExpression(String token) {
        return ExpressionParser.isExpression(token.toLowerCase());
    }

    protected void removeRelationshipFromView(Relationship relationship, ModelView view) {
        view.remove(relationship);
    }

}