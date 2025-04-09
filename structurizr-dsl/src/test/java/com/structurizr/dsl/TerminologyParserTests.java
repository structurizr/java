package com.structurizr.dsl;

import com.structurizr.view.MetadataSymbols;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class TerminologyParserTests extends AbstractTests {

    private TerminologyParser parser = new TerminologyParser();

    @Test
    void test_parsePerson_ThrowsAnException_WhenNoTermIsSpecified() {
        try {
            parser.parsePerson(context(), tokens("person"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: person <term>", e.getMessage());
        }
    }

    @Test
    void test_parsePerson_SetsTheTerm_WhenOneIsSpecified() {
        parser.parsePerson(context(), tokens("person", "TERM"));

        assertEquals("TERM", workspace.getViews().getConfiguration().getTerminology().getPerson());
    }
    
    @Test
    void test_parseSoftwareSystem_ThrowsAnException_WhenNoTermIsSpecified() {
        try {
            parser.parseSoftwareSystem(context(), tokens("softwareSystem"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: softwareSystem <term>", e.getMessage());
        }
    }

    @Test
    void test_parseSoftwareSystem_SetsTheTerm_WhenOneIsSpecified() {
        parser.parseSoftwareSystem(context(), tokens("softwareSystem", "TERM"));

        assertEquals("TERM", workspace.getViews().getConfiguration().getTerminology().getSoftwareSystem());
    }

    @Test
    void test_parseContainer_ThrowsAnException_WhenNoTermIsSpecified() {
        try {
            parser.parseContainer(context(), tokens("container"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: container <term>", e.getMessage());
        }
    }

    @Test
    void test_parseContainer_SetsTheTerm_WhenOneIsSpecified() {
        parser.parseContainer(context(), tokens("container", "TERM"));

        assertEquals("TERM", workspace.getViews().getConfiguration().getTerminology().getContainer());
    }

    @Test
    void test_parseComponent_ThrowsAnException_WhenNoTermIsSpecified() {
        try {
            parser.parseComponent(context(), tokens("component"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: component <term>", e.getMessage());
        }
    }

    @Test
    void test_parseComponent_SetsTheTerm_WhenOneIsSpecified() {
        parser.parseComponent(context(), tokens("component", "TERM"));

        assertEquals("TERM", workspace.getViews().getConfiguration().getTerminology().getComponent());
    }

    @Test
    void test_parsedeploymentNode_ThrowsAnException_WhenNoTermIsSpecified() {
        try {
            parser.parseDeploymentNode(context(), tokens("deploymentNode"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: deploymentNode <term>", e.getMessage());
        }
    }

    @Test
    void test_parsedeploymentNode_SetsTheTerm_WhenOneIsSpecified() {
        parser.parseDeploymentNode(context(), tokens("deploymentNode", "TERM"));

        assertEquals("TERM", workspace.getViews().getConfiguration().getTerminology().getDeploymentNode());
    }

    @Test
    void test_parseInfrastructureNode_ThrowsAnException_WhenNoTermIsSpecified() {
        try {
            parser.parseInfrastructureNode(context(), tokens("infrastructureNode"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: infrastructureNode <term>", e.getMessage());
        }
    }

    @Test
    void test_parseInfrastructureNode_SetsTheTerm_WhenOneIsSpecified() {
        parser.parseInfrastructureNode(context(), tokens("infrastructureNode", "TERM"));

        assertEquals("TERM", workspace.getViews().getConfiguration().getTerminology().getInfrastructureNode());
    }

    @Test
    void test_parseRelationship_ThrowsAnException_WhenNoTermIsSpecified() {
        try {
            parser.parseRelationship(context(), tokens("relationship"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: relationship <term>", e.getMessage());
        }
    }

    @Test
    void test_parseRelationship_SetsTheTerm_WhenOneIsSpecified() {
        parser.parseRelationship(context(), tokens("relationship", "TERM"));

        assertEquals("TERM", workspace.getViews().getConfiguration().getTerminology().getRelationship());
    }

    @Test
    void test_parseMetadataSymbols_ThrowsAnException_WhenNoSymbolTypeIsSpecified() {
        try {
            parser.parseMetadataSymbols(context(), tokens("metadata"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: metadata <square|round|curly|angle|double-angle|none>", e.getMessage());
        }
    }

    @Test
    void test_parseMetadataSymbols_ThrowsAnException_WhenAnInvalidSymbolTypeIsSpecified() {
        try {
            parser.parseMetadataSymbols(context(), tokens("metadata", "invalid"));
            fail();
        } catch (Exception e) {
            assertEquals("The symbol type \"invalid\" is not valid", e.getMessage());
        }
    }

    @Test
    void test_parseMetadataSymbols_SetsTheMetadataSymbols_WhenSpecified() {
        parser.parseMetadataSymbols(context(), tokens("metadata", "square"));
        assertEquals(MetadataSymbols.SquareBrackets, workspace.getViews().getConfiguration().getMetadataSymbols());

        parser.parseMetadataSymbols(context(), tokens("metadata", "round"));
        assertEquals(MetadataSymbols.RoundBrackets, workspace.getViews().getConfiguration().getMetadataSymbols());

        parser.parseMetadataSymbols(context(), tokens("metadata", "curly"));
        assertEquals(MetadataSymbols.CurlyBrackets, workspace.getViews().getConfiguration().getMetadataSymbols());

        parser.parseMetadataSymbols(context(), tokens("metadata", "angle"));
        assertEquals(MetadataSymbols.AngleBrackets, workspace.getViews().getConfiguration().getMetadataSymbols());

        parser.parseMetadataSymbols(context(), tokens("metadata", "double-angle"));
        assertEquals(MetadataSymbols.DoubleAngleBrackets, workspace.getViews().getConfiguration().getMetadataSymbols());

        parser.parseMetadataSymbols(context(), tokens("metadata", "none"));
        assertEquals(MetadataSymbols.None, workspace.getViews().getConfiguration().getMetadataSymbols());
    }

}