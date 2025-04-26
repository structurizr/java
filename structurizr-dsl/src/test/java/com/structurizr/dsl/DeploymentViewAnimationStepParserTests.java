package com.structurizr.dsl;

import com.structurizr.model.DeploymentNode;
import com.structurizr.view.DeploymentView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class DeploymentViewAnimationStepParserTests extends AbstractTests {

    private DeploymentViewAnimationStepParser parser = new DeploymentViewAnimationStepParser();

    @Test
    void test_parseExplicit_ThrowsAnException_WhenElementsAreMissing() {
        try {
            parser.parse((DeploymentViewDslContext)null, tokens("animationStep"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: animationStep <identifier|element expression> [identifier|element expression...]", e.getMessage());
        }
    }

    @Test
    void test_parseImplicit_ThrowsAnException_WhenElementsAreMissing() {
        try {
            parser.parse((DeploymentViewAnimationDslContext)null, tokens());
            fail();
        } catch (Exception e) {
            assertEquals("Expected: <identifier|element expression> [identifier|element expression...]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheElementDoesNotExist() {
        DeploymentView view = workspace.getViews().createDeploymentView("key", "Description");

        DeploymentViewAnimationDslContext context = new DeploymentViewAnimationDslContext(view);
        IdentifiersRegister map = new IdentifiersRegister();
        context.setIdentifierRegister(map);

        try {
            parser.parse(context, tokens("dn"));
            fail();
        } catch (Exception e) {
            assertEquals("The element/relationship \"dn\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenNoAnimatableElementsAreFound() {
        DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Deployment Node");
        DeploymentView view = workspace.getViews().createDeploymentView("key", "Description");
        view.add(deploymentNode);

        DeploymentViewAnimationDslContext context = new DeploymentViewAnimationDslContext(view);
        IdentifiersRegister map = new IdentifiersRegister();
        map.register("dn", deploymentNode);
        context.setIdentifierRegister(map);

        try {
            parser.parse(context, tokens("dn"));
            fail();
        } catch (Exception e) {
            assertEquals("No software system instances, container instances, or infrastructure nodes were found", e.getMessage());
        }
    }

}