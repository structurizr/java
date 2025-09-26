package com.structurizr.dsl;

import com.structurizr.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InstanceOfParserTests extends AbstractTests {

    private final InstanceOfParser parser = new InstanceOfParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(new DeploymentNodeDslContext(null), tokens("instanceOf", "identifier", "deploymentGroups", "tags", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: instanceOf <identifier> [deploymentGroups] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheIdentifierIsNotSpecified() {
        try {
            parser.parse(new DeploymentNodeDslContext(null), tokens("instanceOf"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: instanceOf <identifier> [deploymentGroups] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheElementDoesNotExist() {
        try {
            parser.parse(new DeploymentNodeDslContext(null), tokens("instanceOf", "softwareSystem"));
            fail();
        } catch (Exception e) {
            assertEquals("The element \"softwareSystem\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheElementIsNotASoftwareSystem() {
        DeploymentNodeDslContext context = new DeploymentNodeDslContext(null);
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("softwaresystem", model.addPerson("Name", "Description"));
        context.setIdentifierRegister(elements);

        try {
            parser.parse(context, tokens("instanceOf", "softwareSystem"));
            fail();
        } catch (Exception e) {
            assertEquals("The element \"softwareSystem\" must be a software system or a container", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesASoftwareSystemInstance() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        DeploymentNodeDslContext context = new DeploymentNodeDslContext(deploymentNode);
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("softwaresystem", softwareSystem);
        context.setIdentifierRegister(elements);

        parser.parse(context, tokens("instanceOf", "softwareSystem"));

        assertEquals(3, model.getElements().size());
        assertEquals(1, deploymentNode.getSoftwareSystemInstances().size());
        SoftwareSystemInstance softwareSystemInstance = deploymentNode.getSoftwareSystemInstances().iterator().next();
        assertSame(softwareSystem, softwareSystemInstance.getSoftwareSystem());
        assertEquals("Software System Instance", softwareSystemInstance.getTags());
        assertEquals("Live", softwareSystemInstance.getEnvironment());
        assertEquals(1, softwareSystemInstance.getDeploymentGroups().size());
        assertEquals("Default", softwareSystemInstance.getDeploymentGroups().iterator().next());
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheElementIsNotAContainer() {
        DeploymentNodeDslContext context = new DeploymentNodeDslContext(null);
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("container", model.addPerson("Name", "Description"));
        context.setIdentifierRegister(elements);

        try {
            parser.parse(context, tokens("instanceOf", "container"));
            fail();
        } catch (Exception e) {
            assertEquals("The element \"container\" must be a software system or a container", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesAContainerInstance() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        DeploymentNodeDslContext context = new DeploymentNodeDslContext(deploymentNode);
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("container", container);
        context.setIdentifierRegister(elements);

        parser.parse(context, tokens("instanceOf", "container"));

        assertEquals(4, model.getElements().size());
        assertEquals(1, deploymentNode.getContainerInstances().size());
        ContainerInstance containerInstance = deploymentNode.getContainerInstances().iterator().next();
        assertSame(container, containerInstance.getContainer());
        assertEquals("Container Instance", containerInstance.getTags());
        assertEquals("Live", containerInstance.getEnvironment());
        assertEquals(1, containerInstance.getDeploymentGroups().size());
        assertEquals("Default", containerInstance.getDeploymentGroups().iterator().next());
    }

}