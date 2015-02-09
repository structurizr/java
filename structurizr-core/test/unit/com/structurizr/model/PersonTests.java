package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PersonTests extends AbstractWorkspaceTestBase {

    private Person person = model.addPerson(Location.External, "Person", "Description");

    @Test
    public void test_getCanonicalName() {
        assertEquals("/Person", person.getCanonicalName());
    }

}