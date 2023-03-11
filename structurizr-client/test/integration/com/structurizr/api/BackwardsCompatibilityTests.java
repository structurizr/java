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
    void documentation() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getDocumentation().addSection(new Section(Format.Markdown, "## Heading 1"));

        assertEquals("{\"id\":0,\"name\":\"Name\",\"description\":\"Description\",\"configuration\":{},\"model\":{},\"documentation\":{\"sections\":[{\"content\":\"## Heading 1\",\"format\":\"Markdown\",\"order\":1,\"title\":\"\"}]},\"views\":{\"configuration\":{\"branding\":{},\"styles\":{},\"terminology\":{}}}}", WorkspaceUtils.toJson(workspace, false));
    }

}