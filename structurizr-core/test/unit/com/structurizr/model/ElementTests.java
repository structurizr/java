package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ElementTests extends AbstractWorkspaceTestBase {

    @Test
    void construction() {
        Element element = model.addSoftwareSystem("Name", "Description");
        assertEquals("Name", element.getName());
        assertEquals("Description", element.getDescription());
    }

    @Test
    void setName_ThrowsAnException_WhenANullValueIsSpecified() {
        Element element = model.addSoftwareSystem("Name", "Description");
        try {
            element.setName(null);
            fail();
        } catch (Exception e) {
            assertEquals("The name of an element must not be null or empty.", e.getMessage());
        }
    }

    @Test
    void setName_ThrowsAnException_WhenAnEmptyValueIsSpecified() {
        Element element = model.addSoftwareSystem("Name", "Description");
        try {
            element.setName(" ");
            fail();
        } catch (Exception e) {
            assertEquals("The name of an element must not be null or empty.", e.getMessage());
        }
    }

    @Test
    void hasEfferentRelationshipWith_ReturnsFalse_WhenANullElementIsSpecified() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        assertFalse(softwareSystem1.hasEfferentRelationshipWith(null));
    }

    @Test
    void hasEfferentRelationshipWith_ReturnsFalse_WhenTheSameElementIsSpecifiedAndNoCyclicRelationshipExists() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        assertFalse(softwareSystem1.hasEfferentRelationshipWith(softwareSystem1));
    }

    @Test
    void hasEfferentRelationshipWith_ReturnsTrue_WhenTheSameElementIsSpecifiedAndACyclicRelationshipExists() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        softwareSystem1.uses(softwareSystem1, "uses");
        assertTrue(softwareSystem1.hasEfferentRelationshipWith(softwareSystem1));
    }

    @Test
    void hasEfferentRelationshipWith_ReturnsTrue_WhenThereIsARelationship() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("System 2", "");
        softwareSystem1.uses(softwareSystem2, "uses");
        assertTrue(softwareSystem1.hasEfferentRelationshipWith(softwareSystem2));
    }

    @Test
    void hasEfferentRelationshipWithElementAndDescription_ReturnsFalse_WhenANullElementIsSpecified() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        assertFalse(softwareSystem1.hasEfferentRelationshipWith(null, null));
    }

    @Test
    void hasEfferentRelationshipWithElementAndDescription_ReturnsFalse_WhenThereIsNotAMatchingRelationship() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("System 2", "");
        softwareSystem1.uses(softwareSystem2, "Uses");

        assertFalse(softwareSystem1.hasEfferentRelationshipWith(softwareSystem2, "Does something with"));
    }

    @Test
    void hasEfferentRelationshipWithElementAndDescription_ReturnsTrue_WhenThereIsAMatchingRelationship() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("System 2", "");
        softwareSystem1.uses(softwareSystem2, "Uses");

        assertTrue(softwareSystem1.hasEfferentRelationshipWith(softwareSystem2, "Uses"));
    }

    @Test
    void getEfferentRelationshipWith_ReturnsNull_WhenANullElementIsSpecified() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        assertNull(softwareSystem1.getEfferentRelationshipWith(null));
    }

    @Test
    void getEfferentRelationshipWith_ReturnsNull_WhenTheSameElementIsSpecifiedAndNoCyclicRelationshipExists() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        assertNull(softwareSystem1.getEfferentRelationshipWith(softwareSystem1));
    }

    @Test
    void getEfferentRelationshipWith_ReturnsCyclicRelationship_WhenTheSameElementIsSpecifiedAndACyclicRelationshipExists() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        softwareSystem1.uses(softwareSystem1, "uses");

        Relationship relationship = softwareSystem1.getEfferentRelationshipWith(softwareSystem1);
        assertSame(softwareSystem1, relationship.getSource());
        assertEquals("uses", relationship.getDescription());
        assertSame(softwareSystem1, relationship.getDestination());
    }

    @Test
    void getEfferentRelationshipWith_ReturnsTheRelationship_WhenThereIsARelationship() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("System 2", "");
        softwareSystem1.uses(softwareSystem2, "uses");

        Relationship relationship = softwareSystem1.getEfferentRelationshipWith(softwareSystem2);
        assertSame(softwareSystem1, relationship.getSource());
        assertEquals("uses", relationship.getDescription());
        assertSame(softwareSystem2, relationship.getDestination());
    }

    @Test
    void hasAfferentRelationships_ReturnsFalse_WhenThereAreNoIncomingRelationships() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("System 2", "");
        softwareSystem1.uses(softwareSystem2, "Uses");

        assertFalse(softwareSystem1.hasAfferentRelationships());
    }

    @Test
    void hasAfferentRelationships_ReturnsTrue_WhenThereAreIncomingRelationships() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("System 1", "");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("System 2", "");
        softwareSystem1.uses(softwareSystem2, "Uses");

        assertTrue(softwareSystem2.hasAfferentRelationships());
    }

    @Test
    void addRelationship_DoesNothing_WhenTheSameRelationshipWithASoftwareSystemIsAddedMoreThanOnce() {
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
    void addRelationship_DoesNothing_WhenTheSameRelationshipWithAContainerIsAddedMoreThanOnce() {
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
    void addRelationship_DoesNothing_WhenTheSameRelationshipWithAComponentIsAddedMoreThanOnce() {
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
    void addRelationship_DoesNothing_WhenTheSameRelationshipWithAPersonIsAddedMoreThanOnce() {
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
    void equals_ReturnsFalse_WhenTestedAgainstNull() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        assertNotEquals(softwareSystem, null);
    }

    @Test
    void equals_ReturnsFalse_WhenTheTwoObjectsAreDifferentTypes() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        assertNotEquals(softwareSystem, "hello world");
    }

    @Test
    void equals_ReturnsTrue_WhenTestedAgainstItself() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        assertEquals(softwareSystem, softwareSystem);
    }

    @Test
    void equals_ReturnsFalse_WhenTheTwoObjectsAreDifferent() {
        SoftwareSystem softwareSystemA = model.addSoftwareSystem("A", "Description");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem("B", "Description");
        assertNotEquals(softwareSystemA, softwareSystemB);
    }

    @Test
    void equals_ReturnsFalse_WhenTheTwoElementsHaveTheSameCanonicalNameButAreDifferentTypes() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        Person person = model.addPerson("Name", "Description");
        assertNotEquals(softwareSystem, person);
    }

    @Test
    void setUrl() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.setUrl("https://structurizr.com");
        assertEquals("https://structurizr.com", element.getUrl());
    }

    @Test
    void setUrl_ThrowsAnIllegalArgumentException_WhenAnInvalidUrlIsSpecified() {
        assertThrows(IllegalArgumentException.class, () -> {
            Element element = model.addSoftwareSystem("Name", "Description");
            element.setUrl("htt://blah");
        });
    }

    @Test
    void setUrl_ResetsTheUrl_WhenANullUrlIsSpecified() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.setUrl("https://structurizr.com");
        element.setUrl(null);
        assertNull(element.getUrl());
    }

    @Test
    void setUrl_ResetsTheUrl_WhenAnEmptyUrlIsSpecified() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.setUrl("https://structurizr.com");
        element.setUrl(" ");
        assertNull(element.getUrl());
    }

}