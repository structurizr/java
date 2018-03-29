package com.structurizr.io.json;

import com.structurizr.Workspace;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class JsonTests {

    @Test
    public void test_write_and_read() throws Exception {
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
    public void test_backwardsCompatibilityOfRenamingEnterpriseContextViewsToSystemLandscapeViews() throws Exception {
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

}