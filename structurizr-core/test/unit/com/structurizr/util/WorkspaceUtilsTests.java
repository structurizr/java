package com.structurizr.util;

import com.structurizr.Workspace;
import com.structurizr.example.ExampleWorkspace;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class WorkspaceUtilsTests {

    @Test
    public void test_loadWorkspaceFromJson_ThrowsAnException_WhenANullFileIspecified() {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(null);
            fail();
        } catch (Exception e) {
            assertEquals("The path to a JSON file must be specified.", e.getMessage());
        }
    }

    @Test
    public void test_loadWorkspaceFromJson_ThrowsAnException_WhenTheFileDoesNotExist() {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File("test/unit/com/structurizr/util/other-workspace.json"));
            fail();
        } catch (Exception e) {
            assertEquals("The specified JSON file does not exist.", e.getMessage());
        }
    }

    @Test
    public void test_saveWorkspaceToJson_ThrowsAnException_WhenANullWorkspaceIspecified() {
        try {
            WorkspaceUtils.saveWorkspaceToJson(null, null);
            fail();
        } catch (Exception e) {
            assertEquals("A workspace must be provided.", e.getMessage());
        }
    }

    @Test
    public void test_saveWorkspaceToJson_ThrowsAnException_WhenANullFileIspecified() {
        try {
            WorkspaceUtils.saveWorkspaceToJson(new Workspace("Name", "Description"), null);
            fail();
        } catch (Exception e) {
            assertEquals("The path to a JSON file must be specified.", e.getMessage());
        }
    }

    @Test
    public void test_saveWorkspaceToJson_and_loadWorkspaceFromJson() throws Exception {
        File file = new File("build/workspace-utils.json");
        WorkspaceUtils.saveWorkspaceToJson(ExampleWorkspace.create(), file);

        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(file);
        assertEquals("Example workspace", workspace.getName());
    }

}
