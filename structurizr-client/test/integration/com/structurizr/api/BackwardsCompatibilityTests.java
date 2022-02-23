package com.structurizr.api;

import com.structurizr.util.WorkspaceUtils;
import org.junit.Test;

import java.io.File;

public class BackwardsCompatibilityTests {

    private static final File PATH_TO_WORKSPACE_FILES = new File("test/integration/backwardsCompatibility");

    @Test
    public void test() throws Exception {
        for (File file : PATH_TO_WORKSPACE_FILES.listFiles(f -> f.getName().endsWith(".json"))) {
            WorkspaceUtils.loadWorkspaceFromJson(file);
        }
    }

}