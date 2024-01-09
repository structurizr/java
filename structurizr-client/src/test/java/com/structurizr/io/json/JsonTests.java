package com.structurizr.io.json;

import com.structurizr.Workspace;
import com.structurizr.model.*;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JsonTests {

    @Test
    void write_and_read() throws Exception {
        final Workspace workspace1 = new Workspace("Name", "Description");

        // output the model as JSON
        JsonWriter jsonWriter = new JsonWriter(true);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(workspace1, stringWriter);

        // and read it back again
        JsonReader jsonReader = new JsonReader();
        StringReader stringReader = new StringReader(stringWriter.toString());
        final Workspace workspace2 = jsonReader.read(stringReader);
        assertEquals("Name", workspace2.getName());
        assertEquals("Description", workspace2.getDescription());
    }

    @Test
    void backwardsCompatibilityOfRenamingEnterpriseContextViewsToSystemLandscapeViews() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getViews().createSystemLandscapeView("key", "description");

        JsonWriter jsonWriter = new JsonWriter(false);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(workspace, stringWriter);
        String workspaceAsJson = stringWriter.toString();
        workspaceAsJson = workspaceAsJson.replaceAll("systemLandscapeViews", "enterpriseContextViews");

        JsonReader jsonReader = new JsonReader();
        StringReader stringReader = new StringReader(workspaceAsJson);
        workspace = jsonReader.read(stringReader);
        assertEquals(1, workspace.getViews().getSystemLandscapeViews().size());
    }

    @Test
    void write_and_read_withCustomIdGenerator() throws Exception {
        Workspace workspace1 = new Workspace("Name", "Description");
        workspace1.getModel().setIdGenerator(new CustomIdGenerator());
        Person user = workspace1.getModel().addPerson("User");
        SoftwareSystem softwareSystem = workspace1.getModel().addSoftwareSystem("Software System");
        user.uses(softwareSystem, "Uses");

        // output the model as JSON
        JsonWriter jsonWriter = new JsonWriter(true);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(workspace1, stringWriter);

        // and read it back again
        JsonReader jsonReader = new JsonReader();
        jsonReader.setIdGenerator(new CustomIdGenerator());
        StringReader stringReader = new StringReader(stringWriter.toString());

        Workspace workspace2 = jsonReader.read(stringReader);
        assertEquals(user.getId(), workspace2.getModel().getPersonWithName("User").getId());
        assertNotNull(workspace2.getModel().getElement(user.getId()));
    }

    class CustomIdGenerator implements IdGenerator {

        @Override
        public String generateId(Element element) {
            return UUID.randomUUID().toString();
        }

        @Override
        public String generateId(Relationship relationship) {
            return UUID.randomUUID().toString();
        }

        @Override
        public void found(String id) {

        }

    }


}