package com.structurizr.dsl;

import com.structurizr.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NoRelationshipParserTests extends AbstractTests {

    private static final String DEPLOYMENT_ENVIRONMENT = "live";

    private final NoRelationshipParser parser = new NoRelationshipParser();
    private DeploymentEnvironmentDslContext context;

    @BeforeEach
    void setUp() {
        context = new DeploymentEnvironmentDslContext(DEPLOYMENT_ENVIRONMENT);
        context.setWorkspace(workspace);
    }

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(null, tokens("source", "-/>", "destination", "description", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: <identifier> -/> <identifier> [description]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheDestinationIdentifierIsMissing() {
        try {
            parser.parse(null, tokens("source", "-/>"));
            fail();
        } catch (Exception e) {
            assertEquals("Not enough tokens, expected: <identifier> -/> <identifier> [description]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheSourceElementIsNotDefined() {
        try {
            parser.parse(context, tokens("a", "-/>", "b"));
            fail();
        } catch (Exception e) {
            assertEquals("The source element \"a\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheSourceElementIsNotAStaticStructureElementInstance() {
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("a", model.addPerson("User"));
        context.setIdentifierRegister(elements);

        try {
            parser.parse(context, tokens("a", "->", "b"));
            fail();
        } catch (Exception e) {
            assertEquals("The source element \"a\" is not valid - expecting a software system, software system instance, container, or container instance", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheDestinationElementIsNotDefined() {
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("a", model.addSoftwareSystem("A"));
        context.setIdentifierRegister(elements);

        try {
            parser.parse(context, tokens("a", "-/>", "b"));
            fail();
        } catch (Exception e) {
            assertEquals("The destination element \"b\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheDestinationElementIsNotAStaticStructureElementInstance() {
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("a", model.addSoftwareSystem("A"));
        elements.register("b", model.addPerson("User"));
        context.setIdentifierRegister(elements);

        try {
            parser.parse(context, tokens("a", "->", "b"));
            fail();
        } catch (Exception e) {
            assertEquals("The destination element \"b\" is not valid - expecting a software system, software system instance, container, or container instance", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenARelationshipDoesNotExist() {
        IdentifiersRegister elements = new IdentifiersRegister();

        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");

        DeploymentNode deploymentNode = model.addDeploymentNode(DEPLOYMENT_ENVIRONMENT, "Deployment Node", "Description", "Technology");
        deploymentNode.add(a);
        deploymentNode.add(b);

        elements.register("a", a);
        elements.register("b", b);
        context.setIdentifierRegister(elements);

        try {
            parser.parse(context, tokens("a", "-/>", "b"));
            fail();
        } catch (Exception e) {
            assertEquals("A relationship between \"a\" and \"b\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenARelationshipWithDescriptionDoesNotExist() {
        IdentifiersRegister elements = new IdentifiersRegister();

        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");

        DeploymentNode deploymentNode = model.addDeploymentNode(DEPLOYMENT_ENVIRONMENT, "Deployment Node", "Description", "Technology");
        deploymentNode.add(a);
        deploymentNode.add(b);

        elements.register("a", a);
        elements.register("b", b);
        context.setIdentifierRegister(elements);

        try {
            parser.parse(context, tokens("a", "-/>", "b", "Description"));
            fail();
        } catch (Exception e) {
            assertEquals("A relationship between \"a\" and \"b\" with description \"Description\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parse_RemovesAllRelationshipsBetweenSoftwareSystemInstances_WhenUsingSoftwareSystemInstanceIdentifiersAndNoDescription() {
        IdentifiersRegister elements = new IdentifiersRegister();

        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        a.uses(b, "Description 1", "Technology");
        a.uses(b, "Description 2", "Technology");

        DeploymentNode deploymentNode = model.addDeploymentNode(DEPLOYMENT_ENVIRONMENT, "Deployment Node", "Description", "Technology");
        SoftwareSystemInstance aInstance = deploymentNode.add(a);
        SoftwareSystemInstance bInstance = deploymentNode.add(b);
        Relationship relationship1 = aInstance.getEfferentRelationshipWith(bInstance, "Description 1");
        Relationship relationship2 = aInstance.getEfferentRelationshipWith(bInstance, "Description 2");

        elements.register("aInstance", aInstance);
        elements.register("bInstance", bInstance);
        context.setIdentifierRegister(elements);

        assertTrue(aInstance.hasEfferentRelationshipWith(bInstance, "Description 1"));
        assertTrue(aInstance.hasEfferentRelationshipWith(bInstance, "Description 2"));

        Set<Relationship> relationshipsRemoved = parser.parse(context, tokens("aInstance", "-/>", "bInstance"));

        assertFalse(aInstance.hasEfferentRelationshipWith(bInstance, "Description 1"));
        assertFalse(aInstance.hasEfferentRelationshipWith(bInstance, "Description 2"));
        assertEquals(2, relationshipsRemoved.size());
        assertTrue(relationshipsRemoved.contains(relationship1));
        assertTrue(relationshipsRemoved.contains(relationship2));
    }

    @Test
    void test_parse_RemovesAllRelationshipsBetweenSoftwareSystemInstances_WhenUsingSoftwareSystemIdentifiersAndNoDescription() {
        IdentifiersRegister elements = new IdentifiersRegister();

        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        a.uses(b, "Description 1", "Technology");
        a.uses(b, "Description 2", "Technology");

        DeploymentNode deploymentNode = model.addDeploymentNode(DEPLOYMENT_ENVIRONMENT, "Deployment Node", "Description", "Technology");
        SoftwareSystemInstance aInstance = deploymentNode.add(a);
        SoftwareSystemInstance bInstance = deploymentNode.add(b);
        Relationship relationship1 = aInstance.getEfferentRelationshipWith(bInstance, "Description 1");
        Relationship relationship2 = aInstance.getEfferentRelationshipWith(bInstance, "Description 2");

        elements.register("a", a);
        elements.register("b", b);
        context.setIdentifierRegister(elements);

        assertTrue(aInstance.hasEfferentRelationshipWith(bInstance, "Description 1"));
        assertTrue(aInstance.hasEfferentRelationshipWith(bInstance, "Description 2"));

        Set<Relationship> relationshipsRemoved = parser.parse(context, tokens("a", "-/>", "b"));

        assertFalse(aInstance.hasEfferentRelationshipWith(bInstance, "Description 1"));
        assertFalse(aInstance.hasEfferentRelationshipWith(bInstance, "Description 2"));
        assertEquals(2, relationshipsRemoved.size());
        assertTrue(relationshipsRemoved.contains(relationship1));
        assertTrue(relationshipsRemoved.contains(relationship2));
    }

    @Test
    void test_parse_RemovesAllRelationshipsBetweenContainerInstances_WhenUsingContainerIdentifiersAndNoDescription() {
        IdentifiersRegister elements = new IdentifiersRegister();

        SoftwareSystem ss = model.addSoftwareSystem("A");
        Container a = ss.addContainer("A");
        Container b = ss.addContainer("B");
        a.uses(b, "Description 1", "Technology");
        a.uses(b, "Description 2", "Technology");

        DeploymentNode deploymentNode = model.addDeploymentNode(DEPLOYMENT_ENVIRONMENT, "Deployment Node", "Description", "Technology");
        ContainerInstance aInstance = deploymentNode.add(a);
        ContainerInstance bInstance = deploymentNode.add(b);
        Relationship relationship1 = aInstance.getEfferentRelationshipWith(bInstance, "Description 1");
        Relationship relationship2 = aInstance.getEfferentRelationshipWith(bInstance, "Description 2");

        elements.register("a", a);
        elements.register("b", b);
        context.setIdentifierRegister(elements);

        assertTrue(aInstance.hasEfferentRelationshipWith(bInstance, "Description 1"));
        assertTrue(aInstance.hasEfferentRelationshipWith(bInstance, "Description 2"));

        Set<Relationship> relationshipsRemoved = parser.parse(context, tokens("a", "-/>", "b"));

        assertFalse(aInstance.hasEfferentRelationshipWith(bInstance, "Description 1"));
        assertFalse(aInstance.hasEfferentRelationshipWith(bInstance, "Description 2"));
        assertEquals(2, relationshipsRemoved.size());
        assertTrue(relationshipsRemoved.contains(relationship1));
        assertTrue(relationshipsRemoved.contains(relationship2));
    }

    @Test
    void test_parse_RemovesTheRelationshipBetweenSoftwareSystemInstances_WhenUsingSoftwareSystemInstanceIdentifiersAndADescription() {
        IdentifiersRegister elements = new IdentifiersRegister();

        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        a.uses(b, "Description 1", "Technology");
        a.uses(b, "Description 2", "Technology");

        DeploymentNode deploymentNode = model.addDeploymentNode(DEPLOYMENT_ENVIRONMENT, "Deployment Node", "Description", "Technology");
        SoftwareSystemInstance aInstance = deploymentNode.add(a);
        SoftwareSystemInstance bInstance = deploymentNode.add(b);
        Relationship relationship1 = aInstance.getEfferentRelationshipWith(bInstance, "Description 1");
        Relationship relationship2 = aInstance.getEfferentRelationshipWith(bInstance, "Description 2");

        elements.register("aInstance", aInstance);
        elements.register("bInstance", bInstance);
        context.setIdentifierRegister(elements);

        assertTrue(aInstance.hasEfferentRelationshipWith(bInstance, "Description 1"));
        assertTrue(aInstance.hasEfferentRelationshipWith(bInstance, "Description 2"));

        Set<Relationship> relationshipsRemoved = parser.parse(context, tokens("aInstance", "-/>", "bInstance", "Description 1"));

        assertFalse(aInstance.hasEfferentRelationshipWith(bInstance, "Description 1"));
        assertTrue(aInstance.hasEfferentRelationshipWith(bInstance, "Description 2"));
        assertEquals(1, relationshipsRemoved.size());
        assertTrue(relationshipsRemoved.contains(relationship1));
        assertFalse(relationshipsRemoved.contains(relationship2));
    }

    @Test
    void test_parse_RemovesTheRelationshipBetweenSoftwareSystemInstances_WhenUsingSoftwareSystemIdentifiersAndADescription() {
        IdentifiersRegister elements = new IdentifiersRegister();

        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        a.uses(b, "Description 1", "Technology");
        a.uses(b, "Description 2", "Technology");

        DeploymentNode deploymentNode = model.addDeploymentNode(DEPLOYMENT_ENVIRONMENT, "Deployment Node", "Description", "Technology");
        SoftwareSystemInstance aInstance = deploymentNode.add(a);
        SoftwareSystemInstance bInstance = deploymentNode.add(b);
        Relationship relationship1 = aInstance.getEfferentRelationshipWith(bInstance, "Description 1");
        Relationship relationship2 = aInstance.getEfferentRelationshipWith(bInstance, "Description 2");

        elements.register("a", a);
        elements.register("b", b);
        context.setIdentifierRegister(elements);

        assertTrue(aInstance.hasEfferentRelationshipWith(bInstance, "Description 1"));
        assertTrue(aInstance.hasEfferentRelationshipWith(bInstance, "Description 2"));

        Set<Relationship> relationshipsRemoved = parser.parse(context, tokens("a", "-/>", "b", "Description 1"));

        assertFalse(aInstance.hasEfferentRelationshipWith(bInstance, "Description 1"));
        assertTrue(aInstance.hasEfferentRelationshipWith(bInstance, "Description 2"));
        assertEquals(1, relationshipsRemoved.size());
        assertTrue(relationshipsRemoved.contains(relationship1));
        assertFalse(relationshipsRemoved.contains(relationship2));
    }

    @Test
    void test_parse_RemovesTheRelationshipBetweenContainerInstances_WhenUsingContainerIdentifiersAndADescription() {
        IdentifiersRegister elements = new IdentifiersRegister();

        SoftwareSystem ss = model.addSoftwareSystem("A");
        Container a = ss.addContainer("A");
        Container b = ss.addContainer("B");
        a.uses(b, "Description 1", "Technology");
        a.uses(b, "Description 2", "Technology");

        DeploymentNode deploymentNode = model.addDeploymentNode(DEPLOYMENT_ENVIRONMENT, "Deployment Node", "Description", "Technology");
        ContainerInstance aInstance = deploymentNode.add(a);
        ContainerInstance bInstance = deploymentNode.add(b);
        Relationship relationship1 = aInstance.getEfferentRelationshipWith(bInstance, "Description 1");
        Relationship relationship2 = aInstance.getEfferentRelationshipWith(bInstance, "Description 2");

        elements.register("a", a);
        elements.register("b", b);
        context.setIdentifierRegister(elements);

        assertTrue(aInstance.hasEfferentRelationshipWith(bInstance, "Description 1"));
        assertTrue(aInstance.hasEfferentRelationshipWith(bInstance, "Description 2"));

        Set<Relationship> relationshipsRemoved = parser.parse(context, tokens("a", "-/>", "b", "Description 1"));

        assertFalse(aInstance.hasEfferentRelationshipWith(bInstance, "Description 1"));
        assertTrue(aInstance.hasEfferentRelationshipWith(bInstance, "Description 2"));
        assertEquals(1, relationshipsRemoved.size());
        assertTrue(relationshipsRemoved.contains(relationship1));
        assertFalse(relationshipsRemoved.contains(relationship2));
    }

}