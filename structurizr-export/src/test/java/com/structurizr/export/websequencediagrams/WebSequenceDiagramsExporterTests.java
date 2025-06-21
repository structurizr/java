package com.structurizr.export.websequencediagrams;

import com.structurizr.Workspace;
import com.structurizr.export.AbstractExporterTests;
import com.structurizr.export.Diagram;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.util.WorkspaceUtils;
import com.structurizr.view.DynamicView;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class WebSequenceDiagramsExporterTests extends AbstractExporterTests {

    @Test
    public void test_BigBankPlcExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/structurizr-36141-sequence.json"));
        WebSequenceDiagramsExporter exporter = new WebSequenceDiagramsExporter();

        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(1, diagrams.size());

        Diagram diagram = diagrams.stream().filter(d -> d.getKey().equals("SignIn")).findFirst().get();
        String expected = readFile(new File("./src/test/java/com/structurizr/export/websequencediagrams/36141-SignIn.wsd"));
        assertEquals(expected, diagram.getDefinition());
    }

    @Test
    public void test_dynamicViewThatDoeNotOverrideRelationshipDescriptions() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B");
        a.uses(b, "Uses");

        DynamicView view = workspace.getViews().createDynamicView("key", "Description");
        view.add(a, b);

        WebSequenceDiagramsExporter exporter = new WebSequenceDiagramsExporter();

        Collection<Diagram> diagrams = exporter.export(workspace);
        Diagram diagram = diagrams.iterator().next();
        assertEquals("title Dynamic - key\n" +
                "\n" +
                "participant <<Software System>>\\nA as A\n" +
                "participant <<Software System>>\\nB as B\n" +
                "\n" +
                "A->B: Uses", diagram.getDefinition());
    }

}