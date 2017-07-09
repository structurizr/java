package com.structurizr.example;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.StructurizrDocumentation;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
import com.structurizr.view.Shape;
import com.structurizr.view.Styles;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

import java.io.File;

/**
 * An empty software architecture document using the Structurizr template.
 *
 * See https://structurizr.com/share/14181/documentation for the live version.
 */
public class StructurizrDocumentationExample {

    private static final long WORKSPACE_ID = 14181;
    private static final String API_KEY = "";
    private static final String API_SECRET = "";

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Documentation - Structurizr", "An empty software architecture document using the Structurizr template.");
        Model model = workspace.getModel();
        ViewSet viewSet = workspace.getViews();

        Person user = model.addPerson("User", "A user of my software system.");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "My software system.");
        user.uses(softwareSystem, "Uses");

        SystemContextView contextView = viewSet.createSystemContextView(softwareSystem, "SystemContext", "An example of a System Context diagram.");
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        Styles styles = viewSet.getConfiguration().getStyles();
        styles.addElementStyle(Tags.PERSON).shape(Shape.Person);

        StructurizrDocumentation documentation = new StructurizrDocumentation(workspace);

        // this is the Markdown version
        File documentationRoot = new File("./structurizr-examples/src/com/structurizr/example/documentation/structurizr/markdown");
        documentation.addContextSection(softwareSystem, Format.Markdown, new File(documentationRoot, "01-context.md"));
        documentation.addFunctionalOverviewSection(softwareSystem, Format.Markdown, new File(documentationRoot, "02-functional-overview.md"));
        documentation.addQualityAttributesSection(softwareSystem, Format.Markdown, new File(documentationRoot, "03-quality-attributes.md"));
        documentation.addConstraintsSection(softwareSystem, Format.Markdown, new File(documentationRoot, "04-constraints.md"));
        documentation.addPrinciplesSection(softwareSystem, Format.Markdown, new File(documentationRoot, "05-principles.md"));
        documentation.addSoftwareArchitectureSection(softwareSystem, Format.Markdown, new File(documentationRoot, "06-software-architecture.md"));
        documentation.addDataSection(softwareSystem, Format.Markdown, new File(documentationRoot, "07-data.md"));
        documentation.addInfrastructureArchitectureSection(softwareSystem, Format.Markdown, new File(documentationRoot, "08-infrastructure-architecture.md"));
        documentation.addDeploymentSection(softwareSystem, Format.Markdown, new File(documentationRoot, "09-deployment.md"));
        documentation.addDevelopmentEnvironmentSection(softwareSystem, Format.Markdown, new File(documentationRoot, "10-development-environment.md"));
        documentation.addOperationAndSupportSection(softwareSystem, Format.Markdown, new File(documentationRoot, "11-operation-and-support.md"));
        documentation.addDecisionLog(softwareSystem, Format.Markdown, new File(documentationRoot, "12-decision-log.md"));

        // this is the AsciiDoc version
//        File documentationRoot = new File("./structurizr-examples/src/com/structurizr/example/documentation/structurizr/asciidoc");
//        documentation.addContextSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "01-context.adoc"));
//        documentation.addFunctionalOverviewSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "02-functional-overview.adoc"));
//        documentation.addQualityAttributesSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "03-quality-attributes.adoc"));
//        documentation.addConstraintsSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "04-constraints.adoc"));
//        documentation.addPrinciplesSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "05-principles.adoc"));
//        documentation.addSoftwareArchitectureSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "06-software-architecture.adoc"));
//        documentation.addDataSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "07-data.adoc"));
//        documentation.addInfrastructureArchitectureSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "08-infrastructure-architecture.adoc"));
//        documentation.addDeploymentSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "09-deployment.adoc"));
//        documentation.addDevelopmentEnvironmentSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "10-development-environment.adoc"));
//        documentation.addOperationAndSupportSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "11-operation-and-support.adoc"));
//        documentation.addDecisionLog(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "12-decision-log.adoc"));

        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}