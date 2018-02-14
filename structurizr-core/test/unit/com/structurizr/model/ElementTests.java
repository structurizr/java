package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.Workspace;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ElementTests extends AbstractWorkspaceTestBase {

   @Test
   public void test_construction() {
       Element element = model.addSoftwareSystem("Name", "Description");
       assertEquals("Name", element.getName());
       assertEquals("Description", element.getDescription());
   }

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

    @Test
    public void test_setUrl() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.setUrl("https://structurizr.com");
        assertEquals("https://structurizr.com", element.getUrl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_setUrl_ThrowsAnIllegalArgumentException_WhenAnInvalidUrlIsSpecified() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.setUrl("htt://blah");
    }

    @Test
    public void test_setUrl_DoesNothing_WhenANullUrlIsSpecified() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.setUrl(null);
        assertNull(element.getUrl());
    }

    @Test
    public void test_setUrl_DoesNothing_WhenAnEmptyUrlIsSpecified() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.setUrl(" ");
        assertNull(element.getUrl());
    }

    @Test
    public void test_getProperties_ReturnsAnEmptyList_WhenNoPropertiesHaveBeenAdded() {
        Element element = model.addSoftwareSystem("Name", "Description");
        assertEquals(0, element.getProperties().size());
    }

    @Test
    public void test_addProperty_ThrowsAnException_WhenTheNameIsNull() {
        try {
            Element element = model.addSoftwareSystem("Name", "Description");
            element.addProperty(null, "value");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A property name must be specified.", e.getMessage());
        }
    }

    @Test
    public void test_addProperty_ThrowsAnException_WhenTheNameIsEmpty() {
        try {
            Element element = model.addSoftwareSystem("Name", "Description");
            element.addProperty(" ", "value");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A property name must be specified.", e.getMessage());
        }
    }

    @Test
    public void test_addProperty_ThrowsAnException_WhenTheValueIsNull() {
        try {
            Element element = model.addSoftwareSystem("Name", "Description");
            element.addProperty("name", null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A property value must be specified.", e.getMessage());
        }
    }

    @Test
    public void test_addProperty_ThrowsAnException_WhenTheValueIsEmpty() {
        try {
            Element element = model.addSoftwareSystem("Name", "Description");
            element.addProperty("name", " ");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A property value must be specified.", e.getMessage());
        }
    }

    @Test
    public void test_addProperty_AddsTheProperty_WhenANameAndValueAreSpecified() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.addProperty("AWS region", "us-east-1");
        assertEquals("us-east-1", element.getProperties().get("AWS region"));
    }

    @Test
    public void test_setProperties_DoesNothing_WhenNullIsSpecified() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.setProperties(null);
        assertEquals(0, element.getProperties().size());
    }

    @Test
    public void test_setProperties_SetsTheProperties_WhenANonEmptyMapIsSpecified() {
        Element element = model.addSoftwareSystem("Name", "Description");
        Map<String, String> properties = new HashMap<>();
        properties.put("name", "value");
        element.setProperties(properties);
        assertEquals(1, element.getProperties().size());
        assertEquals("value", element.getProperties().get("name"));
    }

}