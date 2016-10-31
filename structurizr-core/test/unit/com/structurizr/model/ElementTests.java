package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.Workspace;
import org.junit.Test;

import static org.junit.Assert.*;

public class ElementTests extends AbstractWorkspaceTestBase {

    @Test
    public void test_setName_ThrowsAnException_WhenANullValueIsSpecified() {
        Element element = model.addSoftwareSystem("Name", "Description");
        try {
            element.setName(null);
            fail();
        } catch (Exception e) {
            assertEquals("The name of an element must not be null or empty.", e.getMessage());
        }
    }

    @Test
    public void test_setName_ThrowsAnException_WhenAnEmptyValueIsSpecified() {
        Element element = model.addSoftwareSystem("Name", "Description");
        try {
            element.setName(" ");
            fail();
        } catch (Exception e) {
            assertEquals("The name of an element must not be null or empty.", e.getMessage());
        }
    }

    @Test
    public void test_getTags_WhenThereAreNoTags() {
        Element element = model.addSoftwareSystem("Name", "Description");
        assertEquals("Element,Software System", element.getTags());
    }

    @Test
    public void test_getTags_ReturnsTheListOfTags_WhenThereAreSomeTags() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.addTags("tag1", "tag2", "tag3");
        assertEquals("Element,Software System,tag1,tag2,tag3", element.getTags());
    }

    @Test
    public void test_setTags_DoesNotDoAnything_WhenPassedNull() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.setTags(null);
        assertEquals("Element,Software System", element.getTags());
    }

    @Test
    public void test_addTags_DoesNotDoAnything_WhenPassedNull() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.addTags((String)null);
        assertEquals("Element,Software System", element.getTags());

        element.addTags(null, null, null);
        assertEquals("Element,Software System", element.getTags());
    }

    @Test
    public void test_addTags_AddsTags_WhenPassedSomeTags() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.addTags(null, "tag1", null, "tag2");
        assertEquals("Element,Software System,tag1,tag2", element.getTags());
    }

    @Test
    public void test_addTags_AddsTags_WhenPassedSomeTagsAndThereAreDuplicateTags() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.addTags(null, "tag1", null, "tag2", "tag2");
        assertEquals("Element,Software System,tag1,tag2", element.getTags());
    }

    @Test
    public void test_hasEfferentRelationshipWith_ReturnsFalse_WhenANullElementIsSpecified() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        assertFalse(softwareSystem1.hasEfferentRelationshipWith(null));
    }

    @Test
    public void test_hasEfferentRelationshipWith_ReturnsFalse_WhenTheSameElementIsSpecifiedAndNoCyclicRelationshipExists() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        assertFalse(softwareSystem1.hasEfferentRelationshipWith(softwareSystem1));
    }

    @Test
    public void test_hasEfferentRelationshipWith_ReturnsTrue_WhenTheSameElementIsSpecifiedAndACyclicRelationshipExists() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        softwareSystem1.uses(softwareSystem1, "uses");
        assertTrue(softwareSystem1.hasEfferentRelationshipWith(softwareSystem1));
    }

    @Test
    public void test_hasEfferentRelationshipWith_ReturnsTrue_WhenThereIsARelationship() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("System 2", "");
        softwareSystem1.uses(softwareSystem2, "uses");
        assertTrue(softwareSystem1.hasEfferentRelationshipWith(softwareSystem2));
    }

    @Test
    public void test_getEfferentRelationshipWith_ReturnsNull_WhenANullElementIsSpecified() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        assertNull(softwareSystem1.getEfferentRelationshipWith(null));
    }

    @Test
    public void test_getEfferentRelationshipWith_ReturnsNull_WhenTheSameElementIsSpecifiedAndNoCyclicRelationshipExists() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        assertNull(softwareSystem1.getEfferentRelationshipWith(softwareSystem1));
    }

    @Test
    public void test_getEfferentRelationshipWith_ReturnsCyclicRelationship_WhenTheSameElementIsSpecifiedAndACyclicRelationshipExists() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        softwareSystem1.uses(softwareSystem1, "uses");

        Relationship relationship = softwareSystem1.getEfferentRelationshipWith(softwareSystem1);
        assertSame(softwareSystem1, relationship.getSource());
        assertEquals("uses", relationship.getDescription());
        assertSame(softwareSystem1, relationship.getDestination());
    }

    @Test
    public void test_getEfferentRelationshipWith_ReturnsTheRelationship_WhenThereIsARelationship() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("System 2", "");
        softwareSystem1.uses(softwareSystem2, "uses");

        Relationship relationship = softwareSystem1.getEfferentRelationshipWith(softwareSystem2);
        assertSame(softwareSystem1, relationship.getSource());
        assertEquals("uses", relationship.getDescription());
        assertSame(softwareSystem2, relationship.getDestination());
    }

    @Test
    public void test_hasAfferentRelationships_ReturnsFalse_WhenThereAreNoIncomingRelationships() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("System 2", "");
        softwareSystem1.uses(softwareSystem2, "Uses");

        assertFalse(softwareSystem1.hasAfferentRelationships());
    }

    @Test
    public void test_hasAfferentRelationships_ReturnsTrue_WhenThereAreIncomingRelationships() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("System 2", "");
        softwareSystem1.uses(softwareSystem2, "Uses");

        assertTrue(softwareSystem2.hasAfferentRelationships());
    }

    @Test
    public void test_addRelationship_DoesNothing_WhenTheSameRelationshipWithASoftwareSystemIsAddedMoreThanOnce() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");
        SoftwareSystem b = model.addSoftwareSystem("B", "");

        Relationship relationship = a.uses(b, "Uses");
        assertNotNull(relationship);
        assertEquals(1, model.getRelationships().size());

        assertNull(a.uses(b, "Uses"));
        assertEquals(1, model.getRelationships().size());

        assertNull(a.uses(b, "Uses", "Technology"));
        assertEquals(1, model.getRelationships().size());

        assertNull(a.uses(b, "Uses", "Technology", InteractionStyle.Synchronous));
        assertEquals(1, model.getRelationships().size());
    }

    @Test
    public void test_addRelationship_DoesNothing_WhenTheSameRelationshipWithAContainerIsAddedMoreThanOnce() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");
        SoftwareSystem b = model.addSoftwareSystem("B", "");
        Container bb = b.addContainer("BB", "", "");

        Relationship relationship = a.uses(bb, "Uses");
        assertNotNull(relationship);
        assertEquals(1, model.getRelationships().size());

        assertNull(a.uses(bb, "Uses"));
        assertEquals(1, model.getRelationships().size());

        assertNull(a.uses(bb, "Uses", "Technology"));
        assertEquals(1, model.getRelationships().size());

        assertNull(a.uses(bb, "Uses", "Technology", InteractionStyle.Synchronous));
        assertEquals(1, model.getRelationships().size());
    }

    @Test
    public void test_addRelationship_DoesNothing_WhenTheSameRelationshipWithAComponentIsAddedMoreThanOnce() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");
        SoftwareSystem b = model.addSoftwareSystem("B", "");
        Container bb = b.addContainer("BB", "", "");
        Component bbb = bb.addComponent("BBB", "", "");

        Relationship relationship = a.uses(bbb, "Uses");
        assertNotNull(relationship);
        assertEquals(1, model.getRelationships().size());

        assertNull(a.uses(bbb, "Uses"));
        assertEquals(1, model.getRelationships().size());

        assertNull(a.uses(bbb, "Uses", "Technology"));
        assertEquals(1, model.getRelationships().size());

        assertNull(a.uses(bbb, "Uses", "Technology", InteractionStyle.Synchronous));
        assertEquals(1, model.getRelationships().size());
    }

    @Test
    public void test_addRelationship_DoesNothing_WhenTheSameRelationshipWithAPersonIsAddedMoreThanOnce() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");
        Person b = model.addPerson("B", "");

        Relationship relationship = a.delivers(b, "Sends e-mail to");
        assertNotNull(relationship);
        assertEquals(1, model.getRelationships().size());

        assertNull(a.delivers(b, "Sends e-mail to"));
        assertEquals(1, model.getRelationships().size());

        assertNull(a.delivers(b, "Sends e-mail to", "Technology"));
        assertEquals(1, model.getRelationships().size());

        assertNull(a.delivers(b, "Sends e-mail to", "Technology", InteractionStyle.Synchronous));
        assertEquals(1, model.getRelationships().size());
    }

    @Test
    public void test_equals_ReturnsFalse_WhenTestedAgainstNull() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        assertFalse(softwareSystem.equals(null));
    }

    @Test
    public void test_equals_ReturnsFalse_WhenTheTwoObjectsAreDifferentTypes() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        assertFalse(softwareSystem.equals("hello world"));
    }

    @Test
    public void test_equals_ReturnsTrue_WhenTestedAgainstItself() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        assertTrue(softwareSystem.equals(softwareSystem));
    }

    @Test
    public void test_equals_ReturnsFalse_WhenTheTwoObjectsAreDifferent() {
        SoftwareSystem softwareSystemA = model.addSoftwareSystem("A", "Description");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem("B", "Description");
        assertFalse(softwareSystemA.equals(softwareSystemB));
    }

    @Test
    public void test_equals_ReturnsFalse_WhenTheTwoElementsHaveTheSameCanonicalNameButAreDifferentTypes() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        Person person = model.addPerson("Name", "Description");
        assertFalse(softwareSystem.equals(person));
    }

    @Test
    public void test_equals_ReturnsTrue_WhenTheAnObjectWithTheSameCanonicalNameIsPassed() {
        SoftwareSystem softwareSystem1 = new Workspace("", "").getModel().addSoftwareSystem("System 1", "");
        SoftwareSystem softwareSystem2 = new Workspace("", "").getModel().addSoftwareSystem("System 1", "");
        assertTrue(softwareSystem1.equals(softwareSystem2));
        assertTrue(softwareSystem2.equals(softwareSystem1));
    }

}