package com.structurizr.view;

import com.structurizr.Workspace;
import com.structurizr.model.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class ViewSetTests {

    private Workspace createWorkspace() {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();

        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Person person = model.addPerson("Person", "Description");
        person.uses(softwareSystem, "Uses");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        Component component = container.addComponent("Component", "Description", "Technology");

        DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "Description", "Technology");
        ContainerInstance containerInstance = deploymentNode.add(container);

        return workspace;
    }

    @Test
    void createCustomView_GeneratesAKey_WhenANullKeyIsSpecified() {
        View view = new Workspace("", "").getViews().createCustomView(null, "Title", "Description");
        assertEquals("Custom-001", view.getKey());
        assertTrue(view.isGeneratedKey());
    }

    @Test
    void createCustomView_GeneratesAKey_WhenAnEmptyKeyIsSpecified() {
        View view = new Workspace("", "").getViews().createCustomView(" ", "Title", "Description");
        assertEquals("Custom-001", view.getKey());
        assertTrue(view.isGeneratedKey());
    }

    @Test
    void createCustomView_ThrowsAnException_WhenADuplicateKeyIsSpecified() {
        try {
            Workspace workspace = new Workspace("Name", "Description");
            workspace.getViews().createCustomView("key", "Title", "Description");
            workspace.getViews().createCustomView("key", "Title", "Description");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A view with the key key already exists.", iae.getMessage());
        }
    }

    @Test
    void createCustomView_DoesNotThrowAnException_WhenUniqueKeysAreSpecified() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getViews().createCustomView("key1", "Title", "Description");
        workspace.getViews().createCustomView("key2", "Title", "Description");
    }

    @Test
    void createCustomView() {
        Workspace workspace = new Workspace("Name", "Description");
        CustomView customView = workspace.getViews().createCustomView("key", "Title", "Description");
        assertEquals("key", customView.getKey());
        assertEquals("Title", customView.getTitle());
        assertEquals("Description", customView.getDescription());

        assertEquals(1, workspace.getViews().getCustomViews().size());
    }

    @Test
    void createSystemLandscapeView_GeneratesAKey_WhenANullKeyIsSpecified() {
        SystemLandscapeView view = new Workspace("", "").getViews().createSystemLandscapeView(null, "Description");
        assertEquals("SystemLandscape-001", view.getKey());
        assertTrue(view.isGeneratedKey());
    }

    @Test
    void createSystemLandscapeView_GeneratesAKey_WhenAnEmptyKeyIsSpecified() {
        SystemLandscapeView view = new Workspace("", "").getViews().createSystemLandscapeView(" ", "Description");
        assertEquals("SystemLandscape-001", view.getKey());
        assertTrue(view.isGeneratedKey());
    }

    @Test
    void createSystemLandscapeView_ThrowsAnException_WhenADuplicateKeyIsSpecified() {
        try {
            Workspace workspace = new Workspace("Name", "Description");
            workspace.getViews().createSystemLandscapeView("key", "Description");
            workspace.getViews().createSystemLandscapeView("key", "Description");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A view with the key key already exists.", iae.getMessage());
        }
    }

    @Test
    void createSystemLandscapeView_DoesNotThrowAnException_WhenUniqueKeysAreSpecified() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getViews().createSystemLandscapeView("key1", "Description");
        workspace.getViews().createSystemLandscapeView("key2", "Description");
    }

    @Test
    void createSystemLandscapeView() {
        Workspace workspace = new Workspace("Name", "Description");
        SystemLandscapeView systemLandscapeView = workspace.getViews().createSystemLandscapeView("key", "Description");
        assertEquals("key", systemLandscapeView.getKey());
        assertEquals("Description", systemLandscapeView.getDescription());
    }

    @Test
    void createSystemContextView_ThrowsAnException_WhenASoftwareSystemIsSpecified() {
        try {
            new Workspace("", "").getViews().createSystemContextView(null, null, "Description");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A software system must be specified.", iae.getMessage());
        }
    }

    @Test
    void createSystemContextView_GeneratesAKey_WhenANullKeyIsSpecified() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
        SystemContextView view = workspace.getViews().createSystemContextView(softwareSystem, null, "Description");
        assertEquals("SystemContext-001", view.getKey());
        assertTrue(view.isGeneratedKey());
    }

    @Test
    void createSystemContextView_GeneratesAKey_WhenAnEmptyKeyIsSpecified() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
        SystemContextView view = workspace.getViews().createSystemContextView(softwareSystem, " ", "Description");
        assertEquals("SystemContext-001", view.getKey());
        assertTrue(view.isGeneratedKey());
    }

    @Test
    void createSystemContextView_ThrowsAnException_WhenADuplicateKeyIsSpecified() {
        try {
            Workspace workspace = new Workspace("Name", "Description");
            SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
            workspace.getViews().createSystemContextView(softwareSystem, "key", "Description");
            workspace.getViews().createSystemContextView(softwareSystem, "key", "Description");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A view with the key key already exists.", iae.getMessage());
        }
    }

    @Test
    void createSystemContextView() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
        SystemContextView systemContextView = workspace.getViews().createSystemContextView(softwareSystem, "key", "Description");
        assertEquals("key", systemContextView.getKey());
        assertEquals("Description", systemContextView.getDescription());
        assertSame(softwareSystem, systemContextView.getSoftwareSystem());
    }

    @Test
    void createContainerView_ThrowsAnException_WhenASoftwareSystemIsSpecified() {
        try {
            new Workspace("", "").getViews().createContainerView(null, null, "Description");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A software system must be specified.", iae.getMessage());
        }
    }

    @Test
    void createContainerView_GeneratesAKey_WhenANullKeyIsSpecified() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
        ContainerView view = workspace.getViews().createContainerView(softwareSystem, null, "Description");
        assertEquals("Container-001", view.getKey());
        assertTrue(view.isGeneratedKey());
    }

    @Test
    void createContainerView_GeneratesAKey_WhenAnEmptyKeyIsSpecified() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
        ContainerView view = workspace.getViews().createContainerView(softwareSystem, " ", "Description");
        assertEquals("Container-001", view.getKey());
        assertTrue(view.isGeneratedKey());
    }

    @Test
    void createContainerView_ThrowsAnException_WhenADuplicateKeyIsSpecified() {
        try {
            Workspace workspace = new Workspace("Name", "Description");
            SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
            workspace.getViews().createContainerView(softwareSystem, "key", "Description");
            workspace.getViews().createContainerView(softwareSystem, "key", "Description");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A view with the key key already exists.", iae.getMessage());
        }
    }

    @Test
    void createContainerView() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
        ContainerView containerView = workspace.getViews().createContainerView(softwareSystem, "key", "Description");
        assertEquals("key", containerView.getKey());
        assertEquals("Description", containerView.getDescription());
        assertSame(softwareSystem, containerView.getSoftwareSystem());
    }

    @Test
    void createComponentView_ThrowsAnException_WhenASoftwareSystemIsSpecified() {
        try {
            new Workspace("", "").getViews().createComponentView(null, null, "Description");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A container must be specified.", iae.getMessage());
        }
    }

    @Test
    void createComponentView_GeneratesAKey_WhenANullKeyIsSpecified() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        ComponentView view = workspace.getViews().createComponentView(container, null, "Description");
        assertEquals("Component-001", view.getKey());
        assertTrue(view.isGeneratedKey());
    }

    @Test
    void createComponentView_GeneratesAKey_WhenAnEmptyKeyIsSpecified() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        ComponentView view = workspace.getViews().createComponentView(container, " ", "Description");
        assertEquals("Component-001", view.getKey());
        assertTrue(view.isGeneratedKey());
    }

    @Test
    void createComponentView_ThrowsAnException_WhenADuplicateKeyIsSpecified() {
        try {
            Workspace workspace = new Workspace("Name", "Description");
            SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
            Container container = softwareSystem.addContainer("Container", "Description", "Technology");
            workspace.getViews().createComponentView(container, "key", "Description");
            workspace.getViews().createComponentView(container, "key", "Description");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A view with the key key already exists.", iae.getMessage());
        }
    }

    @Test
    void createComponentView() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        ComponentView componentView = workspace.getViews().createComponentView(container, "key", "Description");
        assertEquals("key", componentView.getKey());
        assertEquals("Description", componentView.getDescription());
        assertSame(softwareSystem, componentView.getSoftwareSystem());
        assertSame(container, componentView.getContainer());
    }

    @Test
    void createDynamicView_GeneratesAKey_WhenANullKeyIsSpecified() {
        Workspace workspace = new Workspace("Name", "Description");
        DynamicView view = workspace.getViews().createDynamicView(null);
        assertEquals("Dynamic-001", view.getKey());
        assertTrue(view.isGeneratedKey());
    }

    @Test
    void createDynamicView_GeneratesAKey_WhenAnEmptyKeyIsSpecified() {
        Workspace workspace = new Workspace("Name", "Description");
        DynamicView view = workspace.getViews().createDynamicView(" ", "Description");
        assertEquals("Dynamic-001", view.getKey());
        assertTrue(view.isGeneratedKey());
    }

    @Test
    void createDynamicView() {
        Workspace workspace = new Workspace("Name", "Description");

        DynamicView dynamicView = workspace.getViews().createDynamicView("key", "Description");
        assertEquals("key", dynamicView.getKey());
        assertEquals("Description", dynamicView.getDescription());
        assertNull(dynamicView.getSoftwareSystem());
        assertNull(dynamicView.getElement());
    }

    @Test
    void createDynamicViewForASoftwareSystem_ThrowsAnException_WhenANullSoftwareSystemIsSpecified() {
        try {
            new Workspace("", "").getViews().createDynamicView((SoftwareSystem) null, "key", "Description");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A software system must be specified.", iae.getMessage());
        }
    }

    @Test
    void createDynamicViewForASoftwareSystem_GeneratesAKey_WhenANullKeyIsSpecified() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
        DynamicView view = workspace.getViews().createDynamicView(softwareSystem, null, "Description");
        assertEquals("Dynamic-001", view.getKey());
        assertTrue(view.isGeneratedKey());
    }

    @Test
    void createDynamicViewForASoftwareSystem_GeneratesAKey_WhenAnEmptyKeyIsSpecified() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
        DynamicView view = workspace.getViews().createDynamicView(softwareSystem, " ", "Description");
        assertEquals("Dynamic-001", view.getKey());
        assertTrue(view.isGeneratedKey());
    }

    @Test
    void createDynamicViewForASoftwareSystem_ThrowsAnException_WhenADuplicateKeyIsUsed() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");

        workspace.getViews().createDeploymentView(softwareSystem, "dynamic", "Description");
        try {
            workspace.getViews().createDeploymentView(softwareSystem, "dynamic", "Description");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A view with the key dynamic already exists.", iae.getMessage());
        }
    }

    @Test
    void createDynamicViewForSoftwareSystem() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");

        DynamicView dynamicView = workspace.getViews().createDynamicView(softwareSystem, "key", "Description");
        assertEquals("key", dynamicView.getKey());
        assertEquals("Description", dynamicView.getDescription());
        assertSame(softwareSystem, dynamicView.getSoftwareSystem());
        assertSame(softwareSystem, dynamicView.getElement());
    }

    @Test
    void createDynamicViewForAContainer_ThrowsAnException_WhenANullContainerIsSpecified() {
        try {
            new Workspace("", "").getViews().createDynamicView((Container) null, "key", "Description");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A container must be specified.", iae.getMessage());
        }
    }

    @Test
    void createDynamicViewForAContainer_GeneratesAKey_WhenANullKeyIsSpecified() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        DynamicView view = workspace.getViews().createDynamicView(container, null, "Description");
        assertEquals("Dynamic-001", view.getKey());
        assertTrue(view.isGeneratedKey());
    }

    @Test
    void createDynamicViewForAContainer_GeneratesAKey_WhenAnEmptyKeyIsSpecified() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        DynamicView view = workspace.getViews().createDynamicView(container, " ", "Description");
        assertEquals("Dynamic-001", view.getKey());
        assertTrue(view.isGeneratedKey());
    }

    @Test
    void createDynamicViewForContainer() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");

        DynamicView dynamicView = workspace.getViews().createDynamicView(container, "key", "Description");
        assertEquals("key", dynamicView.getKey());
        assertEquals("Description", dynamicView.getDescription());
        assertSame(softwareSystem, dynamicView.getSoftwareSystem());
        assertSame(container, dynamicView.getElement());
    }

    @Test
    void createDeploymentView_GeneratesAKey_WhenANullKeyIsSpecified() {
        Workspace workspace = new Workspace("Name", "Description");
        DeploymentView view = workspace.getViews().createDeploymentView(null);
        assertEquals("Deployment-001", view.getKey());
        assertTrue(view.isGeneratedKey());
    }

    @Test
    void createDeploymentView_GeneratesAKey_WhenAnEmptyKeyIsSpecified() {
        Workspace workspace = new Workspace("Name", "Description");
        DeploymentView view = workspace.getViews().createDeploymentView(" ", "Description");
        assertEquals("Deployment-001", view.getKey());
        assertTrue(view.isGeneratedKey());
    }

    @Test
    void createDeploymentView_ThrowsAnException_WhenADuplicateKeyIsUsed() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");

        workspace.getViews().createDeploymentView(softwareSystem, "deployment", "Description");
        try {
            workspace.getViews().createDeploymentView(softwareSystem, "deployment", "Description");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A view with the key deployment already exists.", iae.getMessage());
        }
    }

    @Test
    void createDeploymentView() {
        Workspace workspace = new Workspace("Name", "Description");

        DeploymentView deploymentView = workspace.getViews().createDeploymentView("key", "Description");
        assertEquals("key", deploymentView.getKey());
        assertEquals("Description", deploymentView.getDescription());
        assertNull(deploymentView.getSoftwareSystem());
    }

    @Test
    void createDeploymentViewForASoftwareSystem_ThrowsAnException_WhenANullSoftwareSystemIsSpecified() {
        try {
            new Workspace("", "").getViews().createDeploymentView((SoftwareSystem) null, "key", "Description");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A software system must be specified.", iae.getMessage());
        }
    }

    @Test
    void createDeploymentViewForASoftwareSystem_GeneratesAKey_WhenANullKeyIsSpecified() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
        DeploymentView view = workspace.getViews().createDeploymentView(softwareSystem, null, "Description");
        assertEquals("Deployment-001", view.getKey());
        assertTrue(view.isGeneratedKey());
    }

    @Test
    void createDeploymentViewForASoftwareSystem_GeneratesAKey_WhenAnEmptyKeyIsSpecified() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
        DeploymentView view = workspace.getViews().createDeploymentView(softwareSystem, " ", "Description");
        assertEquals("Deployment-001", view.getKey());
        assertTrue(view.isGeneratedKey());
    }

    @Test
    void createDeploymentViewForASoftwareSystem_ThrowsAnException_WhenADuplicateKeyIsUsed() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");

        workspace.getViews().createDeploymentView(softwareSystem, "deployment", "Description");
        try {
            workspace.getViews().createDeploymentView(softwareSystem, "deployment", "Description");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A view with the key deployment already exists.", iae.getMessage());
        }
    }

    @Test
    void createDeploymentViewForSoftwareSystem() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");

        DeploymentView deploymentView = workspace.getViews().createDeploymentView(softwareSystem, "key", "Description");
        assertEquals("key", deploymentView.getKey());
        assertEquals("Description", deploymentView.getDescription());
        assertSame(softwareSystem, deploymentView.getSoftwareSystem());
    }

    @Test
    void createFilteredView_ThrowsAnException_WhenANullViewIsSpecified() {
        try {
            Workspace workspace = new Workspace("Name", "Description");
            workspace.getViews().createFilteredView((SystemLandscapeView)null, "key", "Description", FilterMode.Include, "tag1", "tag2");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A view must be specified.", iae.getMessage());
        }
    }

    @Test
    void createFilteredView_GeneratesAKey_WhenANullKeyIsSpecified() {
        Workspace workspace = new Workspace("Name", "Description");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("systemLandscape", "Description");
        FilteredView filteredView = workspace.getViews().createFilteredView(view, null, "Description", FilterMode.Include, "tag1", "tag2");
        assertEquals("Filtered-001", filteredView.getKey());
        assertTrue(filteredView.isGeneratedKey());
    }

    @Test
    void createFilteredView_GeneratesAKey_WhenAnEmptyKeyIsSpecified() {
        Workspace workspace = new Workspace("Name", "Description");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("systemLandscape", "Description");
        FilteredView filteredView = workspace.getViews().createFilteredView(view, " ", "Description", FilterMode.Include, "tag1", "tag2");
        assertEquals("Filtered-001", filteredView.getKey());
        assertTrue(filteredView.isGeneratedKey());
    }

    @Test
    void createFilteredView_ThrowsAnException_WhenADuplicateKeyIsUsed() {
        Workspace workspace = new Workspace("Name", "Description");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("systemLandscape", "Description");
        workspace.getViews().createFilteredView(view, "filtered", "Description", FilterMode.Include, "tag1", "tag2");
        try {
            workspace.getViews().createFilteredView(view, "filtered", "Description", FilterMode.Include, "tag1", "tag2");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A view with the key filtered already exists.", iae.getMessage());
        }
    }

    @Test
    void createFilteredView_OnStaticView() {
        Workspace workspace = new Workspace("Name", "Description");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("systemLandscape", "Description");
        FilteredView filteredView = workspace.getViews().createFilteredView(view, "key", "Description", FilterMode.Include, "tag1", "tag2");

        assertEquals("key", filteredView.getKey());
        assertEquals("Description", filteredView.getDescription());
        assertEquals(FilterMode.Include, filteredView.getMode());
        assertEquals(2, filteredView.getTags().size());
        assertTrue(filteredView.getTags().contains("tag1"));
        assertTrue(filteredView.getTags().contains("tag2"));
    }

    @Test
    void createFilteredView_OnDeploymentView() {
        Workspace workspace = new Workspace("Name", "Description");
        DeploymentView view = workspace.getViews().createDeploymentView("deployment");
        FilteredView filteredView = workspace.getViews().createFilteredView(view, "key", "Description", FilterMode.Include, "tag1", "tag2");

        assertEquals("key", filteredView.getKey());
        assertEquals("Description", filteredView.getDescription());
        assertEquals(FilterMode.Include, filteredView.getMode());
        assertEquals(2, filteredView.getTags().size());
        assertTrue(filteredView.getTags().contains("tag1"));
        assertTrue(filteredView.getTags().contains("tag2"));
    }

    @Test
    void copyLayoutInformationFrom_WhenAViewKeyIsNotSetButTheViewTitlesMatch() {
        Workspace workspace1 = createWorkspace();
        SoftwareSystem softwareSystem1 = workspace1.getModel().getSoftwareSystemWithName("Software System");
        SystemContextView view1 = workspace1.getViews().createSystemContextView(softwareSystem1, "context", "Description");
        view1.setKey(null); // this simulates views created by previous versions of the client library
        view1.addAllElements();
        view1.getElements().iterator().next().setX(100);
        view1.setPaperSize(PaperSize.A3_Landscape);

        Workspace workspace2 = createWorkspace();
        SoftwareSystem softwareSystem2 = workspace2.getModel().getSoftwareSystemWithName("Software System");
        SystemContextView view2 = workspace2.getViews().createSystemContextView(softwareSystem2, "context", "Description");
        view2.setKey(null); // this simulates views created by previous versions of the client library
        view2.addAllElements();

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(100, view2.getElements().iterator().next().getX());
        assertEquals(PaperSize.A3_Landscape, view2.getPaperSize());
    }

    @Test
    void copyLayoutInformationFrom_WhenAViewKeyHasBeenIntroduced() {
        Workspace workspace1 = createWorkspace();
        SoftwareSystem softwareSystem1 = workspace1.getModel().getSoftwareSystemWithName("Software System");
        SystemContextView view1 = workspace1.getViews().createSystemContextView(softwareSystem1, "context", "Description");
        view1.setKey(null); // this simulates views created by previous versions of the client library
        view1.addAllElements();
        view1.getElements().iterator().next().setX(100);
        view1.setPaperSize(PaperSize.A3_Landscape);

        Workspace workspace2 = createWorkspace();
        SoftwareSystem softwareSystem2 = workspace2.getModel().getSoftwareSystemWithName("Software System");
        SystemContextView view2 = workspace2.getViews().createSystemContextView(softwareSystem2, "context", "Description");
        view2.addAllElements();

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(100, view2.getElements().iterator().next().getX());
        assertEquals(PaperSize.A3_Landscape, view2.getPaperSize());
    }

    @Test
    void copyLayoutInformationFrom_IgnoresThePaperSize_WhenThePaperSizeIsSet() {
        Workspace workspace1 = createWorkspace();
        SoftwareSystem softwareSystem1 = workspace1.getModel().getSoftwareSystemWithName("Software System");
        SystemContextView view1 = workspace1.getViews().createSystemContextView(softwareSystem1, "context", "Description");
        view1.setPaperSize(PaperSize.A3_Landscape);

        Workspace workspace2 = createWorkspace();
        SoftwareSystem softwareSystem2 = workspace2.getModel().getSoftwareSystemWithName("Software System");
        SystemContextView view2 = workspace2.getViews().createSystemContextView(softwareSystem2, "context", "Description");
        view2.setPaperSize(PaperSize.A5_Portrait);

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(PaperSize.A5_Portrait, view2.getPaperSize());
    }

    @Test
    void copyLayoutInformationFrom_CopiesThePaperSize_WhenThePaperSizeIsNotSet() {
        Workspace workspace1 = createWorkspace();
        SoftwareSystem softwareSystem1 = workspace1.getModel().getSoftwareSystemWithName("Software System");
        SystemContextView view1 = workspace1.getViews().createSystemContextView(softwareSystem1, "context", "Description");
        view1.setPaperSize(PaperSize.A3_Landscape);

        Workspace workspace2 = createWorkspace();
        SoftwareSystem softwareSystem2 = workspace2.getModel().getSoftwareSystemWithName("Software System");
        SystemContextView view2 = workspace2.getViews().createSystemContextView(softwareSystem2, "context", "Description");

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(PaperSize.A3_Landscape, view2.getPaperSize());
    }

    @Test
    void copyLayoutInformationFrom_WhenTheSystemLandscapeViewKeysMatch() {
        Workspace workspace1 = createWorkspace();
        SoftwareSystem softwareSystem1 = workspace1.getModel().getSoftwareSystemWithName("Software System");
        SystemLandscapeView view1 = workspace1.getViews().createSystemLandscapeView("landscape", "Description");
        view1.addAllElements();
        view1.getElements().iterator().next().setX(100);
        view1.setPaperSize(PaperSize.A3_Landscape);

        Workspace workspace2 = createWorkspace();
        SoftwareSystem softwareSystem2 = workspace2.getModel().getSoftwareSystemWithName("Software System");
        SystemLandscapeView view2 = workspace2.getViews().createSystemLandscapeView("context", "Description");
        view2.addAllElements();

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(100, view2.getElements().iterator().next().getX());
        assertEquals(PaperSize.A3_Landscape, view2.getPaperSize());
    }

    @Test
    void copyLayoutInformationFrom_DoesNotDoAnythingIfThereIsNoSystemLandscapeViewToCopyInformationFrom() {
        Workspace workspace1 = createWorkspace();

        Workspace workspace2 = createWorkspace();
        SoftwareSystem softwareSystem2 = workspace2.getModel().getSoftwareSystemWithName("Software System");
        SystemLandscapeView view2 = workspace2.getViews().createSystemLandscapeView("landscape", "Description");
        view2.addAllElements();

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(0, view2.getElements().iterator().next().getX()); // default
        assertNull(view2.getPaperSize()); // default
    }

    @Test
    void copyLayoutInformationFrom_WhenTheSystemContextViewKeysMatch() {
        Workspace workspace1 = createWorkspace();
        SoftwareSystem softwareSystem1 = workspace1.getModel().getSoftwareSystemWithName("Software System");
        SystemContextView view1 = workspace1.getViews().createSystemContextView(softwareSystem1, "context", "Description");
        view1.addAllElements();
        view1.getElements().iterator().next().setX(100);
        view1.setPaperSize(PaperSize.A3_Landscape);

        Workspace workspace2 = createWorkspace();
        SoftwareSystem softwareSystem2 = workspace2.getModel().getSoftwareSystemWithName("Software System");
        SystemContextView view2 = workspace2.getViews().createSystemContextView(softwareSystem2, "context", "Description");
        view2.addAllElements();

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(100, view2.getElements().iterator().next().getX());
        assertEquals(PaperSize.A3_Landscape, view2.getPaperSize());
    }

    @Test
    void copyLayoutInformationFrom_DoesNotDoAnythingIfThereIsNoSystemContextViewToCopyInformationFrom() {
        Workspace workspace1 = createWorkspace();

        Workspace workspace2 = createWorkspace();
        SoftwareSystem softwareSystem2 = workspace2.getModel().getSoftwareSystemWithName("Software System");
        SystemContextView view2 = workspace2.getViews().createSystemContextView(softwareSystem2, "context", "Description");
        view2.addAllElements();

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(0, view2.getElements().iterator().next().getX()); // default
        assertNull(view2.getPaperSize()); // default
    }

    @Test
    void copyLayoutInformationFrom_WhenTheContainerViewKeysMatch() {
        Workspace workspace1 = createWorkspace();
        SoftwareSystem softwareSystem1 = workspace1.getModel().getSoftwareSystemWithName("Software System");
        ContainerView view1 = workspace1.getViews().createContainerView(softwareSystem1, "containers", "Description");
        view1.addAllElements();
        view1.getElements().iterator().next().setX(100);
        view1.setPaperSize(PaperSize.A3_Landscape);

        Workspace workspace2 = createWorkspace();
        SoftwareSystem softwareSystem2 = workspace2.getModel().getSoftwareSystemWithName("Software System");
        ContainerView view2 = workspace2.getViews().createContainerView(softwareSystem2, "containers", "Description");
        view2.addAllElements();

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(100, view2.getElements().iterator().next().getX());
        assertEquals(PaperSize.A3_Landscape, view2.getPaperSize());
    }

    @Test
    void copyLayoutInformationFrom_DoesNotDoAnythingIfThereIsNoContainerViewToCopyInformationFrom() {
        Workspace workspace1 = createWorkspace();

        Workspace workspace2 = createWorkspace();
        SoftwareSystem softwareSystem2 = workspace2.getModel().getSoftwareSystemWithName("Software System");
        ContainerView view2 = workspace2.getViews().createContainerView(softwareSystem2, "containers", "Description");
        view2.addAllElements();

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(0, view2.getElements().iterator().next().getX()); // default
        assertNull(view2.getPaperSize()); // default
    }

    @Test
    void copyLayoutInformationFrom_WhenTheComponentViewKeysMatch() {
        Workspace workspace1 = createWorkspace();
        Container container1 = workspace1.getModel().getSoftwareSystemWithName("Software System").getContainerWithName("Container");
        ComponentView view1 = workspace1.getViews().createComponentView(container1, "containers", "Description");
        view1.addAllElements();
        view1.getElements().iterator().next().setX(100);
        view1.setPaperSize(PaperSize.A3_Landscape);

        Workspace workspace2 = createWorkspace();
        Container container2 = workspace2.getModel().getSoftwareSystemWithName("Software System").getContainerWithName("Container");
        ComponentView view2 = workspace2.getViews().createComponentView(container2, "containers", "Description");
        view2.addAllElements();

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(100, view2.getElements().iterator().next().getX());
        assertEquals(PaperSize.A3_Landscape, view2.getPaperSize());
    }

    @Test
    void copyLayoutInformationFrom_DoesNotDoAnythingIfThereIsNoComponentViewToCopyInformationFrom() {
        Workspace workspace1 = createWorkspace();

        Workspace workspace2 = createWorkspace();
        Container container2 = workspace2.getModel().getSoftwareSystemWithName("Software System").getContainerWithName("Container");
        ComponentView view2 = workspace2.getViews().createComponentView(container2, "components", "Description");
        view2.addAllElements();

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(0, view2.getElements().iterator().next().getX()); // default
        assertNull(view2.getPaperSize()); // default
    }

    @Test
    void copyLayoutInformationFrom_WhenTheDynamicViewKeysMatch() {
        Workspace workspace1 = createWorkspace();
        Person person1 = workspace1.getModel().getPersonWithName("Person");
        SoftwareSystem softwareSystem1 = workspace1.getModel().getSoftwareSystemWithName("Software System");
        DynamicView view1 = workspace1.getViews().createDynamicView("context", "Description");
        view1.add(person1, softwareSystem1);
        view1.getElements().iterator().next().setX(100);
        view1.setPaperSize(PaperSize.A3_Landscape);

        Workspace workspace2 = createWorkspace();
        Person person2 = workspace2.getModel().getPersonWithName("Person");
        SoftwareSystem softwareSystem2 = workspace2.getModel().getSoftwareSystemWithName("Software System");
        DynamicView view2 = workspace2.getViews().createDynamicView("context", "Description");
        view2.add(person2, softwareSystem2);

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(100, view2.getElements().iterator().next().getX());
        assertEquals(PaperSize.A3_Landscape, view2.getPaperSize());
    }

    @Test
    void copyLayoutInformationFrom_DoesNotDoAnythingIfThereIsNoDynamicViewToCopyInformationFrom() {
        Workspace workspace1 = createWorkspace();

        Workspace workspace2 = createWorkspace();
        Person person2 = workspace2.getModel().getPersonWithName("Person");
        SoftwareSystem softwareSystem2 = workspace2.getModel().getSoftwareSystemWithName("Software System");
        DynamicView view2 = workspace2.getViews().createDynamicView("context", "Description");
        view2.add(person2, softwareSystem2);

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(0, view2.getElements().iterator().next().getX()); // default
        assertNull(view2.getPaperSize()); // default
    }

    @Test
    void copyLayoutInformationFrom_WhenTheDeploymentViewKeysMatch() {
        Workspace workspace1 = createWorkspace();
        DeploymentNode deploymentNode1 = workspace1.getModel().getDeploymentNodeWithName("Deployment Node");
        DeploymentView view1 = workspace1.getViews().createDeploymentView("key", "Description");
        view1.add(deploymentNode1);
        view1.getElements().stream().filter(ev -> ev.getElement() instanceof ContainerInstance).findFirst().get().setX(100);
        view1.getElements().stream().filter(ev -> ev.getElement() instanceof ContainerInstance).findFirst().get().setY(200);
        view1.setPaperSize(PaperSize.A3_Landscape);

        Workspace workspace2 = createWorkspace();
        DeploymentNode deploymentNode2 = workspace2.getModel().getDeploymentNodeWithName("Deployment Node");
        DeploymentView view2 = workspace2.getViews().createDeploymentView("key", "Description");
        view2.add(deploymentNode2);

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(100, view2.getElements().stream().filter(ev -> ev.getElement() instanceof ContainerInstance).findFirst().get().getX());
        assertEquals(200, view2.getElements().stream().filter(ev -> ev.getElement() instanceof ContainerInstance).findFirst().get().getY());
        assertEquals(PaperSize.A3_Landscape, view2.getPaperSize());
    }

    @Test
    void copyLayoutInformationFrom_DoesNotDoAnythingIfThereIsNoDeploymentViewToCopyInformationFrom() {
        Workspace workspace1 = createWorkspace();

        Workspace workspace2 = createWorkspace();
        DeploymentNode deploymentNode2 = workspace2.getModel().getDeploymentNodeWithName("Deployment Node");
        DeploymentView view2 = workspace2.getViews().createDeploymentView("key", "Description");
        view2.add(deploymentNode2);

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(0, view2.getElements().stream().filter(ev -> ev.getElement() instanceof ContainerInstance).findFirst().get().getX()); // default
        assertEquals(0, view2.getElements().stream().filter(ev -> ev.getElement() instanceof ContainerInstance).findFirst().get().getY()); // default
        assertNull(view2.getPaperSize()); // default
    }

    private HashSet<ElementView> elementViewsFor(Element... elements) {
        HashSet<ElementView> set = new HashSet<>();

        for (Element element : elements) {
            ElementView elementView = new ElementView();
            elementView.setId(element.getId());
            set.add(elementView);
        }

        return set;
    }

    private HashSet<RelationshipView> relationshipViewsFor(Relationship... relationships) {
        HashSet<RelationshipView> set = new HashSet<>();

        for (Relationship relationship : relationships) {
            RelationshipView relationshipView = new RelationshipView();
            relationshipView.setId(relationship.getId());
            set.add(relationshipView);
        }

        return set;
    }

    @Test
    void hydrate() {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();
        ViewSet views = workspace.getViews();
        Person person = model.addPerson("Person", "Description");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        Component component = container.addComponent("Component", "Description", "Technology");
        Relationship personUsesSoftwareSystemRelationship = person.uses(softwareSystem, "uses");
        DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "Description", "Technology");
        ContainerInstance containerInstance = deploymentNode.add(container);

        SystemLandscapeView systemLandscapeView = new SystemLandscapeView();
        systemLandscapeView.setKey("systemLandscape"); // this is used for the filtered view below
        systemLandscapeView.setElements(elementViewsFor(person, softwareSystem));
        systemLandscapeView.setRelationships(relationshipViewsFor(personUsesSoftwareSystemRelationship));
        views.setSystemLandscapeViews(Collections.singleton(systemLandscapeView));

        SystemContextView systemContextView = new SystemContextView();
        systemContextView.setKey("systemContext");
        systemContextView.setSoftwareSystemId(softwareSystem.getId());
        systemContextView.setElements(elementViewsFor(softwareSystem));
        views.setSystemContextViews(Collections.singleton(systemContextView));

        ContainerView containerView = new ContainerView();
        containerView.setKey("containers");
        containerView.setSoftwareSystemId(softwareSystem.getId());
        containerView.setElements(elementViewsFor(container));
        views.setContainerViews(Collections.singleton(containerView));

        ComponentView componentView = new ComponentView();
        componentView.setKey("components");
        componentView.setSoftwareSystemId(softwareSystem.getId());
        componentView.setContainerId(container.getId());
        componentView.setElements(elementViewsFor(component));
        views.setComponentViews(Collections.singleton(componentView));

        DynamicView dynamicView = new DynamicView();
        dynamicView.setKey("dynamic");
        dynamicView.setElementId(softwareSystem.getId());
        dynamicView.setElements(elementViewsFor(component));
        views.setDynamicViews(Collections.singleton(dynamicView));

        DeploymentView deploymentView = new DeploymentView();
        deploymentView.setKey("deployment");
        deploymentView.setSoftwareSystemId(softwareSystem.getId());
        deploymentView.setElements(elementViewsFor(deploymentNode, containerInstance));
        views.setDeploymentViews(Collections.singleton(deploymentView));

        FilteredView filteredView = new FilteredView();
        filteredView.setKey("filtered");
        filteredView.setBaseViewKey(systemLandscapeView.getKey());
        views.setFilteredViews(Collections.singleton(filteredView));

        workspace.getViews().hydrate(model);

        assertSame(model, systemLandscapeView.getModel());
        assertSame(views, systemLandscapeView.getViewSet());
        assertSame(person, systemLandscapeView.getElementView(person).getElement());
        assertSame(softwareSystem, systemLandscapeView.getElementView(softwareSystem).getElement());
        assertSame(personUsesSoftwareSystemRelationship, systemLandscapeView.getRelationshipView(personUsesSoftwareSystemRelationship).getRelationship());

        assertSame(model, systemContextView.getModel());
        assertSame(views, systemContextView.getViewSet());
        assertSame(softwareSystem, systemContextView.getSoftwareSystem());
        assertSame(softwareSystem, systemContextView.getElementView(softwareSystem).getElement());

        assertSame(model, containerView.getModel());
        assertSame(views, containerView.getViewSet());
        assertSame(softwareSystem, containerView.getSoftwareSystem());
        assertSame(container, containerView.getElementView(container).getElement());

        assertSame(model, componentView.getModel());
        assertSame(views, componentView.getViewSet());
        assertSame(softwareSystem, componentView.getSoftwareSystem());
        assertSame(container, componentView.getContainer());
        assertSame(component, componentView.getElementView(component).getElement());

        assertSame(model, dynamicView.getModel());
        assertSame(views, dynamicView.getViewSet());
        assertSame(softwareSystem, dynamicView.getSoftwareSystem());
        assertSame(softwareSystem, dynamicView.getElement());
        assertSame(component, dynamicView.getElementView(component).getElement());

        assertSame(model, deploymentView.getModel());
        assertSame(views, deploymentView.getViewSet());
        assertSame(softwareSystem, deploymentView.getSoftwareSystem());
        assertSame(deploymentNode, deploymentView.getElementView(deploymentNode).getElement());
        assertSame(containerInstance, deploymentView.getElementView(containerInstance).getElement());

        assertSame(systemLandscapeView, filteredView.getView());
    }

    @Test
    void setEnterpriseContextViews_IsSupportedForOlderWorkspaces() {
        ViewSet views = new Workspace("", "").getViews();
        SystemLandscapeView systemLandscapeView = views.createSystemLandscapeView("key", "Description");
        views.setEnterpriseContextViews(Collections.singleton(systemLandscapeView));
        assertEquals(1, views.getSystemLandscapeViews().size());
        assertSame(systemLandscapeView, views.getSystemLandscapeViews().iterator().next());
    }

    @Test
    void createDefaultViews() {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();
        ViewSet views = workspace.getViews();

        SoftwareSystem ss1 = model.addSoftwareSystem("Software System 1");
        Container c1 = ss1.addContainer("Container 1", "", "");
        Component cc1 = c1.addComponent("Component 1", "", "");

        SoftwareSystem ss2 = model.addSoftwareSystem("Software System 2");
        Container c2 = ss2.addContainer("Container 2", "", "");
        Component cc2 = c2.addComponent("Component 2", "", "");

        DeploymentNode dev = model.addDeploymentNode("Development", "Developer Laptop", "", "");
        DeploymentNode live = model.addDeploymentNode("Live", "Amazon Web Services", "", "");
        DeploymentNode liveEc2 = live.addDeploymentNode("EC2");

        views.createDefaultViews();

        assertEquals(1, views.getSystemLandscapeViews().size());
        assertEquals("SystemLandscape-001", views.getSystemLandscapeViews().iterator().next().getKey());

        assertEquals(2, views.getSystemContextViews().size());
        assertSame(ss1, views.getSystemContextViews().stream().filter(v -> v.getKey().equals("SystemContext-001")).findFirst().get().getSoftwareSystem());
        assertSame(ss2, views.getSystemContextViews().stream().filter(v -> v.getKey().equals("SystemContext-002")).findFirst().get().getSoftwareSystem());

        assertEquals(2, views.getContainerViews().size());
        assertSame(ss1, views.getContainerViews().stream().filter(v -> v.getKey().equals("Container-001")).findFirst().get().getSoftwareSystem());
        assertSame(ss2, views.getContainerViews().stream().filter(v -> v.getKey().equals("Container-002")).findFirst().get().getSoftwareSystem());

        assertEquals(2, views.getComponentViews().size());
        assertSame(c1, views.getComponentViews().stream().filter(v -> v.getKey().equals("Component-001")).findFirst().get().getContainer());
        assertSame(c2, views.getComponentViews().stream().filter(v -> v.getKey().equals("Component-002")).findFirst().get().getContainer());

        assertEquals(0, views.getDynamicViews().size());
        assertEquals(0, views.getDeploymentViews().size());

        live.addInfrastructureNode("Route 53");

        views.clear();
        views.createDefaultViews();

        assertEquals(1, views.getDeploymentViews().size());
        assertSame("Live", views.getDeploymentViews().stream().filter(v -> v.getKey().equals("Deployment-001")).findFirst().get().getEnvironment());

        dev.add(ss1);
        liveEc2.add(c1);
        liveEc2.add(c2);

        views.clear();
        views.createDefaultViews();

        assertEquals(3, views.getDeploymentViews().size());
        assertSame(ss1, views.getDeploymentViews().stream().filter(v -> v.getKey().equals("Deployment-001")).findFirst().get().getSoftwareSystem());
        assertSame("Development", views.getDeploymentViews().stream().filter(v -> v.getKey().equals("Deployment-001")).findFirst().get().getEnvironment());
        assertSame(ss1, views.getDeploymentViews().stream().filter(v -> v.getKey().equals("Deployment-002")).findFirst().get().getSoftwareSystem());
        assertSame("Live", views.getDeploymentViews().stream().filter(v -> v.getKey().equals("Deployment-002")).findFirst().get().getEnvironment());
        assertSame(ss2, views.getDeploymentViews().stream().filter(v -> v.getKey().equals("Deployment-003")).findFirst().get().getSoftwareSystem());
        assertSame("Live", views.getDeploymentViews().stream().filter(v -> v.getKey().equals("Deployment-003")).findFirst().get().getEnvironment());
    }

    @Test
    void copyLayoutInformationFrom_DoesNothing_WhenMergeFromRemoteIsSetToFalse() {
        Workspace workspace1 = createWorkspace();
        SoftwareSystem softwareSystem1 = workspace1.getModel().getSoftwareSystemWithName("Software System");
        SystemLandscapeView view1 = workspace1.getViews().createSystemLandscapeView("landscape", "Description");
        view1.addAllElements();
        view1.getElements().iterator().next().setX(100);
        view1.setPaperSize(PaperSize.A3_Landscape);

        Workspace workspace2 = createWorkspace();
        SoftwareSystem softwareSystem2 = workspace2.getModel().getSoftwareSystemWithName("Software System");
        SystemLandscapeView view2 = workspace2.getViews().createSystemLandscapeView("context", "Description");
        view2.addAllElements();
        view2.setMergeFromRemote(false);

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(0, view2.getElements().iterator().next().getX());
        assertNull(view2.getPaperSize());
    }

    @Test
    void view_ordering() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        ViewSet views = workspace.getViews();

        CustomView customView = views.createCustomView("custom1", "Title", "Description");
        SystemLandscapeView systemLandscapeView1 = views.createSystemLandscapeView("landscape1", "Description");
        FilteredView filteredView = views.createFilteredView(systemLandscapeView1, "filtered1", "Description", FilterMode.Include, "Tag 1");
        SystemContextView systemContextView = views.createSystemContextView(softwareSystem, "context1", "Description");
        ContainerView containerView = views.createContainerView(softwareSystem, "container1", "Description");
        ComponentView componentView = views.createComponentView(container, "component1", "Description");
        DynamicView dynamicView = views.createDynamicView("dynamic1", "Description");
        DeploymentView deploymentView = views.createDeploymentView("deployment1", "Description");
        SystemLandscapeView systemLandscapeView2 = views.createSystemLandscapeView("landscape2", "Description");

        assertEquals(1, customView.getOrder());
        assertEquals(2, systemLandscapeView1.getOrder());
        assertEquals(3, filteredView.getOrder());
        assertEquals(4, systemContextView.getOrder());
        assertEquals(5, containerView.getOrder());
        assertEquals(6, componentView.getOrder());
        assertEquals(7, dynamicView.getOrder());
        assertEquals(8, deploymentView.getOrder());
        assertEquals(9, systemLandscapeView2.getOrder());
    }

    @Test
    public void createDefaultViews_ForSoftwareSystemsWithNamesUsingUTF8Characters() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem ss1 = workspace.getModel().addSoftwareSystem("English is fine");
        SoftwareSystem ss2 = workspace.getModel().addSoftwareSystem("Но люди говорят и на других языках");
        SoftwareSystem ss3 = workspace.getModel().addSoftwareSystem("英語だけが言語ではない");

        workspace.getViews().createDefaultViews();

        assertEquals(4, workspace.getViews().getViews().size());

        assertEquals(1, workspace.getViews().getSystemLandscapeViews().size());
        assertEquals("SystemLandscape-001", workspace.getViews().getSystemLandscapeViews().iterator().next().getKey());

        assertEquals(3, workspace.getViews().getSystemContextViews().size());
        assertSame(ss1, workspace.getViews().getSystemContextViews().stream().filter(v -> v.getKey().equals("SystemContext-001")).findFirst().get().getSoftwareSystem());
        assertSame(ss2, workspace.getViews().getSystemContextViews().stream().filter(v -> v.getKey().equals("SystemContext-002")).findFirst().get().getSoftwareSystem());
        assertSame(ss3, workspace.getViews().getSystemContextViews().stream().filter(v -> v.getKey().equals("SystemContext-003")).findFirst().get().getSoftwareSystem());
    }

    @Test
    void removeViewWithKey_ThrowsAndException_WhenNoKeyIsSpecified() {
        try {
            new Workspace("Name").getViews().removeViewWithKey(null);
            fail();
        } catch (Exception e) {
            assertEquals("A view key must be specified.", e.getMessage());
        }

        try {
            new Workspace("Name").getViews().removeViewWithKey("");
            fail();
        } catch (Exception e) {
            assertEquals("A view key must be specified.", e.getMessage());
        }
    }

    @Test
    void removeViewWithKey_ThrowsAndException_WhenNoViewExists() {
        try {
            new Workspace("Name").getViews().removeViewWithKey("key");
            fail();
        } catch (Exception e) {
            assertEquals("A view with key \"key\" does not exist.", e.getMessage());
        }
    }

    @Test
    void removeViewWithKey_ThrowsAndException_WhenABaseViewExistsForTheSpecifiedFilteredView() {
        Workspace workspace = new Workspace("Name");

        try {
            SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("landscape");
            workspace.getViews().createFilteredView(view, "filtered", FilterMode.Include, Tags.ELEMENT);

            workspace.getViews().removeViewWithKey("landscape");
            fail();
        } catch (Exception e) {
            assertEquals("A filtered view based upon \"landscape\" exists - please remove this first.", e.getMessage());
        }
    }

    @Test
    void removeViewWithKey_CustomView() {
        Workspace workspace = new Workspace("Name");
        workspace.getViews().createCustomView("key", "title");

        assertFalse(workspace.getViews().getCustomViews().isEmpty());
        workspace.getViews().removeViewWithKey("key");
        assertTrue(workspace.getViews().getCustomViews().isEmpty());
    }

    @Test
    void removeViewWithKey_SystemLandscapeView() {
        Workspace workspace = new Workspace("Name");
        workspace.getViews().createSystemLandscapeView("key");

        assertFalse(workspace.getViews().getSystemLandscapeViews().isEmpty());
        workspace.getViews().removeViewWithKey("key");
        assertTrue(workspace.getViews().getSystemLandscapeViews().isEmpty());
    }

    @Test
    void removeViewWithKey_SystemContextView() {
        Workspace workspace = new Workspace("Name");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name");
        workspace.getViews().createSystemContextView(softwareSystem, "key");

        assertFalse(workspace.getViews().getSystemContextViews().isEmpty());
        workspace.getViews().removeViewWithKey("key");
        assertTrue(workspace.getViews().getSystemContextViews().isEmpty());
    }

    @Test
    void removeViewWithKey_ContainerView() {
        Workspace workspace = new Workspace("Name");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name");
        workspace.getViews().createContainerView(softwareSystem, "key");

        assertFalse(workspace.getViews().getContainerViews().isEmpty());
        workspace.getViews().removeViewWithKey("key");
        assertTrue(workspace.getViews().getContainerViews().isEmpty());
    }

    @Test
    void removeViewWithKey_ComponentView() {
        Workspace workspace = new Workspace("Name");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name");
        Container container = softwareSystem.addContainer("Name");
        workspace.getViews().createComponentView(container, "key");

        assertFalse(workspace.getViews().getComponentViews().isEmpty());
        workspace.getViews().removeViewWithKey("key");
        assertTrue(workspace.getViews().getComponentViews().isEmpty());
    }

    @Test
    void removeViewWithKey_DynamicView() {
        Workspace workspace = new Workspace("Name");
        workspace.getViews().createDynamicView("key");

        assertFalse(workspace.getViews().getDynamicViews().isEmpty());
        workspace.getViews().removeViewWithKey("key");
        assertTrue(workspace.getViews().getDynamicViews().isEmpty());
    }

    @Test
    void removeViewWithKey_DeploymentView() {
        Workspace workspace = new Workspace("Name");
        workspace.getViews().createDeploymentView("key");

        assertFalse(workspace.getViews().getDeploymentViews().isEmpty());
        workspace.getViews().removeViewWithKey("key");
        assertTrue(workspace.getViews().getDeploymentViews().isEmpty());
    }

    @Test
    void removeViewWithKey_FilteredView() {
        Workspace workspace = new Workspace("Name");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("landscape");
        workspace.getViews().createFilteredView(view, "key", FilterMode.Include, Tags.ELEMENT);

        assertFalse(workspace.getViews().getFilteredViews().isEmpty());
        workspace.getViews().removeViewWithKey("key");
        assertTrue(workspace.getViews().getFilteredViews().isEmpty());
    }

    @Test
    void removeViewWithKey_ImageView() {
        Workspace workspace = new Workspace("Name");
        workspace.getViews().createImageView("key");

        assertFalse(workspace.getViews().getImageViews().isEmpty());
        workspace.getViews().removeViewWithKey("key");
        assertTrue(workspace.getViews().getImageViews().isEmpty());
    }

}