package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ImpliedRelationshipsParserTests extends AbstractTests {

    private ImpliedRelationshipsParser parser = new ImpliedRelationshipsParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("!impliedRelationships", "boolean", "extra"), null, false);
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: !impliedRelationships <true|false|fqcn>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenNoFlagIsSpecified() {
        try {
            parser.parse(context(), tokens("!impliedRelationships"), null, false);
            fail();
        } catch (Exception e) {
            assertEquals("Expected: !impliedRelationships <true|false|fqcn>", e.getMessage());
        }
    }

    @Test
    void test_parse_SetsTheStrategy_WhenFalseIsSpecified() {
        parser.parse(context(), tokens("!impliedRelationships", "false"), null, false);

        assertEquals("com.structurizr.model.DefaultImpliedRelationshipsStrategy", workspace.getModel().getImpliedRelationshipsStrategy().getClass().getCanonicalName());
    }

    @Test
    void test_parse_SetsTheStrategy_WhenTrueIsSpecified() {
        parser.parse(context(), tokens("!impliedRelationships", "true"), null, false);

        assertEquals("com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy", workspace.getModel().getImpliedRelationshipsStrategy().getClass().getCanonicalName());
    }

    @Test
    void test_parse_SetsTheStrategy_WhenABuiltInStrategyIsUsedInUnrestrictedMode() {
        parser.parse(context(), tokens("!impliedRelationships", "com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy"), new File("."), false);

        assertEquals("com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy", workspace.getModel().getImpliedRelationshipsStrategy().getClass().getCanonicalName());
    }

    @Test
    void test_parse_SetsTheStrategy_WhenABuiltInStrategyIsUsedInRestrictedMode() {
        parser.parse(context(), tokens("!impliedRelationships", "com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy"), new File("."), true);

        assertEquals("com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy", workspace.getModel().getImpliedRelationshipsStrategy().getClass().getCanonicalName());
    }

    @Test
    void test_parse_ThrowsAnException_WhenACustomStrategyIsUsedInRestrictedMode() {
        try {
            parser.parse(context(), tokens("!impliedRelationships", "com.example.CustomImpliedRelationshipsStrategy"), new File("."), true);
            fail();
        } catch (Exception e) {
            assertEquals("The implied relationships strategy com.example.CustomImpliedRelationshipsStrategy is not available when the DSL parser is running in restricted mode", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenACustomStrategyIsUsedInUnrestrictedModeButCannotBeLoaded() {
        try {
            parser.parse(context(), tokens("!impliedRelationships", "com.example.CustomImpliedRelationshipsStrategy"), new File("."), false);
            fail();
        } catch (Exception e) {
            assertEquals("Error loading implied relationships strategy: com.example.CustomImpliedRelationshipsStrategy was not found", e.getMessage());
        }
    }

}