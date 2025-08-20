package com.structurizr.dsl;

import com.structurizr.model.*;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ExplicitRelationshipParserTests extends AbstractTests {

    private final ExplicitRelationshipParser parser = new ExplicitRelationshipParser();
    private Archetype archetype = new Archetype("name", "type");

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("source", "->", "destination", "description", "technology", "tags", "extra"), archetype);
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: <identifier> -> <identifier> [description] [technology] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheDestinationIdentifierIsMissing() {
        try {
            parser.parse(context(), tokens("source", "->"), archetype);
            fail();
        } catch (Exception e) {
            assertEquals("Expected: <identifier> -> <identifier> [description] [technology] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheSourceElementIsNotDefined() {
        try {
            parser.parse(context(), tokens("source", "->", "destination"), archetype);
            fail();
        } catch (Exception e) {
            assertEquals("The source element \"source\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheDestinationElementIsNotDefined() {
        DslContext context = context();
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("source", model.addPerson("User", "Description"));
        context.setIdentifierRegister(elements);

        try {
            parser.parse(context, tokens("source", "->", "destination"), archetype);
            fail();
        } catch (Exception e) {
            assertEquals("The destination element \"destination\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parse_AddsTheRelationship() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        DslContext context = context();

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("source", user);
        elements.register("destination", softwareSystem);
        context.setIdentifierRegister(elements);

        assertEquals(0, model.getRelationships().size());

        parser.parse(context, tokens("source", "->", "destination"), archetype);

        assertEquals(1, model.getRelationships().size());
        Relationship r = model.getRelationships().iterator().next();
        assertSame(user, r.getSource());
        assertSame(softwareSystem, r.getDestination());
        assertEquals("", r.getDescription());
        assertEquals("", r.getTechnology());
        assertEquals("Relationship", r.getTags());
    }

    @Test
    void test_parse_AddsTheRelationshipWithADescription() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        DslContext context = context();

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("source", user);
        elements.register("destination", softwareSystem);
        context.setIdentifierRegister(elements);

        assertEquals(0, model.getRelationships().size());

        parser.parse(context, tokens("source", "->", "destination", "Uses"), archetype);

        assertEquals(1, model.getRelationships().size());
        Relationship r = model.getRelationships().iterator().next();
        assertSame(user, r.getSource());
        assertSame(softwareSystem, r.getDestination());
        assertEquals("Uses", r.getDescription());
        assertEquals("", r.getTechnology());
        assertEquals("Relationship", r.getTags());
    }

    @Test
    void test_parse_AddsTheRelationshipWithADescriptionAndTechnology() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        DslContext context = context();

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("source", user);
        elements.register("destination", softwareSystem);
        context.setIdentifierRegister(elements);

        assertEquals(0, model.getRelationships().size());

        parser.parse(context, tokens("source", "->", "destination", "Uses", "HTTP"), archetype);

        assertEquals(1, model.getRelationships().size());
        Relationship r = model.getRelationships().iterator().next();
        assertSame(user, r.getSource());
        assertSame(softwareSystem, r.getDestination());
        assertEquals("Uses", r.getDescription());
        assertEquals("HTTP", r.getTechnology());
    }

    @Test
    void test_parse_AddsTheRelationshipWithADescriptionAndTechnologyAndTags() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        DslContext context = context();

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("source", user);
        elements.register("destination", softwareSystem);
        context.setIdentifierRegister(elements);

        assertEquals(0, model.getRelationships().size());

        parser.parse(context, tokens("source", "->", "destination", "Uses", "HTTP", "Tag 1,Tag 2"), archetype);

        assertEquals(1, model.getRelationships().size());
        Relationship r = model.getRelationships().iterator().next();
        assertSame(user, r.getSource());
        assertSame(softwareSystem, r.getDestination());
        assertEquals("Uses", r.getDescription());
        assertEquals("HTTP", r.getTechnology());
        assertEquals("Relationship,Tag 1,Tag 2", r.getTags());
    }

    @Test
    void test_parse_AddsTheRelationshipWithADescriptionAndTechnologyAndTagsBasedUponAnArchetype() {
        archetype = new Archetype("name", "type");
        archetype.setDescription("Default Description");
        archetype.setTechnology("Default Technology");
        archetype.addTags("Default Tag");

        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        DslContext context = context();

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("source", user);
        elements.register("destination", softwareSystem);
        context.setIdentifierRegister(elements);

        assertEquals(0, model.getRelationships().size());

        parser.parse(context, tokens("source", "->", "destination", "Uses", "HTTP", "Tag 1,Tag 2"), archetype);

        assertEquals(1, model.getRelationships().size());
        Relationship r = model.getRelationships().iterator().next();
        assertSame(user, r.getSource());
        assertSame(softwareSystem, r.getDestination());
        assertEquals("Uses", r.getDescription()); // overridden from archetype
        assertEquals("HTTP", r.getTechnology()); // overridden from archetype
        assertEquals("Relationship,Default Tag,Tag 1,Tag 2", r.getTags());
    }

    @Test
    void test_parse_AddsTheRelationshipAndImplicitRelationshipsWithADescriptionAndTechnologyAndTags() {
        Person user = model.addPerson("User", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        DslContext context = context();

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("source", user);
        elements.register("destination", container);
        context.setIdentifierRegister(elements);

        assertEquals(0, model.getRelationships().size());

        parser.parse(context, tokens("source", "->", "destination", "Uses", "HTTP", "Tag 1,Tag 2"), archetype);
        assertEquals(2, model.getRelationships().size());

        // this is the relationship that was created
        Relationship r = user.getEfferentRelationshipWith(container);
        assertSame(user, r.getSource());
        assertSame(container, r.getDestination());
        assertEquals("Uses", r.getDescription());
        assertEquals("HTTP", r.getTechnology());
        assertEquals("Relationship,Tag 1,Tag 2", r.getTags());

        // and this is an implied relationship
        r = user.getEfferentRelationshipWith(softwareSystem);
        assertSame(user, r.getSource());
        assertSame(softwareSystem, r.getDestination());
        assertEquals("Uses", r.getDescription());
        assertEquals("HTTP", r.getTechnology());
        assertEquals("", r.getTags());
    }

    @Test
    void test_parse_AddsTheRelationship_WithASourceOfThis() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");

        GroupableElementDslContext context = new PersonDslContext(user);
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("destination", softwareSystem);
        context.setIdentifierRegister(elements);

        assertEquals(0, model.getRelationships().size());

        parser.parse(context, tokens("this", "->", "destination"), archetype);

        assertEquals(1, model.getRelationships().size());
        Relationship r = model.getRelationships().iterator().next();
        assertSame(user, r.getSource());
        assertSame(softwareSystem, r.getDestination());
        assertEquals("", r.getDescription());
        assertEquals("", r.getTechnology());
        assertEquals("Relationship", r.getTags());
    }

    @Test
    void test_parse_AddsTheRelationship_WithADestinationOfThis() {
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");

        GroupableElementDslContext context = new SoftwareSystemDslContext(softwareSystem);
        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("source", user);
        context.setIdentifierRegister(elements);

        assertEquals(0, model.getRelationships().size());

        parser.parse(context, tokens("source", "->", "this"), archetype);

        assertEquals(1, model.getRelationships().size());
        Relationship r = model.getRelationships().iterator().next();
        assertSame(user, r.getSource());
        assertSame(softwareSystem, r.getDestination());
        assertEquals("", r.getDescription());
        assertEquals("", r.getTechnology());
        assertEquals("Relationship", r.getTags());
    }

    @Test
    void test_parse_AddsTheRelationshipToAllSoftwareSystemInstancesInTheDeploymentEnvironment() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");

        DeploymentNode devDeploymentNode = model.addDeploymentNode("dev", "Deployment Node", "Description", "Technology");
        devDeploymentNode.addInfrastructureNode("Infrastructure Node");
        devDeploymentNode.add(softwareSystem);
        devDeploymentNode.add(softwareSystem);

        DeploymentNode liveDeploymentNode = model.addDeploymentNode("live", "Deployment Node", "Description", "Technology");
        InfrastructureNode liveInfrastructureNode = liveDeploymentNode.addInfrastructureNode("Infrastructure Node");
        SoftwareSystemInstance liveSoftwareSystemInstance1 = liveDeploymentNode.add(softwareSystem);
        SoftwareSystemInstance liveSoftwareSystemInstance2 = liveDeploymentNode.add(softwareSystem);

        DeploymentEnvironmentDslContext context = new DeploymentEnvironmentDslContext("live");
        context.setWorkspace(workspace);

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("softwareSystem", softwareSystem);
        elements.register("liveInfrastructureNode", liveInfrastructureNode);
        context.setIdentifierRegister(elements);

        assertEquals(0, model.getRelationships().size());

        Set<Relationship> relationships = parser.parse(context, tokens("liveInfrastructureNode", "->", "softwareSystem"), archetype);

        assertEquals(2, relationships.size());
        assertEquals(2, model.getRelationships().size());
        assertTrue(liveInfrastructureNode.hasEfferentRelationshipWith(liveSoftwareSystemInstance1));
        assertTrue(liveInfrastructureNode.hasEfferentRelationshipWith(liveSoftwareSystemInstance2));
    }

    @Test
    void test_parse_AddsTheRelationshipFromAllSoftwareSystemInstancesInTheDeploymentEnvironment() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");

        DeploymentNode devDeploymentNode = model.addDeploymentNode("dev", "Deployment Node", "Description", "Technology");
        devDeploymentNode.addInfrastructureNode("Infrastructure Node");
        devDeploymentNode.add(softwareSystem);
        devDeploymentNode.add(softwareSystem);

        DeploymentNode liveDeploymentNode = model.addDeploymentNode("live", "Deployment Node", "Description", "Technology");
        InfrastructureNode liveInfrastructureNode = liveDeploymentNode.addInfrastructureNode("Infrastructure Node");
        SoftwareSystemInstance liveSoftwareSystemInstance1 = liveDeploymentNode.add(softwareSystem);
        SoftwareSystemInstance liveSoftwareSystemInstance2 = liveDeploymentNode.add(softwareSystem);

        DeploymentEnvironmentDslContext context = new DeploymentEnvironmentDslContext("live");
        context.setWorkspace(workspace);

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("softwareSystem", softwareSystem);
        elements.register("liveInfrastructureNode", liveInfrastructureNode);
        context.setIdentifierRegister(elements);

        assertEquals(0, model.getRelationships().size());

        Set<Relationship> relationships = parser.parse(context, tokens("softwareSystem", "->", "liveInfrastructureNode"), archetype);

        assertEquals(2, relationships.size());
        assertEquals(2, model.getRelationships().size());
        assertTrue(liveSoftwareSystemInstance1.hasEfferentRelationshipWith(liveInfrastructureNode));
        assertTrue(liveSoftwareSystemInstance2.hasEfferentRelationshipWith(liveInfrastructureNode));
    }

    @Test
    void test_parse_AddsTheRelationshipToAllContainerInstancesInTheDeploymentEnvironment() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");

        DeploymentNode devDeploymentNode = model.addDeploymentNode("dev", "Deployment Node", "Description", "Technology");
        devDeploymentNode.addInfrastructureNode("Infrastructure Node");
        devDeploymentNode.add(container);
        devDeploymentNode.add(container);

        DeploymentNode liveDeploymentNode = model.addDeploymentNode("live", "Deployment Node", "Description", "Technology");
        InfrastructureNode liveInfrastructureNode = liveDeploymentNode.addInfrastructureNode("Infrastructure Node");
        ContainerInstance liveContainerInstance1 = liveDeploymentNode.add(container);
        ContainerInstance liveContainerInstance2 = liveDeploymentNode.add(container);

        DeploymentNodeDslContext context = new DeploymentNodeDslContext(liveDeploymentNode);
        context.setWorkspace(workspace);

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("container", container);
        elements.register("liveInfrastructureNode", liveInfrastructureNode);
        context.setIdentifierRegister(elements);

        assertEquals(0, model.getRelationships().size());

        Set<Relationship> relationships = parser.parse(context, tokens("liveInfrastructureNode", "->", "container"), archetype);

        assertEquals(2, relationships.size());
        assertEquals(2, model.getRelationships().size());
        assertTrue(liveInfrastructureNode.hasEfferentRelationshipWith(liveContainerInstance1));
        assertTrue(liveInfrastructureNode.hasEfferentRelationshipWith(liveContainerInstance2));
    }

    @Test
    void test_parse_AddsTheRelationshipFromAllContainerInstancesInTheDeploymentEnvironment() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");

        DeploymentNode devDeploymentNode = model.addDeploymentNode("dev", "Deployment Node", "Description", "Technology");
        devDeploymentNode.addInfrastructureNode("Infrastructure Node");
        devDeploymentNode.add(container);
        devDeploymentNode.add(container);

        DeploymentNode liveDeploymentNode = model.addDeploymentNode("live", "Deployment Node", "Description", "Technology");
        InfrastructureNode liveInfrastructureNode = liveDeploymentNode.addInfrastructureNode("Infrastructure Node");
        ContainerInstance liveContainerInstance1 = liveDeploymentNode.add(container);
        ContainerInstance liveContainerInstance2 = liveDeploymentNode.add(container);

        DeploymentNodeDslContext context = new DeploymentNodeDslContext(liveDeploymentNode);
        context.setWorkspace(workspace);

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("container", container);
        elements.register("liveInfrastructureNode", liveInfrastructureNode);
        context.setIdentifierRegister(elements);

        assertEquals(0, model.getRelationships().size());

        Set<Relationship> relationships = parser.parse(context, tokens("container", "->", "liveInfrastructureNode"), archetype);

        assertEquals(2, relationships.size());
        assertEquals(2, model.getRelationships().size());
        assertTrue(liveContainerInstance1.hasEfferentRelationshipWith(liveInfrastructureNode));
        assertTrue(liveContainerInstance2.hasEfferentRelationshipWith(liveInfrastructureNode));
    }

    @Test
    void test_parse_AddsAViaRelationshipUsingTheDescriptionAndTechnologyOfTheRemovedRelationship() {
        SoftwareSystem ss = model.addSoftwareSystem("SS");
        Container a = ss.addContainer("A");
        Container b = ss.addContainer("B");
        Relationship relationship = a.uses(b, "Makes API calls using", "JSON/HTTPS");

        DeploymentNode liveDeploymentNode = model.addDeploymentNode("live", "Deployment Node", "Description", "Technology");
        InfrastructureNode infrastructureNode = liveDeploymentNode.addInfrastructureNode("Infrastructure Node");
        ContainerInstance aInstance = liveDeploymentNode.add(a);
        ContainerInstance bInstance = liveDeploymentNode.add(b);

        NoRelationshipInDeploymentEnvironmentDslContext context = new NoRelationshipInDeploymentEnvironmentDslContext(new DeploymentEnvironmentDslContext("live"), relationship);
        context.setWorkspace(workspace);

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("a", aInstance);
        elements.register("infrastructureNode", infrastructureNode);
        context.setIdentifierRegister(elements);

        parser.parse(context, tokens("a", "->", "infrastructureNode"), archetype);

        Relationship aInstanceToInfrastructureNode = aInstance.getEfferentRelationshipWith(infrastructureNode);
        assertEquals("Makes API calls using", aInstanceToInfrastructureNode.getDescription());
        assertEquals("JSON/HTTPS", aInstanceToInfrastructureNode.getTechnology());
    }

    @Test
    void test_parse_AddsAViaRelationshipUOverridingTheDescriptionAndTechnologyOfTheRemovedRelationship() {
        SoftwareSystem ss = model.addSoftwareSystem("SS");
        Container a = ss.addContainer("A");
        Container b = ss.addContainer("B");
        Relationship relationship = a.uses(b, "Makes API calls using", "JSON/HTTPS");

        DeploymentNode liveDeploymentNode = model.addDeploymentNode("live", "Deployment Node", "Description", "Technology");
        InfrastructureNode infrastructureNode = liveDeploymentNode.addInfrastructureNode("Infrastructure Node");
        ContainerInstance aInstance = liveDeploymentNode.add(a);
        ContainerInstance bInstance = liveDeploymentNode.add(b);

        NoRelationshipInDeploymentEnvironmentDslContext context = new NoRelationshipInDeploymentEnvironmentDslContext(new DeploymentEnvironmentDslContext("live"), relationship);
        context.setWorkspace(workspace);

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("a", aInstance);
        elements.register("infrastructureNode", infrastructureNode);
        context.setIdentifierRegister(elements);

        parser.parse(context, tokens("a", "->", "infrastructureNode", "New description", "New technology"), archetype);

        Relationship aInstanceToInfrastructureNode = aInstance.getEfferentRelationshipWith(infrastructureNode);
        assertEquals("New description", aInstanceToInfrastructureNode.getDescription());
        assertEquals("New technology", aInstanceToInfrastructureNode.getTechnology());
    }

}