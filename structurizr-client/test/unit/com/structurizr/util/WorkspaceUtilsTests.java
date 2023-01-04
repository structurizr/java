package com.structurizr.util;

import com.structurizr.Workspace;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class WorkspaceUtilsTests {

    @Test
    void loadWorkspaceFromJson_ThrowsAnException_WhenANullFileIsSpecified() {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(null);
            fail();
        } catch (Exception e) {
            assertEquals("The path to a JSON file must be specified.", e.getMessage());
        }
    }

    @Test
    void loadWorkspaceFromJson_ThrowsAnException_WhenTheFileDoesNotExist() {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File("test/unit/com/structurizr/util/other-workspace.json"));
            fail();
        } catch (Exception e) {
            assertEquals("The specified JSON file does not exist.", e.getMessage());
        }
    }

    @Test
    void saveWorkspaceToJson_ThrowsAnException_WhenANullWorkspaceIsSpecified() {
        try {
            WorkspaceUtils.saveWorkspaceToJson(null, null);
            fail();
        } catch (Exception e) {
            assertEquals("A workspace must be provided.", e.getMessage());
        }
    }

    @Test
    void saveWorkspaceToJson_ThrowsAnException_WhenANullFileIsSpecified() {
        try {
            WorkspaceUtils.saveWorkspaceToJson(new Workspace("Name", "Description"), null);
            fail();
        } catch (Exception e) {
            assertEquals("The path to a JSON file must be specified.", e.getMessage());
        }
    }

    @Test
    void saveWorkspaceToJson_and_loadWorkspaceFromJson() throws Exception {
        File file = new File("build/workspace-utils.json");
        Workspace workspace = new Workspace("Name", "Description");
        WorkspaceUtils.saveWorkspaceToJson(workspace, file);

        workspace = WorkspaceUtils.loadWorkspaceFromJson(file);
        assertEquals("Name", workspace.getName());
    }

    @Test
    void toJson_ThrowsAnException_WhenANullWorkspaceIsProvided() throws Exception {
        try {
            WorkspaceUtils.toJson(null, true);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A workspace must be provided.", iae.getMessage());
        }
    }

    @Test
    void toJson() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        String indentedOutput = WorkspaceUtils.toJson(workspace, true);
        String unindentedOutput = WorkspaceUtils.toJson(workspace, false);

        assertEquals("{\n" +
                "  \"id\" : 0,\n" +
                "  \"name\" : \"Name\",\n" +
                "  \"description\" : \"Description\",\n" +
                "  \"configuration\" : { },\n" +
                "  \"model\" : { },\n" +
                "  \"documentation\" : { },\n" +
                "  \"views\" : {\n" +
                "    \"configuration\" : {\n" +
                "      \"branding\" : { },\n" +
                "      \"styles\" : { },\n" +
                "      \"terminology\" : { }\n" +
                "    }\n" +
                "  }\n" +
                "}", indentedOutput);
        assertEquals("{\"id\":0,\"name\":\"Name\",\"description\":\"Description\",\"configuration\":{},\"model\":{},\"documentation\":{},\"views\":{\"configuration\":{\"branding\":{},\"styles\":{},\"terminology\":{}}}}", unindentedOutput);
    }

    @Test
    void fromJson_ThrowsAnException_WhenANullJsonStringIsProvided() throws Exception {
        try {
            WorkspaceUtils.fromJson(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A JSON string must be provided.", iae.getMessage());
        }
    }

    @Test
    void fromJson_ThrowsAnException_WhenAnEmptyJsonStringIsProvided() throws Exception {
        try {
            WorkspaceUtils.fromJson(" ");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A JSON string must be provided.", iae.getMessage());
        }
    }

    @Test
    void fromJson() throws Exception {
        Workspace workspace = WorkspaceUtils.fromJson("{\"id\":0,\"name\":\"Name\",\"description\":\"Description\",\"model\":{},\"documentation\":{},\"views\":{\"configuration\":{\"branding\":{},\"styles\":{},\"terminology\":{}}}}");
        assertEquals("Name", workspace.getName());
        assertEquals("Description", workspace.getDescription());
    }

    @Test
    void elementNamesAreCaseSensitive() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Name");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("NAME");
        SoftwareSystem softwareSystem3 = model.addSoftwareSystem("name");

        assertEquals(3, model.getSoftwareSystems().size());

        WorkspaceUtils.fromJson(WorkspaceUtils.toJson(workspace, false)); // no exception thrown
    }

    @Test
    void relationshipDescriptionsAreCaseSensitive() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("1");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("2");

        softwareSystem1.uses(softwareSystem2, "Uses");
        softwareSystem1.uses(softwareSystem2, "USES");
        softwareSystem1.uses(softwareSystem2, "uses");

        assertEquals(3, softwareSystem1.getRelationships().size());

        WorkspaceUtils.fromJson(WorkspaceUtils.toJson(workspace, false)); // no exception thrown
    }

}