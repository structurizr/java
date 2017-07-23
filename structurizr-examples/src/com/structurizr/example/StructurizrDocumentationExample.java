package com.structurizr.example;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.StructurizrDocumentationTemplate;
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
        ViewSet views = workspace.getViews();

        Person user = model.addPerson("User", "A user of my software system.");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "My software system.");
        user.uses(softwareSystem, "Uses");

        SystemContextView contextView = views.createSystemContextView(softwareSystem, "SystemContext", "An example of a System Context diagram.");
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle(Tags.PERSON).shape(Shape.Person);

        StructurizrDocumentationTemplate template = new StructurizrDocumentationTemplate(workspace);

        // this is the Markdown version
        File documentationRoot = new File("./structurizr-examples/src/com/structurizr/example/documentation/structurizr/markdown");
        template.addContextSection(softwareSystem, new File(documentationRoot, "01-context.md"));
        template.addFunctionalOverviewSection(softwareSystem, new File(documentationRoot, "02-functional-overview.md"));
        template.addQualityAttributesSection(softwareSystem, new File(documentationRoot, "03-quality-attributes.md"));
        template.addConstraintsSection(softwareSystem, new File(documentationRoot, "04-constraints.md"));
        template.addPrinciplesSection(softwareSystem, new File(documentationRoot, "05-principles.md"));
        template.addSoftwareArchitectureSection(softwareSystem, new File(documentationRoot, "06-software-architecture.md"));
        template.addDataSection(softwareSystem, new File(documentationRoot, "07-data.md"));
        template.addInfrastructureArchitectureSection(softwareSystem, new File(documentationRoot, "08-infrastructure-architecture.md"));
        template.addDeploymentSection(softwareSystem, new File(documentationRoot, "09-deployment.md"));
        template.addDevelopmentEnvironmentSection(softwareSystem, new File(documentationRoot, "10-development-environment.md"));
        template.addOperationAndSupportSection(softwareSystem, new File(documentationRoot, "11-operation-and-support.md"));
        template.addDecisionLogSection(softwareSystem, new File(documentationRoot, "12-decision-log.md"));

        // this is the AsciiDoc version
//        File documentationRoot = new File("./structurizr-examples/src/com/structurizr/example/documentation/structurizr/asciidoc");
//        template.addContextSection(softwareSystem, new File(documentationRoot, "01-context.adoc"));
//        template.addFunctionalOverviewSection(softwareSystem, new File(documentationRoot, "02-functional-overview.adoc"));
//        template.addQualityAttributesSection(softwareSystem, new File(documentationRoot, "03-quality-attributes.adoc"));
//        template.addConstraintsSection(softwareSystem, new File(documentationRoot, "04-constraints.adoc"));
//        template.addPrinciplesSection(softwareSystem, new File(documentationRoot, "05-principles.adoc"));
//        template.addSoftwareArchitectureSection(softwareSystem, new File(documentationRoot, "06-software-architecture.adoc"));
//        template.addDataSection(softwareSystem, new File(documentationRoot, "07-data.adoc"));
//        template.addInfrastructureArchitectureSection(softwareSystem, new File(documentationRoot, "08-infrastructure-architecture.adoc"));
//        template.addDeploymentSection(softwareSystem, new File(documentationRoot, "09-deployment.adoc"));
//        template.addDevelopmentEnvironmentSection(softwareSystem, new File(documentationRoot, "10-development-environment.adoc"));
//        template.addOperationAndSupportSection(softwareSystem, new File(documentationRoot, "11-operation-and-support.adoc"));
//        template.addDecisionLogSection(softwareSystem, new File(documentationRoot, "12-decision-log.adoc"));

        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}