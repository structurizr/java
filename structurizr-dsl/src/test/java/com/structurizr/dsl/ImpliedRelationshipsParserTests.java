package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ImpliedRelationshipsParserTests extends AbstractTests {

    private final ImpliedRelationshipsParser parser = new ImpliedRelationshipsParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("!impliedRelationships", "boolean", "extra"), null);
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: !impliedRelationships <true|false|fqcn>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenNoFlagIsSpecified() {
        try {
            parser.parse(context(), tokens("!impliedRelationships"), null);
            fail();
        } catch (Exception e) {
            assertEquals("Expected: !impliedRelationships <true|false|fqcn>", e.getMessage());
        }
    }

    @Test
    void test_parse_SetsTheStrategy_WhenFalseIsSpecified() {
        parser.parse(context(), tokens("!impliedRelationships", "false"), null);

        assertEquals("com.structurizr.model.DefaultImpliedRelationshipsStrategy", workspace.getModel().getImpliedRelationshipsStrategy().getClass().getCanonicalName());
    }

    @Test
    void test_parse_SetsTheStrategy_WhenTrueIsSpecified() {
        parser.parse(context(), tokens("!impliedRelationships", "true"), null);

        assertEquals("com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy", workspace.getModel().getImpliedRelationshipsStrategy().getClass().getCanonicalName());
    }

    @Test
    void test_parse_SetsTheStrategy_WhenABuiltInStrategyIsUsedInUnrestrictedMode() {
        parser.parse(context(), tokens("!impliedRelationships", "com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy"), new File("."));

        assertEquals("com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy", workspace.getModel().getImpliedRelationshipsStrategy().getClass().getCanonicalName());
    }

    @Test
    void test_parse_SetsTheStrategy_WhenABuiltInStrategyIsUsedAndCustomStrategiesAreNotEnabled() {
        DslContext context = context();
        context.getFeatures().disable(Features.PLUGINS);
        parser.parse(context, tokens("!impliedRelationships", "com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy"), new File("."));

        assertEquals("com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy", workspace.getModel().getImpliedRelationshipsStrategy().getClass().getCanonicalName());
    }

    @Test
    void test_parse_ThrowsAnException_WhenACustomStrategyIsUsedAndCustomStrategiesAreNotEnabled() {
        try {
            DslContext context = context();
            context.getFeatures().disable(Features.PLUGINS);
            parser.parse(context, tokens("!impliedRelationships", "com.example.CustomImpliedRelationshipsStrategy"), new File("."));
            fail();
        } catch (Exception e) {
            assertEquals("The implied relationships strategy com.example.CustomImpliedRelationshipsStrategy is not available (feature structurizr.feature.dsl.plugins is not enabled)", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenACustomStrategyIsUsedButCannotBeLoaded() {
        try {
            DslContext context = context();
            context.getFeatures().enable(Features.PLUGINS);
            parser.parse(context, tokens("!impliedRelationships", "com.example.CustomImpliedRelationshipsStrategy"), new File("."));
            fail();
        } catch (Exception e) {
            assertEquals("Error loading implied relationships strategy: com.example.CustomImpliedRelationshipsStrategy was not found", e.getMessage());
        }
    }

    @Test
    void test_parse_SetsTheStrategy_WhenACustomStrategyIsUsed() {
        DslContext context = context();
        context.getFeatures().enable(Features.PLUGINS);
        parser.parse(context, tokens("!impliedRelationships", "com.structurizr.dsl.example.CustomImpliedRelationshipsStrategy"), new File("."));

        assertEquals("com.structurizr.dsl.example.CustomImpliedRelationshipsStrategy", workspace.getModel().getImpliedRelationshipsStrategy().getClass().getCanonicalName());
    }

}