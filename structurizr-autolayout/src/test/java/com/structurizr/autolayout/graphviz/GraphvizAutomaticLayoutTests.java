package com.structurizr.autolayout.graphviz;

import com.structurizr.Workspace;
import com.structurizr.autolayout.graphviz.GraphvizAutomaticLayout;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
import com.structurizr.view.AutomaticLayout;
import com.structurizr.view.Shape;
import com.structurizr.view.SystemContextView;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphvizAutomaticLayoutTests {

    @Test
    public void apply_Workspace() throws Exception {
        File tempDir = Files.createTempDirectory("graphviz").toFile();
        GraphvizAutomaticLayout graphviz = new GraphvizAutomaticLayout(tempDir);

        Workspace workspace = new Workspace("Name", "");
        Person user = workspace.getModel().addPerson("User");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        user.uses(softwareSystem, "Uses");

        SystemContextView view = workspace.getViews().createSystemContextView(softwareSystem, "SystemContext", "");
        view.addAllElements();

        workspace.getViews().getConfiguration().getStyles().addElementStyle(Tags.PERSON).shape(Shape.Person);

        assertEquals(0, view.getElementView(user).getX());
        assertEquals(0, view.getElementView(user).getY());
        assertEquals(0, view.getElementView(softwareSystem).getX());
        assertEquals(0, view.getElementView(softwareSystem).getY());

        graphviz.apply(workspace);

        // no change - the view doesn't have automatic layout configured
        assertEquals(0, view.getElementView(user).getX());
        assertEquals(0, view.getElementView(user).getY());
        assertEquals(0, view.getElementView(softwareSystem).getX());
        assertEquals(0, view.getElementView(softwareSystem).getY());

        view.enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom);
        graphviz.apply(workspace);

        assertEquals(233, view.getElementView(user).getX());
        assertEquals(208, view.getElementView(user).getY());
        assertEquals(208, view.getElementView(softwareSystem).getX());
        assertEquals(908, view.getElementView(softwareSystem).getY());
    }

}