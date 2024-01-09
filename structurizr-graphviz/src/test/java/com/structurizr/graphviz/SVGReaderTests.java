package com.structurizr.graphviz;

import com.structurizr.Workspace;
import com.structurizr.model.Location;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
import com.structurizr.view.PaperSize;
import com.structurizr.view.Shape;
import com.structurizr.view.SystemContextView;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SVGReaderTests {

    private static final File PATH = new File("./src/test/resources/graphviz");

    @Test
    public void test_readView() throws Exception {
        Workspace workspace = createWorkspace();
        SystemContextView view = workspace.getViews().getSystemContextViews().iterator().next();
        Person user = workspace.getModel().getPersonWithName("User");
        SoftwareSystem softwareSystem = workspace.getModel().getSoftwareSystemWithName("Software System");

        assertEquals(0, view.getElementView(user).getX());
        assertEquals(0, view.getElementView(user).getY());

        assertEquals(0, view.getElementView(softwareSystem).getX());
        assertEquals(0, view.getElementView(softwareSystem).getY());

        assertNull(view.getPaperSize());

        SVGReader svgReader = new SVGReader(PATH, 200, true);
        svgReader.parseAndApplyLayout(view);

        assertEquals(PaperSize.A6_Portrait, view.getPaperSize());

        assertEquals(254, view.getElementView(user).getX());
        assertEquals(108, view.getElementView(user).getY());

        assertEquals(229, view.getElementView(softwareSystem).getX());
        assertEquals(808, view.getElementView(softwareSystem).getY());
    }

    private static Workspace createWorkspace() {
        Workspace workspace = new Workspace("Name", "");
        Person user = workspace.getModel().addPerson("User", "");
        user.setLocation(Location.External);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "");
        softwareSystem.setLocation(Location.Internal);
        user.uses(softwareSystem, "Uses");

        SystemContextView view = workspace.getViews().createSystemContextView(softwareSystem, "SystemContext", "");
        view.addAllElements();
        view.setEnterpriseBoundaryVisible(true);

        workspace.getViews().getConfiguration().getStyles().addElementStyle(Tags.PERSON).shape(Shape.Person);

        return workspace;
    }

}