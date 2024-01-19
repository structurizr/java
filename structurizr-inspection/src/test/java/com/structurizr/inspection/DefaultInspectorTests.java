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
        assertEquals("workspace.scope", violation.getType());
        assertEquals("This workspace has no defined scope. It is recommended that the workspace scope is set to \"Landscape\" or \"SoftwareSystem\".", violation.getMessage());

        violation = violations.get(1);
        assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("model.empty", violation.getType());
        assertEquals("Add some elements to the model.", violation.getMessage());


        violation = violations.get(2);
        assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("views.empty", violation.getType());
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
        assertEquals(0, violations.stream().filter(v -> v.getSeverity() == Severity.ERROR).count());
        assertEquals(27, violations.stream().filter(v -> v.getSeverity() == Severity.WARNING).count());
        assertEquals(1, violations.stream().filter(v -> v.getSeverity() == Severity.INFO).count());
    }

    @Test
    void test_WithAllViolationsAsError() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/workspace.dsl"));
        Workspace workspace = parser.getWorkspace();

        DefaultInspector inspector = new DefaultInspector(workspace) {
            @Override
            public SeverityStrategy getSeverityStrategy() {
                return new FixedSeverityStrategy(Severity.ERROR);
            }
        };
        Collection<Violation> violations = inspector.getViolations();

        for (Violation violation : violations) {
            System.out.println(violation);
        }

        assertEquals(28, violations.size());
        assertEquals(28, violations.stream().filter(v -> v.getSeverity() == Severity.ERROR).count());
    }

}
