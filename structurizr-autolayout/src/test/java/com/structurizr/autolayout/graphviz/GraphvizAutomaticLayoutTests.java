package com.structurizr.autolayout.graphviz;

import com.structurizr.Workspace;
import com.structurizr.autolayout.graphviz.GraphvizAutomaticLayout;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
import com.structurizr.view.AutomaticLayout;
import com.structurizr.view.Shape;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.SystemLandscapeView;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphvizAutomaticLayoutTests {

    @Test
    public void apply_Workspace() throws Exception {
        File tempDir = Files.createTempDirectory("graphviz").toFile();
        GraphvizAutomaticLayout graphviz = new GraphvizAutomaticLayout(tempDir);

        Workspace workspace = new Workspace("Name");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B");
        a.uses(b, "Uses");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key");
        view.addAllElements();

        workspace.getViews().getConfiguration().getStyles().addElementStyle(Tags.PERSON).shape(Shape.Person);

        assertEquals(0, view.getElementView(a).getX());
        assertEquals(0, view.getElementView(a).getY());
        assertEquals(0, view.getElementView(b).getX());
        assertEquals(0, view.getElementView(b).getY());

        graphviz.apply(workspace);

        // no change - the view doesn't have automatic layout configured
        assertEquals(0, view.getElementView(a).getX());
        assertEquals(0, view.getElementView(a).getY());
        assertEquals(0, view.getElementView(b).getX());
        assertEquals(0, view.getElementView(b).getY());

        view.enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom);
        graphviz.apply(workspace);

        assertEquals(208, view.getElementView(a).getX());
        assertEquals(208, view.getElementView(a).getY());
        assertEquals(208, view.getElementView(b).getX());
        assertEquals(808, view.getElementView(b).getY());
    }

}