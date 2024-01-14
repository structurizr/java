package com.structurizr.inspection;

import com.structurizr.Workspace;
import com.structurizr.dsl.StructurizrDslParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultInspectorTests {

    @Test
    void test_EmptyWorkspace() throws Exception {
        DefaultInspector inspector = new DefaultInspector(new Workspace("Name", "Description"));
        List<Violation> violations = inspector.getViolations();

        assertEquals(3, violations.size());

        Violation violation = violations.get(0);
        assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("structurizr.inspection.workspace.scope", violation.getType());
        assertEquals("This workspace has no defined scope. It is recommended that the workspace scope is set to \"Landscape\" or \"SoftwareSystem\".", violation.getMessage());

        violation = violations.get(1);
        assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("structurizr.inspection.model.empty", violation.getType());
        assertEquals("Add some elements to the model.", violation.getMessage());


        violation = violations.get(2);
        assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("structurizr.inspection.views.empty", violation.getType());
        assertEquals("Add some views to the workspace.", violation.getMessage());
    }

    @Test
    void test() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/workspace.dsl"));
        Workspace workspace = parser.getWorkspace();

        DefaultInspector inspector = new DefaultInspector(workspace);
        Collection<Violation> violations = inspector.getViolations();

        for (Violation violation : violations) {
            System.out.println(violation);
        }

        assertEquals(28, violations.size());
    }

}
