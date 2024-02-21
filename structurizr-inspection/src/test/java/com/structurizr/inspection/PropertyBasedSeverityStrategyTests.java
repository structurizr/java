package com.structurizr.inspection;

import com.structurizr.Workspace;
import com.structurizr.inspection.model.ComponentDescriptionInspection;
import com.structurizr.inspection.model.RelationshipDescriptionInspection;
import com.structurizr.inspection.model.RelationshipTechnologyInspection;
import com.structurizr.inspection.workspace.WorkspaceScopeInspection;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Relationship;
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
    void getSeverityForWorkspace_WhenInspectionSpecifiedByName() {
        inspection = new WorkspaceScopeInspection(inspector);

        // specify by name at workspace level
        workspace.addProperty("structurizr.inspection." + inspection.getType(), "warning");
        assertEquals(Severity.WARNING, severityStrategy.getSeverity(inspection, workspace));
    }

    @Test
    void getSeverityForWorkspace_WhenInspectionSpecifiedByWildcard() {
        inspection = new WorkspaceScopeInspection(inspector);

        // specify by wildcard at workspace level
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
    void getSeverityForComponent_WhenInspectionSpecifiedByNameInComponent() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // specify by name at component level
        component.addProperty("structurizr.inspection." + inspection.getType(), "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void getSeverityForComponent_WhenInspectionSpecifiedByWildcardInComponent() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // specify by wildcard at component level
        workspace.addProperty("structurizr.inspection.model.component.*", "ignore");
        component.addProperty("structurizr.inspection.model.component.*", "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void getSeverityForComponent_WhenInspectionSpecifiedByNameInContainer() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // specify by name in parent container
        container.addProperty("structurizr.inspection." + inspection.getType(), "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void getSeverityForComponent_WhenInspectionSpecifiedByWildcardInContainer() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // specify by wildcard in parent container
        container.addProperty("structurizr.inspection.model.component.*", "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void getSeverityForComponent_WhenSpecifiedByNameInSoftwareSystem() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // specify by name in parent software system
        softwareSystem.addProperty("structurizr.inspection." + inspection.getType(), "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void getSeverityForComponent_WhenSpecifiedByWildcardInSoftwareSystem() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // specify by wildcard in parent software system
        softwareSystem.addProperty("structurizr.inspection.model.component.*", "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void getSeverityForComponent_WhenSpecifiedByNameInModel() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // specify by name in model
        workspace.getModel().addProperty("structurizr.inspection." + inspection.getType(), "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void getSeverityForComponent_WhenSpecifiedByWildcardInModel() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // specify by wildcard in model
        workspace.getModel().addProperty("structurizr.inspection.model.component.*", "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void getSeverityForComponent_WhenSpecifiedByNameInWorkspace() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // specify by name in workspace
        workspace.addProperty("structurizr.inspection." + inspection.getType(), "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void getSeverityForComponent_WhenSpecifiedByComponentWildcardInWorkspace() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // specify by wildcard in workspace
        workspace.addProperty("structurizr.inspection.model.component.*", "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void getSeverityForComponent_WhenSpecifiedByModelWildcardInWorkspace() {
        inspection = new ComponentDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component = container.addComponent("Component");

        // specify by model wildcard in workspace
        workspace.addProperty("structurizr.inspection.model.*", "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, component));
    }

    @Test
    void generateTypes_ForElement() {
        PropertyBasedSeverityStrategy strategy = new PropertyBasedSeverityStrategy();
        List<String> types = strategy.generatePropertyNames("model.component.description");

        assertEquals(4, types.size());
        assertEquals("structurizr.inspection.model.component.description", types.get(0));
        assertEquals("structurizr.inspection.model.component.*", types.get(1));
        assertEquals("structurizr.inspection.model.*", types.get(2));
        assertEquals("structurizr.inspection.*", types.get(3));
    }

    @Test
    void generateTypes_ForRelationship() {
        PropertyBasedSeverityStrategy strategy = new PropertyBasedSeverityStrategy();
        List<String> types = strategy.generatePropertyNames("model.relationship[component->component].technology");

        assertEquals(2, types.size());
        assertEquals("structurizr.inspection.model.relationship[component->component].technology", types.get(0));
        assertEquals("structurizr.inspection.model.relationship[component->component].*", types.get(1));
    }

    @Test
    void getSeverityForRelationship_BetweenComponents() {
        inspection = new RelationshipDescriptionInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component1 = container.addComponent("Component 1");
        Component component2 = container.addComponent("Component 2");
        Relationship relationship = component1.uses(component2, "");

        // default is error
        assertEquals(Severity.ERROR, severityStrategy.getSeverity(inspection, relationship));
    }

    @Test
    void getSeverityForRelationship_BetweenComponents_WhenSpecifiedByRelationshipTypeInWorkspace() {
        inspection = new RelationshipTechnologyInspection(inspector);
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        Component component1 = container.addComponent("Component 1");
        Component component2 = container.addComponent("Component 2");
        Relationship relationship = component1.uses(component2, "");

        // specify by relationship type in workspace
        workspace.addProperty("structurizr.inspection.model.relationship[component->component].technology", "info");
        assertEquals(Severity.INFO, severityStrategy.getSeverity(inspection, relationship));
    }

}