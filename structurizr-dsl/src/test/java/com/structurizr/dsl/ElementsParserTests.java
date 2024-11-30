package com.structurizr.dsl;

import com.structurizr.model.*;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ElementsParserTests extends AbstractTests {

    private final ElementsParser parser = new ElementsParser();

    @Test
    void test_parseTechnology_ThrowsAnException_WhenNoTechnologyIsSpecified() {
        try {
            parser.parseTechnology(null, tokens("technology"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: technology <technology>", e.getMessage());
        }
    }

    @Test
    void test_parseTechnology() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");
        DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node");
        InfrastructureNode infrastructureNode = deploymentNode.addInfrastructureNode("Infrastructure Node");

        ElementsDslContext context = new ElementsDslContext(null, Set.of(softwareSystem, container, component, deploymentNode, infrastructureNode));

        parser.parseTechnology(context, tokens("technology", "Technology"));
        assertEquals("Technology", container.getTechnology());
        assertEquals("Technology", component.getTechnology());
        assertEquals("Technology", deploymentNode.getTechnology());
        assertEquals("Technology", infrastructureNode.getTechnology());
    }

    @Test
    void test_parseDescription_ThrowsAnException_WhenNoDescriptionIsSpecified() {
        try {
            parser.parseDescription(null, tokens("description"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: description <description>", e.getMessage());
        }
    }

    @Test
    void test_parseDescription() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");
        DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node");
        InfrastructureNode infrastructureNode = deploymentNode.addInfrastructureNode("Infrastructure Node");

        ElementsDslContext context = new ElementsDslContext(null, Set.of(softwareSystem, container, component, deploymentNode, infrastructureNode));

        parser.parseDescription(context, tokens("description", "Description"));
        assertEquals("Description", softwareSystem.getDescription());
        assertEquals("Description", container.getDescription());
        assertEquals("Description", component.getDescription());
        assertEquals("Description", deploymentNode.getDescription());
        assertEquals("Description", infrastructureNode.getDescription());
    }

}