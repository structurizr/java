package com.structurizr.documentation;

import com.structurizr.Workspace;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Element;
import com.structurizr.model.SoftwareSystem;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StructurizrDocumentationTests {

    private SoftwareSystem softwareSystem;
    private Container containerA;
    private Container containerB;
    private Component componentA1;
    private Component componentA2;
    private StructurizrDocumentation documentation;

    @Before
    public void setUp() {
        Workspace workspace = new Workspace("Name", "Description");
        softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");
        containerA = softwareSystem.addContainer("Container A", "Description", "Technology");
        containerB = softwareSystem.addContainer("Container B", "Description", "Technology");
        componentA1 = containerA.addComponent("Component A1", "Description", "Technology");
        componentA2 = containerA.addComponent("Component A2", "Description", "Technology");

        documentation = new StructurizrDocumentation(workspace.getModel());
    }

    @Test
    public void test_construction() {
        assertTrue(documentation.getSections().isEmpty());
        assertTrue(documentation.getImages().isEmpty());
    }

    @Test
    public void test_addAllSectionsWithContentAsStrings() {
        Section section;

        section = documentation.addContextSection(softwareSystem, Format.Markdown, "Context section");
        assertSection(softwareSystem, "Context", 1, Format.Markdown, "Context section", 1, section, documentation);

        section = documentation.addFunctionalOverviewSection(softwareSystem, Format.Markdown, "Functional overview section");
        assertSection(softwareSystem, "Functional Overview", 2, Format.Markdown, "Functional overview section", 2, section, documentation);

        section = documentation.addQualityAttributesSection(softwareSystem, Format.Markdown, "Quality attributes section");
        assertSection(softwareSystem, "Quality Attributes", 2, Format.Markdown, "Quality attributes section", 3, section, documentation);

        section = documentation.addConstraintsSection(softwareSystem, Format.Markdown, "Constraints section");
        assertSection(softwareSystem, "Constraints", 2, Format.Markdown, "Constraints section", 4, section, documentation);

        section = documentation.addPrinciplesSection(softwareSystem, Format.Markdown, "Principles section");
        assertSection(softwareSystem, "Principles", 2, Format.Markdown, "Principles section", 5, section, documentation);

        section = documentation.addSoftwareArchitectureSection(softwareSystem, Format.Markdown, "Software architecture section");
        assertSection(softwareSystem, "Software Architecture", 3, Format.Markdown, "Software architecture section", 6, section, documentation);

        section = documentation.addContainersSection(softwareSystem, Format.Markdown, "Containers section");
        assertSection(softwareSystem, "Containers", 3, Format.Markdown, "Containers section", 7, section, documentation);

        section = documentation.addComponentsSection(containerA, Format.Markdown, "Components section for container A");
        assertSection(containerA, "Components", 3, Format.Markdown, "Components section for container A", 8, section, documentation);

        section = documentation.addComponentsSection(containerB, Format.Markdown, "Components section for container B");
        assertSection(containerB, "Components", 3, Format.Markdown, "Components section for container B", 9, section, documentation);

        section = documentation.addCodeSection(componentA1, Format.Markdown, "Code section for component A1");
        assertSection(componentA1, "Code", 3, Format.Markdown, "Code section for component A1", 10, section, documentation);

        section = documentation.addCodeSection(componentA2, Format.Markdown, "Code section for component A2");
        assertSection(componentA2, "Code", 3, Format.Markdown, "Code section for component A2", 11, section, documentation);

        section = documentation.addDataSection(softwareSystem, Format.Markdown, "Data section");
        assertSection(softwareSystem, "Data", 3, Format.Markdown, "Data section", 12, section, documentation);

        section = documentation.addInfrastructureArchitectureSection(softwareSystem, Format.Markdown, "Infrastructure architecture section");
        assertSection(softwareSystem, "Infrastructure Architecture", 4, Format.Markdown, "Infrastructure architecture section", 13, section, documentation);

        section = documentation.addDeploymentSection(softwareSystem, Format.Markdown, "Deployment section");
        assertSection(softwareSystem, "Deployment", 4, Format.Markdown, "Deployment section", 14, section, documentation);

        section = documentation.addDevelopmentEnvironmentSection(softwareSystem, Format.Markdown, "Development environment section");
        assertSection(softwareSystem, "Development Environment", 4, Format.Markdown, "Development environment section", 15, section, documentation);

        section = documentation.addOperationAndSupportSection(softwareSystem, Format.Markdown, "Operation and support section");
        assertSection(softwareSystem, "Operation and Support", 4, Format.Markdown, "Operation and support section", 16, section, documentation);

        section = documentation.addDecisionLog(softwareSystem, Format.Markdown, "Decision log section");
        assertSection(softwareSystem, "Decision Log", 5, Format.Markdown, "Decision log section", 17, section, documentation);
    }

    @Test
    public void test_addAllSectionsWithContentFromFiles() throws IOException {
        Section section;
        File root = new File(".//test/unit/com/structurizr/documentation/structurizr");

        section = documentation.addContextSection(softwareSystem, Format.Markdown, new File(root, "context.md"));
        assertSection(softwareSystem, "Context", 1, Format.Markdown, "Context section", 1, section, documentation);

        section = documentation.addFunctionalOverviewSection(softwareSystem, Format.Markdown, new File(root, "functional-overview.md"));
        assertSection(softwareSystem, "Functional Overview", 2, Format.Markdown, "Functional overview section", 2, section, documentation);

        section = documentation.addQualityAttributesSection(softwareSystem, Format.Markdown, new File(root, "quality-attributes.md"));
        assertSection(softwareSystem, "Quality Attributes", 2, Format.Markdown, "Quality attributes section", 3, section, documentation);

        section = documentation.addConstraintsSection(softwareSystem, Format.Markdown, new File(root, "constraints.md"));
        assertSection(softwareSystem, "Constraints", 2, Format.Markdown, "Constraints section", 4, section, documentation);

        section = documentation.addPrinciplesSection(softwareSystem, Format.Markdown, new File(root, "principles.md"));
        assertSection(softwareSystem, "Principles", 2, Format.Markdown, "Principles section", 5, section, documentation);

        section = documentation.addSoftwareArchitectureSection(softwareSystem, Format.Markdown, new File(root, "software-architecture.md"));
        assertSection(softwareSystem, "Software Architecture", 3, Format.Markdown, "Software architecture section", 6, section, documentation);

        section = documentation.addContainersSection(softwareSystem, Format.Markdown, new File(root, "containers.md"));
        assertSection(softwareSystem, "Containers", 3, Format.Markdown, "Containers section", 7, section, documentation);

        section = documentation.addComponentsSection(containerA, Format.Markdown, new File(root, "components-for-containerA.md"));
        assertSection(containerA, "Components", 3, Format.Markdown, "Components section for container A", 8, section, documentation);

        section = documentation.addComponentsSection(containerB, Format.Markdown, new File(root, "components-for-containerB.md"));
        assertSection(containerB, "Components", 3, Format.Markdown, "Components section for container B", 9, section, documentation);

        section = documentation.addCodeSection(componentA1, Format.Markdown, new File(root, "code-for-componentA1.md"));
        assertSection(componentA1, "Code", 3, Format.Markdown, "Code section for component A1", 10, section, documentation);

        section = documentation.addCodeSection(componentA2, Format.Markdown, new File(root, "code-for-componentA2.md"));
        assertSection(componentA2, "Code", 3, Format.Markdown, "Code section for component A2", 11, section, documentation);

        section = documentation.addDataSection(softwareSystem, Format.Markdown, new File(root, "data.md"));
        assertSection(softwareSystem, "Data", 3, Format.Markdown, "Data section", 12, section, documentation);

        section = documentation.addInfrastructureArchitectureSection(softwareSystem, Format.Markdown, new File(root, "infrastructure-architecture.md"));
        assertSection(softwareSystem, "Infrastructure Architecture", 4, Format.Markdown, "Infrastructure architecture section", 13, section, documentation);

        section = documentation.addDeploymentSection(softwareSystem, Format.Markdown, new File(root, "deployment.md"));
        assertSection(softwareSystem, "Deployment", 4, Format.Markdown, "Deployment section", 14, section, documentation);

        section = documentation.addDevelopmentEnvironmentSection(softwareSystem, Format.Markdown, new File(root, "development-environment.md"));
        assertSection(softwareSystem, "Development Environment", 4, Format.Markdown, "Development environment section", 15, section, documentation);

        section = documentation.addOperationAndSupportSection(softwareSystem, Format.Markdown, new File(root, "operation-and-support.md"));
        assertSection(softwareSystem, "Operation and Support", 4, Format.Markdown, "Operation and support section", 16, section, documentation);

        section = documentation.addDecisionLog(softwareSystem, Format.Markdown, new File(root, "decision-log.md"));
        assertSection(softwareSystem, "Decision Log", 5, Format.Markdown, "Decision log section", 17, section, documentation);
    }

    @Test
    public void test_addCustomSectionWithContentAsAString() {
        Section section = documentation.addCustomSection(softwareSystem, "Custom Section", 3, Format.Markdown, "Custom content");
        assertSection(softwareSystem, "Custom Section", 3, Format.Markdown, "Custom content", 1, section, documentation);
    }

    @Test
    public void test_addCustomSectionFromAFile() throws IOException {
        File root = new File(".//test/unit/com/structurizr/documentation/structurizr");

        Section section = documentation.addCustomSection(softwareSystem, "Custom Section", 3, Format.Markdown, new File(root, "context.md"));
        assertSection(softwareSystem, "Custom Section", 3, Format.Markdown, "Context section", 1, section, documentation);
    }

    private void assertSection(Element element, String type, int group, Format format, String content, int order, Section section, Documentation documentation) {
        assertTrue(documentation.getSections().contains(section));
        assertEquals(element, section.getElement());
        assertEquals(element.getId(), section.getElementId());
        assertEquals(type, section.getType());
        assertEquals(group, section.getGroup());
        assertEquals(format, section.getFormat());
        assertEquals(content, section.getContent());
        assertEquals(order, section.getOrder());
    }

}