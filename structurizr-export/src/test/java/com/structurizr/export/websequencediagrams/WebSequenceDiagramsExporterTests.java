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
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/big-bank-plc.json"));
        WebSequenceDiagramsExporter exporter = new WebSequenceDiagramsExporter();

        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(1, diagrams.size());

        Diagram diagram = diagrams.stream().filter(d -> d.getKey().equals("SignIn")).findFirst().get();
        assertEquals("""
                title API Application - Dynamic - SignIn
                
                participant <<Container>>\\nSingle-Page Application as Single-Page Application
                participant <<Component>>\\nSign In Controller as Sign In Controller
                participant <<Component>>\\nSecurity Component as Security Component
                participant <<Container>>\\nDatabase as Database
                
                Single-Page Application->Sign In Controller: Submits credentials to
                Sign In Controller->Security Component: Validates credentials using
                Security Component->Database: select * from users where username = ?
                Database-->Security Component: Returns user data to
                Security Component-->Sign In Controller: Returns true if the hashed password matches
                Sign In Controller-->Single-Page Application: Sends back an authentication token to
                """, diagram.getDefinition());
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
        assertEquals("""
                title Dynamic - key
                
                participant <<Software System>>\\nA as A
                participant <<Software System>>\\nB as B
                
                A->B: Uses
                """, diagram.getDefinition());
    }

}