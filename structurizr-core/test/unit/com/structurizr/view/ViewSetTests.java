package com.structurizr.view;

import com.structurizr.Workspace;
import com.structurizr.model.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ViewSetTests {

    private Workspace createWorkspace() {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();

        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Person person = model.addPerson("Person", "Description");
        person.uses(softwareSystem, "Uses");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        Component component = container.addComponent("Component", "Description", "Technology");

        return workspace;
    }

    @Test
    public void test_copyLayoutInformationFrom_WhenAViewKeyIsNotSetButTheViewTitlesMatch() {
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
    public void test_copyLayoutInformationFrom_WhenAViewKeyHasBeenIntroduced() {
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
    public void test_copyLayoutInformationFrom_WhenTheSystemContextViewKeysMatch() {
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
    public void test_copyLayoutInformationFrom_DoesNotDoAnythingIfThereIsNoSystemContextViewToCopyInformationFrom() {
        Workspace workspace1 = createWorkspace();

        Workspace workspace2 = createWorkspace();
        SoftwareSystem softwareSystem2 = workspace2.getModel().getSoftwareSystemWithName("Software System");
        SystemContextView view2 = workspace2.getViews().createSystemContextView(softwareSystem2, "context", "Description");
        view2.addAllElements();

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(0, view2.getElements().iterator().next().getX()); // default
        assertEquals(PaperSize.A4_Portrait, view2.getPaperSize()); // default
    }

    @Test
    public void test_copyLayoutInformationFrom_WhenTheContainerViewKeysMatch() {
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
    public void test_copyLayoutInformationFrom_DoesNotDoAnythingIfThereIsNoContainerViewToCopyInformationFrom() {
        Workspace workspace1 = createWorkspace();

        Workspace workspace2 = createWorkspace();
        SoftwareSystem softwareSystem2 = workspace2.getModel().getSoftwareSystemWithName("Software System");
        ContainerView view2 = workspace2.getViews().createContainerView(softwareSystem2, "containers", "Description");
        view2.addAllElements();

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(0, view2.getElements().iterator().next().getX()); // default
        assertEquals(PaperSize.A4_Portrait, view2.getPaperSize()); // default
    }

    @Test
    public void test_copyLayoutInformationFrom_WhenTheComponentViewKeysMatch() {
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
    public void test_copyLayoutInformationFrom_DoesNotDoAnythingIfThereIsNoComponentViewToCopyInformationFrom() {
        Workspace workspace1 = createWorkspace();

        Workspace workspace2 = createWorkspace();
        Container container2 = workspace2.getModel().getSoftwareSystemWithName("Software System").getContainerWithName("Container");
        ComponentView view2 = workspace2.getViews().createComponentView(container2, "components", "Description");
        view2.addAllElements();

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(0, view2.getElements().iterator().next().getX()); // default
        assertEquals(PaperSize.A4_Portrait, view2.getPaperSize()); // default
    }

    @Test
    public void test_copyLayoutInformationFrom_WhenTheDynamicViewKeysMatch() {
        Workspace workspace1 = createWorkspace();
        Person person1 = workspace1.getModel().getPersonWithName("Person");
        SoftwareSystem softwareSystem1 = workspace1.getModel().getSoftwareSystemWithName("Software System");
        DynamicView view1 = workspace1.getViews().createDynamicView(softwareSystem1, "context", "Description");
        view1.add(person1, softwareSystem1);
        view1.getElements().iterator().next().setX(100);
        view1.setPaperSize(PaperSize.A3_Landscape);

        Workspace workspace2 = createWorkspace();
        Person person2 = workspace2.getModel().getPersonWithName("Person");
        SoftwareSystem softwareSystem2 = workspace2.getModel().getSoftwareSystemWithName("Software System");
        DynamicView view2 = workspace2.getViews().createDynamicView(softwareSystem2, "context", "Description");
        view2.add(person2, softwareSystem2);

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(100, view2.getElements().iterator().next().getX());
        assertEquals(PaperSize.A3_Landscape, view2.getPaperSize());
    }

    @Test
    public void test_copyLayoutInformationFrom_DoesNotDoAnythingIfThereIsNoDynamicViewToCopyInformationFrom() {
        Workspace workspace1 = createWorkspace();

        Workspace workspace2 = createWorkspace();
        Person person2 = workspace2.getModel().getPersonWithName("Person");
        SoftwareSystem softwareSystem2 = workspace2.getModel().getSoftwareSystemWithName("Software System");
        DynamicView view2 = workspace2.getViews().createDynamicView(softwareSystem2, "context", "Description");
        view2.add(person2, softwareSystem2);

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(0, view2.getElements().iterator().next().getX()); // default
        assertEquals(PaperSize.A4_Portrait, view2.getPaperSize()); // default
    }

    @Test
    public void test_createSystemContextView_DoesNotThrowAnException_WhenEveryViewHasADifferentKey() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");

        workspace.getViews().createSystemContextView(softwareSystem, "context1", "Description");
        workspace.getViews().createSystemContextView(softwareSystem, "context2", "Description");
    }

    @Test
    public void test_createSystemContextView_ThrowsAnException_WhenADuplicateKeyIsUsed() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");

        workspace.getViews().createSystemContextView(softwareSystem, "context", "Description");
        try {
            workspace.getViews().createSystemContextView(softwareSystem, "context", "Description");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A view with the key context already exists.", iae.getMessage());
        }
    }

    @Test
    public void test_createContainerView_DoesNotThrowAnException_WhenEveryViewHasADifferentKey() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");
        Container container = softwareSystem.addContainer("Name", "Description", "Technology");

        workspace.getViews().createContainerView(softwareSystem, "containers1", "Description");
        workspace.getViews().createContainerView(softwareSystem, "containers2", "Description");
    }

    @Test
    public void test_createContainerView_ThrowsAnException_WhenADuplicateKeyIsUsed() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");
        Container container = softwareSystem.addContainer("Name", "Description", "Technology");

        workspace.getViews().createContainerView(softwareSystem, "containers", "Description");
        try {
            workspace.getViews().createContainerView(softwareSystem, "containers", "Description");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A view with the key containers already exists.", iae.getMessage());
        }
    }

    @Test
    public void test_createComponentView_DoesNotThrowAnException_WhenEveryViewHasADifferentKey() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");
        Container container = softwareSystem.addContainer("Name", "Description", "Technology");
        Component component = container.addComponent("Name", "Description", "Technology");

        workspace.getViews().createComponentView(container, "components1", "Description");
        workspace.getViews().createComponentView(container, "components2", "Description");
    }

    @Test
    public void test_createComponentView_ThrowsAnException_WhenADuplicateKeyIsUsed() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");
        Container container = softwareSystem.addContainer("Name", "Description", "Technology");
        Component component = container.addComponent("Name", "Description", "Technology");

        workspace.getViews().createComponentView(container, "components", "Description");
        try {
            workspace.getViews().createComponentView(container, "components", "Description");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A view with the key components already exists.", iae.getMessage());
        }
    }

    @Test
    public void test_createDynamicView_DoesNotThrowAnException_WhenEveryViewHasADifferentKey() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");

        workspace.getViews().createDynamicView(softwareSystem, "dynamic1", "Description");
        workspace.getViews().createDynamicView(softwareSystem, "dynamic2", "Description");
    }

    @Test
    public void test_createDynamicView_ThrowsAnException_WhenADuplicateKeyIsUsed() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");

        workspace.getViews().createDynamicView(softwareSystem, "dynamic", "Description");
        try {
            workspace.getViews().createDynamicView(softwareSystem, "dynamic", "Description");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A view with the key dynamic already exists.", iae.getMessage());
        }
    }

}
