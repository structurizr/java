package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GroupableElementTests extends AbstractWorkspaceTestBase {

    @Test
    void test_getGroup_ReturnsNullByDefault() {
        Person element = model.addPerson("Person");
        assertNull(element.getGroup());
    }

    @Test
    void test_setGroup() {
        Person element = model.addPerson("Person");
        element.setGroup("Group");
        assertEquals("Group", element.getGroup());
    }

    @Test
    void test_setGroup_TrimsWhiteSpace() {
        Person element = model.addPerson("Person");
        element.setGroup(" Group ");
        assertEquals("Group", element.getGroup());
    }

    @Test
    void test_setGroup_HandlesEmptyAndNullValues() {
        Person element = model.addPerson("Person");
        element.setGroup("Group");

        element.setGroup(null);
        assertNull(element.getGroup());

        element.setGroup("Group");
        element.setGroup("");
        assertNull(element.getGroup());

        element.setGroup("Group");
        element.setGroup(" ");
        assertNull(element.getGroup());
    }

}