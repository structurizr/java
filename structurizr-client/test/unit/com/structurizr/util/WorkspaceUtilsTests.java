package com.structurizr.util;

import com.structurizr.Workspace;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FilenameFilter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class WorkspaceUtilsTests {

    @Test
    void test_loadWorkspaceFromJson_ThrowsAnException_WhenANullFileIsSpecified() {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(null);
            fail();
        } catch (Exception e) {
            assertEquals("The path to a JSON file must be specified.", e.getMessage());
        }
    }

    @Test
    void test_loadWorkspaceFromJson_ThrowsAnException_WhenTheFileDoesNotExist() {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File("test/unit/com/structurizr/util/other-workspace.json"));
            fail();
        } catch (Exception e) {
            assertEquals("The specified JSON file does not exist.", e.getMessage());
        }
    }

    @Test
    void test_saveWorkspaceToJson_ThrowsAnException_WhenANullWorkspaceIsSpecified() {
        try {
            WorkspaceUtils.saveWorkspaceToJson(null, null);
            fail();
        } catch (Exception e) {
            assertEquals("A workspace must be provided.", e.getMessage());
        }
    }

    @Test
    void test_saveWorkspaceToJson_ThrowsAnException_WhenANullFileIsSpecified() {
        try {
            WorkspaceUtils.saveWorkspaceToJson(new Workspace("Name", "Description"), null);
            fail();
        } catch (Exception e) {
            assertEquals("The path to a JSON file must be specified.", e.getMessage());
        }
    }

    @Test
    void test_saveWorkspaceToJson_and_loadWorkspaceFromJson() throws Exception {
        File file = new File("build/workspace-utils.json");
        Workspace workspace = new Workspace("Name", "Description");
        WorkspaceUtils.saveWorkspaceToJson(workspace, file);

        workspace = WorkspaceUtils.loadWorkspaceFromJson(file);
        assertEquals("Name", workspace.getName());
    }

    @Test
    void test_toJson_ThrowsAnException_WhenANullWorkspaceIsProvided() throws Exception {
        try {
            WorkspaceUtils.toJson(null, true);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A workspace must be provided.", iae.getMessage());
        }
    }

    @Test
    void test_toJson() throws Exception {
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
    void test_fromJson_ThrowsAnException_WhenANullJsonStringIsProvided() throws Exception {
        try {
            WorkspaceUtils.fromJson(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A JSON string must be provided.", iae.getMessage());
        }
    }

    @Test
    void test_fromJson_ThrowsAnException_WhenAnEmptyJsonStringIsProvided() throws Exception {
        try {
            WorkspaceUtils.fromJson(" ");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A JSON string must be provided.", iae.getMessage());
        }
    }

    @Test
    void test_fromJson() throws Exception {
        Workspace workspace = WorkspaceUtils.fromJson("{\"id\":0,\"name\":\"Name\",\"description\":\"Description\",\"model\":{},\"documentation\":{},\"views\":{\"configuration\":{\"branding\":{},\"styles\":{},\"terminology\":{}}}}");
        assertEquals("Name", workspace.getName());
        assertEquals("Description", workspace.getDescription());
    }

}