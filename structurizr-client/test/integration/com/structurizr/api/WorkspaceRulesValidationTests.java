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
            assertTrue(we.getMessage().startsWith("The element SoftwareSystem://Software System "));
            assertTrue(we.getMessage().endsWith(" has a non-unique ID of 1."));
        }
    }

    @Test
    public void test_exceptionThrown_WhenRelationshipIdsAreNotUnique() throws Exception {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File(PATH_TO_WORKSPACE_FILES, "RelationshipIdsAreNotUnique.json"));
            fail();
        } catch (WorkspaceValidationException we) {
            assertTrue(we.getMessage().startsWith("The relationship {1 | User | null} ---[Uses "));
            assertTrue(we.getMessage().endsWith("]---> {2 | Software System | null} has a non-unique ID of 3."));
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
            assertEquals("A top-level deployment node named \"Deployment Node\" already exists for the environment named \"Default\".", we.getMessage());
        }
    }

    @Test
    public void test_exceptionNotThrown_WhenTopLevelDeploymentNodeNamesAreNotUniqueButTheyExistInDifferentEnvironments() throws Exception {
        WorkspaceUtils.loadWorkspaceFromJson(new File(PATH_TO_WORKSPACE_FILES, "TopLevelDeploymentNodeNamesAreNotUniqueButTheyExistInDifferentEnvironments.json"));
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

    @Test
    public void test_exceptionThrown_WhenSoftwareSystemAssociatedWithSystemContextViewIsMissingFromTheModel() throws Exception {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File(PATH_TO_WORKSPACE_FILES, "SoftwareSystemAssociatedWithSystemContextViewIsMissingFromTheModel.json"));
            fail();
        } catch (WorkspaceValidationException we) {
            assertEquals("The system context view with key \"SystemContext\" is associated with a software system (id=2), but that element does not exist in the model.", we.getMessage());
        }
    }

    @Test
    public void test_exceptionThrown_WhenSoftwareSystemAssociatedWithContainerViewIsMissingFromTheModel() throws Exception {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File(PATH_TO_WORKSPACE_FILES, "SoftwareSystemAssociatedWithContainerViewIsMissingFromTheModel.json"));
            fail();
        } catch (WorkspaceValidationException we) {
            assertEquals("The container view with key \"Containers\" is associated with a software system (id=2), but that element does not exist in the model.", we.getMessage());
        }
    }

    @Test
    public void test_exceptionThrown_WhenContainerAssociatedWithComponentViewIsMissingFromTheModel() throws Exception {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File(PATH_TO_WORKSPACE_FILES, "ContainerAssociatedWithComponentViewIsMissingFromTheModel.json"));
            fail();
        } catch (WorkspaceValidationException we) {
            assertEquals("The component view with key \"Components\" is associated with a container (id=3), but that element does not exist in the model.", we.getMessage());
        }
    }

    @Test
    public void test_exceptionThrown_WhenElementAssociatedWithDynamicViewIsMissingFromTheModel() throws Exception {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File(PATH_TO_WORKSPACE_FILES, "ElementAssociatedWithDynamicViewIsMissingFromTheModel.json"));
            fail();
        } catch (WorkspaceValidationException we) {
            assertEquals("The dynamic view with key \"Dynamic\" is associated with an element (id=2), but that element does not exist in the model.", we.getMessage());
        }
    }

    @Test
    public void test_exceptionThrown_WhenSoftwareSystemAssociatedWithDeploymentViewIsMissingFromTheModel() throws Exception {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File(PATH_TO_WORKSPACE_FILES, "SoftwareSystemAssociatedWithDeploymentViewIsMissingFromTheModel.json"));
            fail();
        } catch (WorkspaceValidationException we) {
            assertEquals("The deployment view with key \"Deployment\" is associated with a software system (id=2), but that element does not exist in the model.", we.getMessage());
        }
    }

    @Test
    public void test_exceptionThrown_WhenViewAssociatedWithFilteredViewIsMissingFromTheWorkspace() throws Exception {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File(PATH_TO_WORKSPACE_FILES, "ViewAssociatedWithFilteredViewIsMissingFromTheWorkspace.json"));
            fail();
        } catch (WorkspaceValidationException we) {
            assertEquals("The filtered view with key \"Filtered\" is based upon a view (key=SystemContext), but that view does not exist in the workspace.", we.getMessage());
        }
    }

    @Test
    public void test_exceptionThrown_WhenElementReferencedByViewIsMissingFromTheModel() throws Exception {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File(PATH_TO_WORKSPACE_FILES, "ElementReferencedByViewIsMissingFromTheModel.json"));
            fail();
        } catch (WorkspaceValidationException we) {
            assertEquals("The view with key \"SystemLandscape\" references an element (id=2), but that element does not exist in the model.", we.getMessage());
        }
    }

    @Test
    public void test_exceptionThrown_WhenRelationshipReferencedByViewIsMissingFromTheModel() throws Exception {
        try {
            WorkspaceUtils.loadWorkspaceFromJson(new File(PATH_TO_WORKSPACE_FILES, "RelationshipReferencedByViewIsMissingFromTheModel.json"));
            fail();
        } catch (WorkspaceValidationException we) {
            assertEquals("The view with key \"SystemLandscape\" references a relationship (id=4), but that relationship does not exist in the model.", we.getMessage());
        }
    }

}