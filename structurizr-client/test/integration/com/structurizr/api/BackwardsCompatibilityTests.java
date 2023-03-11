package com.structurizr.api;

import com.structurizr.Workspace;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.Section;
import com.structurizr.model.Location;
import com.structurizr.util.WorkspaceUtils;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BackwardsCompatibilityTests {

    private static final File PATH_TO_WORKSPACE_FILES = new File("test/integration/backwardsCompatibility");

    @Test
    void test() throws Exception {
        for (File file : PATH_TO_WORKSPACE_FILES.listFiles(f -> f.getName().endsWith(".json"))) {
            WorkspaceUtils.loadWorkspaceFromJson(file);
        }
    }

    @Test
    void enterprise_and_location() throws Exception {
        File file = new File(PATH_TO_WORKSPACE_FILES, "structurizr-36141-workspace.json");
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(file);

        assertEquals("Big Bank plc", workspace.getModel().getEnterprise().getName());
        assertEquals(Location.Internal, workspace.getModel().getPersonWithName("Back Office Staff").getLocation());
        assertEquals(Location.External, workspace.getModel().getPersonWithName("Personal Banking Customer").getLocation());

        // make sure enterprise and location information is not lost when going to/from JSON
        workspace = WorkspaceUtils.fromJson(WorkspaceUtils.toJson(workspace, false));

        assertEquals("Big Bank plc", workspace.getModel().getEnterprise().getName());
        assertEquals(Location.Internal, workspace.getModel().getPersonWithName("Back Office Staff").getLocation());
        assertEquals(Location.External, workspace.getModel().getPersonWithName("Personal Banking Customer").getLocation());
    }

    @Test
    void documentation() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getDocumentation().addSection(new Section(Format.Markdown, "## Heading 1"));

        assertEquals("{\"id\":0,\"name\":\"Name\",\"description\":\"Description\",\"configuration\":{},\"model\":{},\"documentation\":{\"sections\":[{\"content\":\"## Heading 1\",\"format\":\"Markdown\",\"order\":1,\"title\":\"\"}]},\"views\":{\"configuration\":{\"branding\":{},\"styles\":{},\"terminology\":{}}}}", WorkspaceUtils.toJson(workspace, false));
    }

}