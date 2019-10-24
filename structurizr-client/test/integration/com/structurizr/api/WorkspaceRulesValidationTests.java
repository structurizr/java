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
            assertEquals("A person or software system named \"Name\" already exists.", we.getMessage());
        }
    }

    @Test
    public void test_exceptionThrown_WhenContainerNamesAreNotUnique() throws Exception {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File(PATH_TO_WORKSPACE_FILES, "ContainerNamesAreNotUnique.json"));
            fail();
        } catch (WorkspaceValidationException we) {
            assertEquals("A container named \"Container\" already exists within \"Software System\".", we.getMessage());
        }
    }

    @Test
    public void test_exceptionThrown_WhenComponentNamesAreNotUnique() throws Exception {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File(PATH_TO_WORKSPACE_FILES, "ComponentNamesAreNotUnique.json"));
            fail();
        } catch (WorkspaceValidationException we) {
            assertEquals("A component named \"Component\" already exists within \"Container\".", we.getMessage());
        }
    }

    @Test
    public void test_exceptionThrown_WhenTopLevelDeploymentNodeNamesAreNotUnique() throws Exception {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File(PATH_TO_WORKSPACE_FILES, "TopLevelDeploymentNodeNamesAreNotUnique.json"));
            fail();
        } catch (WorkspaceValidationException we) {
            assertEquals("A top-level deployment node named \"Deployment Node\" already exists.", we.getMessage());
        }
    }

    @Test
    public void test_exceptionThrown_WhenChildDeploymentNodeNamesAreNotUnique() throws Exception {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File(PATH_TO_WORKSPACE_FILES, "ChildDeploymentNodeNamesAreNotUnique.json"));
            fail();
        } catch (WorkspaceValidationException we) {
            assertEquals("A deployment node named \"Child\" already exists within \"Deployment Node\".", we.getMessage());
        }
    }

    @Test
    public void test_exceptionThrown_WhenRelationshipDescriptionsAreNotUnique() throws Exception {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File(PATH_TO_WORKSPACE_FILES, "RelationshipDescriptionsAreNotUnique.json"));
            fail();
        } catch (WorkspaceValidationException we) {
            assertEquals("A relationship with the description \"Uses\" already exists between \"User\" and \"Software System\".", we.getMessage());
        }
    }

//    public static void main(String[] args) throws Exception {
//        Workspace workspace = new Workspace("Name", "Description");
//        Model model = workspace.getModel();
//        ViewSet views = workspace.getViews();
//
//        Person person = model.addPerson("User", "");
//        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "");
//
//        person.uses(softwareSystem, "Uses");
//        person.uses(softwareSystem, "Uses2");
//
//        WorkspaceUtils.saveWorkspaceToJson(workspace, new File(new File("./structurizr-client/" + PATH_TO_WORKSPACE_FILES), "RelationshipDescriptionsAreNotUnique.json"));
//    }

}