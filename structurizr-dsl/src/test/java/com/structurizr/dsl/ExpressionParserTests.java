package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.view.ContainerView;
import com.structurizr.view.DeploymentView;
import com.structurizr.view.SystemLandscapeView;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionParserTests extends AbstractTests {

    private final ExpressionParser parser = new ExpressionParser();

    @Test
    void test_parseExpression_ThrowsAnException_WhenTheRelationshipSourceIsSpecifiedUsingLongSyntaxButDoesNotExist() {
        try {
            SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
            context.setWorkspace(workspace);

            parser.parseExpression("relationship.source==a", context);
            fail();
        } catch (Exception e) {
            assertEquals("The element \"a\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parseExpression_ThrowsAnException_WhenTheRelationshipSourceIsSpecifiedUsingShortSyntaxButDoesNotExist() {
        try {
            SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
            context.setWorkspace(workspace);

            parser.parseExpression("relationship==a->*", context);
            fail();
        } catch (Exception e) {
            assertEquals("The element \"a\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parseExpression_ThrowsAnException_WhenTheRelationshipDestinationIsSpecifiedUsingLongSyntaxButDoesNotExist() {
        try {
            SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
            context.setWorkspace(workspace);

            parser.parseExpression("relationship.destination==a", context);
            fail();
        } catch (Exception e) {
            assertEquals("The element \"a\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parseExpression_ThrowsAnException_WhenTheWildcardRelationshipIsNegated() {
        try {
            SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
            context.setWorkspace(workspace);

            parser.parseExpression("relationship!=*", context);
            fail();
        } catch (Exception e) {
            assertEquals("The relationship \"*\" cannot be negated", e.getMessage());
        }
    }

    @Test
    void test_parseExpression_ThrowsAnException_WhenTheRelationshipDestinationIsSpecifiedUsingShortSyntaxButDoesNotExist() {
        try {
            SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
            context.setWorkspace(workspace);

            parser.parseExpression("relationship==*->a", context);
            fail();
        } catch (Exception e) {
            assertEquals("The element \"a\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parseExpression_ReturnsARelationship_WhenTheRelationshipSourceIsSpecifiedUsingLongSyntax() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("a", a);
        elements.register("b", b);
        elements.register("c", c);
        context.setIdentifierRegister(elements);

        Set<ModelItem> relationships = parser.parseExpression("relationship.source==a", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));
    }

    @Test
    void test_parseExpression_ReturnsARelationship_WhenTheRelationshipSourceIsSpecifiedUsingAnExpression() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        a.addTags("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        b.addTags("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        c.addTags("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);
        context.setIdentifierRegister(new IdentifiersRegister());

        Set<ModelItem> relationships = parser.parseExpression("* -> element.tag==B", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));

        relationships = parser.parseExpression("element.tag==A -> *", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));

        relationships = parser.parseExpression("element.tag==A -> element.tag==B", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));
    }

    @Test
    void test_parseExpression_ReturnsARelationship_WhenTheRelationshipSourceIsSpecifiedUsingShortSyntax() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("a", a);
        elements.register("b", b);
        elements.register("c", c);
        context.setIdentifierRegister(elements);

        Set<ModelItem> relationships = parser.parseExpression("relationship==a->*", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));

        relationships = parser.parseExpression("relationship!=a->*", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(bToC));

        relationships = parser.parseExpression("a -> *", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));
    }

    @Test
    void test_parseExpression_ReturnsARelationship_WhenTheRelationshipDestinationIsSpecifiedUsingLongSyntax() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");
        Relationship aToC = a.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("a", a);
        elements.register("b", b);
        elements.register("c", c);
        context.setIdentifierRegister(elements);

        Set<ModelItem> relationships = parser.parseExpression("relationship.destination==b", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));

        relationships = parser.parseExpression("relationship.destination!=b", context);
        assertEquals(2, relationships.size());
        assertTrue(relationships.containsAll(Arrays.asList(aToC, bToC)));
    }

    @Test
    void test_parseExpression_ReturnsARelationship_WhenTheRelationshipDestinationIsSpecifiedUsingShortSyntax() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("a", a);
        elements.register("b", b);
        elements.register("c", c);
        context.setIdentifierRegister(elements);

        Set<ModelItem> relationships = parser.parseExpression("relationship==*->b", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));

        relationships = parser.parseExpression("relationship!=*->b", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(bToC));

        relationships = parser.parseExpression("* -> b", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));
    }

    @Test
    void test_parseExpression_ReturnsARelationship_WhenTheRelationshipSourceAndDestinationAreSpecifiedUsingLongSyntax() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("a", a);
        elements.register("b", b);
        elements.register("c", c);
        context.setIdentifierRegister(elements);

        Set<ModelItem> relationships = parser.parseExpression("relationship.source==a && relationship.destination==b",
                context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));

        relationships = parser.parseExpression("relationship.source!=a && relationship.destination!=b",
                context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(bToC));
    }

    @Test
    void test_parseExpression_ReturnsARelationship_WhenTheRelationshipSourceAndDestinationAreSpecifiedUsingShortSyntax() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        IdentifiersRegister elements = new IdentifiersRegister();
        elements.register("a", a);
        elements.register("b", b);
        elements.register("c", c);
        context.setIdentifierRegister(elements);

        Set<ModelItem> relationships = parser.parseExpression("relationship==a->b", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(aToB));

        relationships = parser.parseExpression("relationship!=a->b", context);
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains(bToC));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenUsingAnAfferentCouplingExpressionWithAnElementIdentifier() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        IdentifiersRegister map = new IdentifiersRegister();
        map.register("a", a);
        map.register("b", b);
        map.register("c", c);
        context.setIdentifierRegister(map);

        Set<ModelItem> elements = parser.parseExpression("->b", context);
        assertEquals(2, elements.size());
        assertTrue(elements.contains(a));
        assertTrue(elements.contains(b));

        elements = parser.parseExpression("element==->b", context);
        assertEquals(2, elements.size());
        assertTrue(elements.contains(a));
        assertTrue(elements.contains(b));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenUsingAnEfferentCouplingExpressionWithAnElementIdentifier() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        IdentifiersRegister map = new IdentifiersRegister();
        map.register("a", a);
        map.register("b", b);
        map.register("c", c);
        context.setIdentifierRegister(map);

        Set<ModelItem> elements = parser.parseExpression("b->", context);
        assertEquals(2, elements.size());
        assertTrue(elements.contains(b));
        assertTrue(elements.contains(c));

        elements = parser.parseExpression("element==b->", context);
        assertEquals(2, elements.size());
        assertTrue(elements.contains(b));
        assertTrue(elements.contains(c));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenUsingAnAfferentAndEfferentCouplingExpressionWithAnElementIdentifier() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        IdentifiersRegister map = new IdentifiersRegister();
        map.register("a", a);
        map.register("b", b);
        map.register("c", c);
        context.setIdentifierRegister(map);

        Set<ModelItem> elements = parser.parseExpression("->b->", context);
        assertEquals(3, elements.size());
        assertTrue(elements.contains(a));
        assertTrue(elements.contains(b));
        assertTrue(elements.contains(c));

        elements = parser.parseExpression("element==->b->", context);
        assertEquals(3, elements.size());
        assertTrue(elements.contains(a));
        assertTrue(elements.contains(b));
        assertTrue(elements.contains(c));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenUsingAnAfferentAndEfferentCouplingExpressionWithAnElementExpression() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        b.addTags("Tag 1");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        IdentifiersRegister map = new IdentifiersRegister();
        map.register("a", a);
        map.register("b", b);
        map.register("c", c);
        context.setIdentifierRegister(map);

        Set<ModelItem> elements = parser.parseExpression("->element.tag==Tag 1->", context);
        assertEquals(3, elements.size());
        assertTrue(elements.contains(a));
        assertTrue(elements.contains(b));
        assertTrue(elements.contains(c));

        elements = parser.parseExpression("element==->element.tag==Tag 1->", context);
        assertEquals(3, elements.size());
        assertTrue(elements.contains(a));
        assertTrue(elements.contains(b));
        assertTrue(elements.contains(c));
    }

    @Test
    void test_parseExpression_ReturnsAllRelationships_WhenUsingTheWildcardRelationshipExpression() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        IdentifiersRegister map = new IdentifiersRegister();
        map.register("a", a);
        map.register("b", b);
        map.register("c", c);
        context.setIdentifierRegister(map);

        Set<ModelItem> relationships = parser.parseExpression("relationship==*->*", context);
        assertEquals(2, relationships.size());
        assertTrue(relationships.contains(aToB));
        assertTrue(relationships.contains(bToC));

        relationships = parser.parseExpression("* -> *", context);
        assertEquals(2, relationships.size());
        assertTrue(relationships.contains(aToB));
        assertTrue(relationships.contains(bToC));

        relationships = parser.parseExpression("relationship==*", context);
        assertEquals(2, relationships.size());
        assertTrue(relationships.contains(aToB));
        assertTrue(relationships.contains(bToC));
    }

    @Test
    void test_parseExpression_ReturnsElementsAndElementInstances_WhenUsingAnElementTagEqualsExpression() {
        model.addPerson("User");
        SoftwareSystem ss = model.addSoftwareSystem("Software System");
        SoftwareSystemInstance ssi = model.addDeploymentNode("DN").add(ss);

        Set<ModelItem> elements = parser.parseExpression("element.tag==Software System", context());
        assertEquals(2, elements.size());
        assertTrue(elements.contains(ss)); // this is tagged "Software System"
        assertTrue(elements.contains(ssi)); // this is not tagged "Software System", but the element it's based upon is
    }

    @Test
    void test_parseExpression_ReturnsElementsAndElementInstances_WhenUsingAnElementTechnologyEqualsExpression() {
        model.addPerson("User");
        SoftwareSystem ss = model.addSoftwareSystem("Software System");
        Container c = ss.addContainer("Container");
        c.setTechnology("Java");
        DeploymentNode dn = model.addDeploymentNode("DN");
        dn.setTechnology("EC2");
        InfrastructureNode in = dn.addInfrastructureNode("Infrastructure Node");
        in.setTechnology("ELB");
        ContainerInstance ci = dn.add(c);

        Set<ModelItem> elements = parser.parseExpression("element.technology==Java", context());
        assertEquals(2, elements.size());
        assertTrue(elements.contains(c)); // this has a technology property of "Java"
        assertTrue(elements.contains(ci)); // this has no technology property, but the element it's based upon is

        elements = parser.parseExpression("element.technology==EC2", context());
        assertEquals(1, elements.size());
        assertTrue(elements.contains(dn));

        elements = parser.parseExpression("element.technology==ELB", context());
        assertEquals(1, elements.size());
        assertTrue(elements.contains(in));
    }

    @Test
    void test_parseExpression_ReturnsElementsAndElementInstances_WhenUsingAnElementPropertyEqualsExpression() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        a.addProperty("Technical Debt", "Low");
        SoftwareSystem b = model.addSoftwareSystem("B");
        b.addProperty("Technical Debt", "Medium");
        SoftwareSystem c = model.addSoftwareSystem("C");
        c.addProperty("Technical Debt", "High");
        SoftwareSystem d = model.addSoftwareSystem("D");

        DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node");
        SoftwareSystemInstance ai = deploymentNode.add(a);
        SoftwareSystemInstance bi = deploymentNode.add(b);
        SoftwareSystemInstance ci = deploymentNode.add(c);

        Set<ModelItem> elements = parser.parseExpression("element.properties[Technical Debt]==High", context());
        assertEquals(2, elements.size());
        assertTrue(elements.contains(c)); // this has the property
        assertTrue(elements.contains(ci)); // this doesn't have the property, but the element it's based upon does
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenUsingAnElementTypeExpression() {
        model.addPerson("User");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");

        SystemLandscapeView view = views.createSystemLandscapeView("key", "Description");
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(view);
        context.setWorkspace(workspace);
        context.setIdentifierRegister(new IdentifiersRegister());

        Set<ModelItem> elements = parser.parseExpression("element.type==SoftwareSystem", context);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(softwareSystem));
    }

    @Test
    void test_parseExpression_ThrowsAnException_WhenUsingAnElementParentExpressionAndTheElementDoesNotExist() {
        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);
        context.setIdentifierRegister(new IdentifiersRegister());

        try {
            parser.parseExpression("element.parent==a", context);
            fail();
        } catch (Exception e) {
            assertEquals("The parent element \"a\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenUsingAnElementParentExpression() {
        SoftwareSystem softwareSystemA = model.addSoftwareSystem("Software System A");
        Container container1 = softwareSystemA.addContainer("Container 1");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem("Software System B");
        Container container2 = softwareSystemB.addContainer("Container 2");

        ContainerView view = views.createContainerView(softwareSystemA, "key", "Description");
        ContainerViewDslContext context = new ContainerViewDslContext(view);
        context.setWorkspace(workspace);
        IdentifiersRegister identifiersRegister = new IdentifiersRegister();
        identifiersRegister.register("b", softwareSystemB);
        context.setIdentifierRegister(identifiersRegister);

        Set<ModelItem> elements = parser.parseExpression("element.parent==b", context);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(container2));
    }

    @Test
    void test_parseExpression_ReturnsRelationshipsAndImpliedRelationships_WhenUsingARelationshipTagEqualsExpression() {
        Person user = model.addPerson("User");
        SoftwareSystem a = model.addSoftwareSystem("A");
        Container aa = a.addContainer("AA");
        SoftwareSystem b = model.addSoftwareSystem("B");
        Container bb = b.addContainer("BB");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Container cc = c.addContainer("CC");

        Relationship r1 = user.uses(aa, "Uses");
        r1.addTags("Tag 1");
        Relationship r2 = user.uses(bb, "Uses");
        r2.addTags("Tag 2");
        Relationship r3 = user.uses(cc, "Uses");

        Set<ModelItem> relationships = parser.parseExpression("relationship.tag==Tag 1", context());
        assertEquals(2, relationships.size());
        assertTrue(relationships.contains(r1));

        Relationship impliedRelationship = user.getEfferentRelationshipWith(a);
        assertTrue(relationships.contains(impliedRelationship));
    }

    @Test
    void test_parseExpression_ReturnsRelationshipsAndImpliedRelationships_WhenUsingARelationshipPropertyEqualsExpression() {
        Person user = model.addPerson("User");
        SoftwareSystem a = model.addSoftwareSystem("A");
        Container aa = a.addContainer("AA");
        SoftwareSystem b = model.addSoftwareSystem("B");
        Container bb = b.addContainer("BB");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Container cc = c.addContainer("CC");

        Relationship r1 = user.uses(aa, "Uses");
        r1.addProperty("Secure", "Yes");
        Relationship r2 = user.uses(bb, "Uses");
        r2.addProperty("Secure", "No");
        Relationship r3 = user.uses(cc, "Uses");

        assertEquals(6, model.getRelationships().size());

        Set<ModelItem> relationships = parser.parseExpression("relationship.properties[Secure]==Yes", context());
        assertEquals(2, relationships.size());
        assertTrue(relationships.contains(r1));

        Relationship impliedRelationship = user.getEfferentRelationshipWith(a);
        assertTrue(relationships.contains(impliedRelationship));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenUsingElementNotEqualsIdentifier() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        IdentifiersRegister map = new IdentifiersRegister();
        map.register("a", a);
        map.register("b", b);
        map.register("c", c);
        context.setIdentifierRegister(map);

        Set<ModelItem> elements = parser.parseExpression("element!=c", context);
        assertEquals(2, elements.size());
        assertTrue(elements.contains(a));
        assertTrue(elements.contains(b));
    }

    @Test
    void test_parseExpression_ReturnsElements_WhenUsingElementNotEqualsExpression() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");
        Relationship aToB = a.uses(b, "Uses");
        Relationship bToC = b.uses(c, "Uses");

        SystemLandscapeViewDslContext context = new SystemLandscapeViewDslContext(null);
        context.setWorkspace(workspace);

        IdentifiersRegister map = new IdentifiersRegister();
        map.register("a", a);
        map.register("b", b);
        map.register("c", c);
        context.setIdentifierRegister(map);

        Set<ModelItem> elements = parser.parseExpression("element!=->b", context);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(c));
    }

}
