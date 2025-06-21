package com.structurizr.export.ilograph;

import com.structurizr.Workspace;
import com.structurizr.export.AbstractExporterTests;
import com.structurizr.export.WorkspaceExport;
import com.structurizr.model.CustomElement;
import com.structurizr.model.Model;
import com.structurizr.util.WorkspaceUtils;
import com.structurizr.view.ThemeUtils;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IlographExporterTests extends AbstractExporterTests {

    @Test
    public void test_BigBankPlcExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/structurizr-36141-sequence.json"));
        IlographExporter ilographExporter = new IlographExporter();
        WorkspaceExport export = ilographExporter.export(workspace);

        String expected = readFile(new File("./src/test/java/com/structurizr/export/ilograph/36141.ilograph"));
        assertEquals(expected, export.getDefinition());
    }

    @Test
    void test_AmazonWebServicesExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/structurizr-54915-sequence.json"));
        workspace.getViews().getConfiguration().getStyles().addElementStyle("Amazon Web Services - Route 53").addProperty(IlographExporter.ILOGRAPH_ICON, "AWS/Networking/Route-53.svg");

        ThemeUtils.loadThemes(workspace);
        IlographExporter ilographExporter = new IlographExporter();
        WorkspaceExport export = ilographExporter.export(workspace);

        String expected = readFile(new File("./src/test/java/com/structurizr/export/ilograph/54915.ilograph"));
        assertEquals(expected, export.getDefinition());
    }

    @Test
    void test_renderCustomElements() {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();

        CustomElement a = model.addCustomElement("A");
        CustomElement b = model.addCustomElement("B", "Custom", "Description");
        a.uses(b, "Uses");

        WorkspaceExport export = new IlographExporter().export(workspace);
        assertEquals("resources:\n" +
                "  - id: \"1\"\n" +
                "    name: \"A\"\n" +
                "    subtitle: \"\"\n" +
                "    backgroundColor: \"#dddddd\"\n" +
                "    color: \"#000000\"\n" +
                "\n" +
                "  - id: \"2\"\n" +
                "    name: \"B\"\n" +
                "    subtitle: \"[Custom]\"\n" +
                "    description: \"Description\"\n" +
                "    backgroundColor: \"#dddddd\"\n" +
                "    color: \"#000000\"\n" +
                "\n" +
                "perspectives:\n" +
                "  - name: Static Structure\n" +
                "    relations:\n" +
                "      - from: \"1\"\n" +
                "        to: \"2\"\n" +
                "        label: \"Uses\"\n" +
                "        color: \"#707070\"\n", export.getDefinition());
    }

    @Test
    void test_imports() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.addProperty(IlographExporter.ILOGRAPH_IMPORTS, "NAMESPACE1:path1,NAMESPACE2:path2");

        WorkspaceExport export = new IlographExporter().export(workspace);
        assertEquals("""
imports:
- from: path1
  namespace: NAMESPACE1
- from: path2
  namespace: NAMESPACE2

resources:
perspectives:
  - name: Static Structure
    relations:""", export.getDefinition());
    }

}