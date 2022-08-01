package com.structurizr.api;

import com.structurizr.util.WorkspaceUtils;
import org.junit.jupiter.api.Test;

import java.io.File;

class BackwardsCompatibilityTests {

    private static final File PATH_TO_WORKSPACE_FILES = new File("test/integration/backwardsCompatibility");

    @Test
    void test() throws Exception {
        for (File file : PATH_TO_WORKSPACE_FILES.listFiles(f -> f.getName().endsWith(".json"))) {
            WorkspaceUtils.loadWorkspaceFromJson(file);
        }
    }

}