package com.structurizr.inspection;

import com.structurizr.Workspace;
import com.structurizr.inspection.model.ComponentDescriptionInspection;
import com.structurizr.inspection.workspace.WorkspaceScopeInspection;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PropertyBasedSeverityStrategyTests {

    private Workspace workspace;
    private Inspector inspector;
    private Inspection inspection;
    private PropertyBasedSeverityStrategy severityStrategy;

    @BeforeEach
    void setUp() {
        workspace = new Workspace("Name", "Description");
        inspector = new Inspector(workspace) {
            @Override
            public SeverityStrategy getSeverityStrategy() {
                return null;
            }
        };
        severityStrategy = new PropertyBasedSeverityStrategy();
    }

    @Test
    void getSeverityForWorkspace() {
        inspection = new WorkspaceScopeInspection(inspector);

        // default is error
        assertEquals(Severity.ERROR, severityStrategy.getSeverity(inspection, workspace));
    }

    @Test
    void getSeverityForWorkspace_WhenOverriddenByName() {
        inspection = new WorkspaceScopeInspection(inspector);

        // override by name
        workspace.addProperty("structurizr.inspection." + inspection.getType(), "warning");
        assertEquals(Severity.WARNING, severityStrategy.getSeverity(inspection, workspace));
    }

    @Test
    void getSeverityForWorkspace_WhenOverriddenByWildcard() {
        inspection = new WorkspaceScopeInspection(inspector);

        // override by wildcard
        workspace.addProperty("structurizr.inspection.*", "warning");
        assertEquals(Severity.WARNING, severityStrategy.getSeverity(inspection, workspace));
    }

    @Test
    void getSeverityForComponent() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // default is error
        assertEquals(Severity.ERROR, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void getSeverityForComponent_WhenOverriddenByNameInComponent() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // override by name
        component.addProperty("structurizr.inspection." + inspection.getType(), "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void getSeverityForComponent_WhenOverriddenByWildcardInComponent() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // override by wildcard
        component.addProperty("structurizr.inspection.model.component.*", "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void getSeverityForComponent_WhenOverriddenByNameInContainer() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // override by name
        container.addProperty("structurizr.inspection." + inspection.getType(), "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void getSeverityForComponent_WhenOverriddenByWildcardInContainer() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // override by wildcard
        container.addProperty("structurizr.inspection.model.component.*", "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void getSeverityForComponent_WhenOverriddenByNameInSoftwareSystem() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // override by name
        softwareSystem.addProperty("structurizr.inspection." + inspection.getType(), "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void getSeverityForComponent_WhenOverriddenByWildcardInSoftwareSystem() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // override by wildcard
        softwareSystem.addProperty("structurizr.inspection.model.component.*", "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void getSeverityForComponent_WhenOverriddenByNameInModel() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // override by name
        workspace.getModel().addProperty("structurizr.inspection." + inspection.getType(), "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void getSeverityForComponent_WhenOverriddenByWildcardInModel() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // override by wildcard
        workspace.getModel().addProperty("structurizr.inspection.model.component.*", "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void getSeverityForComponent_WhenOverriddenByNameInWorkspace() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // override by name
        workspace.addProperty("structurizr.inspection." + inspection.getType(), "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void getSeverityForComponent_WhenOverriddenByComponentWildcardInWorkspace() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // override by wildcard
        workspace.addProperty("structurizr.inspection.model.component.*", "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void getSeverityForComponent_WhenOverriddenByModelWildcardInWorkspace() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // override by wildcard
        workspace.addProperty("structurizr.inspection.model.*", "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void generateTypes() {
        PropertyBasedSeverityStrategy strategy = new PropertyBasedSeverityStrategy();
        List<String> types = strategy.generatePropertyNames("model.component.description");

        assertEquals("structurizr.inspection.model.component.description", types.get(0));
        assertEquals("structurizr.inspection.model.component.*", types.get(1));
        assertEquals("structurizr.inspection.model.*", types.get(2));
        assertEquals("structurizr.inspection.*", types.get(3));
    }

}