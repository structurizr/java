package com.structurizr.api;

import com.structurizr.WorkspaceValidationException;
import com.structurizr.util.WorkspaceUtils;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class WorkspaceRulesValidationTests {

    private static final File PATH_TO_WORKSPACE_FILES = new File("test/integration/workspaceValidation");

    @Test
    public void test_exceptionThrown_WhenElementIdsAreNotUnique() throws Exception {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File(PATH_TO_WORKSPACE_FILES, "ElementIdsAreNotUnique.json"));
            fail();
        } catch (WorkspaceValidationException we) {
            assertTrue(we.getMessage().startsWith("The element /Software System "));
            assertTrue(we.getMessage().endsWith(" has a non-unique ID of 1."));
        }
    }

    @Test
    public void test_exceptionThrown_WhenRelationshipIdsAreNotUnique() throws Exception {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File(PATH_TO_WORKSPACE_FILES, "RelationshipIdsAreNotUnique.json"));
            fail();
        } catch (WorkspaceValidationException we) {
            assertEquals("The relationship {1 | User | null} ---[Uses 2]---> {2 | Software System | null} has a non-unique ID of 3.", we.getMessage());
        }
    }

    @Test
    public void test_exceptionThrown_WhenViewKeysAreNotUnique() throws Exception {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File(PATH_TO_WORKSPACE_FILES, "ViewKeysAreNotUnique.json"));
            fail();
        } catch (WorkspaceValidationException we) {
            assertEquals("A view with the key key already exists.", we.getMessage());
        }
    }

    @Test
    public void test_exceptionThrown_WhenPeopleAndSoftwareSystemNamesAreNotUnique() throws Exception {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File(PATH_TO_WORKSPACE_FILES, "PeopleAndSoftwareSystemNamesAreNotUnique.json"));
            fail();
        } catch (WorkspaceValidationException we) {
            assertEquals("An element named Name already exists in this context.", we.getMessage());
        }
    }

    @Test
    public void test_exceptionThrown_WhenContainerNamesAreNotUnique() throws Exception {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File(PATH_TO_WORKSPACE_FILES, "ContainerNamesAreNotUnique.json"));
            fail();
        } catch (WorkspaceValidationException we) {
            assertEquals("An element named Container already exists in this context.", we.getMessage());
        }
    }

    @Test
    public void test_exceptionThrown_WhenComponentNamesAreNotUnique() throws Exception {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File(PATH_TO_WORKSPACE_FILES, "ComponentNamesAreNotUnique.json"));
            fail();
        } catch (WorkspaceValidationException we) {
            assertEquals("An element named Component already exists in this context.", we.getMessage());
        }
    }

//    public static void main(String[] args) throws Exception {
//        Workspace workspace = new Workspace("Name", "Description");
//        Model model = workspace.getModel();
//        ViewSet views = workspace.getViews();
//
//        WorkspaceUtils.saveWorkspaceToJson(workspace, new File (new File("structurizr-client/test/integration/workspaceValidation"), "x.json"));
//    }

}