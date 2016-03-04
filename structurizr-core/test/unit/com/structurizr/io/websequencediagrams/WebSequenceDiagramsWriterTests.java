package com.structurizr.io.websequencediagrams;

import com.structurizr.Workspace;
import com.structurizr.model.InteractionStyle;
import com.structurizr.model.Model;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.DynamicView;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class WebSequenceDiagramsWriterTests {

    private WebSequenceDiagramsWriter webSequenceDiagramsWriter;
    private Workspace workspace;
    private StringWriter stringWriter;

    @Before
    public void setUp() {
        webSequenceDiagramsWriter = new WebSequenceDiagramsWriter();
        workspace = new Workspace("Name", "Description");
        stringWriter = new StringWriter();
    }

    @Test
    public void test_write_DoesNotThrowAnExceptionWhenPassedNullParameters() throws Exception {
        webSequenceDiagramsWriter.write(null, null);
        webSequenceDiagramsWriter.write(workspace, null);
        webSequenceDiagramsWriter.write(null, stringWriter);
    }

    @Test
    public void test_write_createsAWebSequenceDiagram() throws Exception {
        Model model = workspace.getModel();
        SoftwareSystem a = model.addSoftwareSystem("System A", "");
        SoftwareSystem b = model.addSoftwareSystem("System B", "");
        SoftwareSystem c = model.addSoftwareSystem("System C", "");

        a.uses(b, "");
        Relationship bc = b.uses(c, "");
        bc.setInteractionStyle(InteractionStyle.Asynchronous);

        DynamicView view = workspace.getViews().createDynamicView(a, "A description of the diagram");
        view.add(a, "Does something using", b);
        view.add(b, "Does something then using", c);

        webSequenceDiagramsWriter.write(workspace, stringWriter);
        assertEquals("title System A - Dynamic - A description of the diagram" + System.lineSeparator() +
                System.lineSeparator() +
                "System A->System B: Does something using" + System.lineSeparator() +
                "System B->>System C: Does something then using" + System.lineSeparator() +
                System.lineSeparator(), stringWriter.toString());
    }

}
