package com.structurizr.example;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.documentation.Arc42Documentation;
import com.structurizr.documentation.Format;
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
 * An empty software architecture document using the arc42 template.
 *
 * See https://structurizr.com/share/27791/documentation for the live version.
 */
public class Arc42DocumentationExample {

    private static final long WORKSPACE_ID = 27791;
    private static final String API_KEY = "";
    private static final String API_SECRET = "";

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Documentation - arc42", "An empty software architecture document using the arc42 template.");
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

        Arc42Documentation documentation = new Arc42Documentation(workspace);

        // this is the Markdown version
        File documentationRoot = new File("./structurizr-examples/src/com/structurizr/example/documentation/arc42/markdown");
        documentation.addIntroductionAndGoalsSection(softwareSystem, Format.Markdown, new File(documentationRoot, "01-introduction-and-goals.md"));
        documentation.addConstraintsSection(softwareSystem, Format.Markdown, new File(documentationRoot, "02-architecture-constraints.md"));
        documentation.addContextAndScopeSection(softwareSystem, Format.Markdown, new File(documentationRoot, "03-system-scope-and-context.md"));
        documentation.addSolutionStrategySection(softwareSystem, Format.Markdown, new File(documentationRoot, "04-solution-strategy.md"));
        documentation.addBuildingBlockViewSection(softwareSystem, Format.Markdown, new File(documentationRoot, "05-building-block-view.md"));
        documentation.addRuntimeViewSection(softwareSystem, Format.Markdown, new File(documentationRoot, "06-runtime-view.md"));
        documentation.addDeploymentViewSection(softwareSystem, Format.Markdown, new File(documentationRoot, "07-deployment-view.md"));
        documentation.addCrosscuttingConceptsSection(softwareSystem, Format.Markdown, new File(documentationRoot, "08-crosscutting-concepts.md"));
        documentation.addArchitecturalDecisionsSection(softwareSystem, Format.Markdown, new File(documentationRoot, "09-architecture-decisions.md"));
        documentation.addRisksAndTechnicalDebtSection(softwareSystem, Format.Markdown, new File(documentationRoot, "10-quality-requirements.md"));
        documentation.addQualityRequirementsSection(softwareSystem, Format.Markdown, new File(documentationRoot, "11-risks-and-technical-debt.md"));
        documentation.addGlossarySection(softwareSystem, Format.Markdown, new File(documentationRoot, "12-glossary.md"));

        // this is the AsciiDoc version
//        File documentationRoot = new File("./structurizr-examples/src/com/structurizr/example/documentation/arc42/asciidoc");
//        documentation.addIntroductionAndGoalsSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "01-introduction-and-goals.adoc"));
//        documentation.addConstraintsSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "02-architecture-constraints.adoc"));
//        documentation.addContextAndScopeSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "03-system-scope-and-context.adoc"));
//        documentation.addSolutionStrategySection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "04-solution-strategy.adoc"));
//        documentation.addBuildingBlockViewSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "05-building-block-view.adoc"));
//        documentation.addRuntimeViewSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "06-runtime-view.adoc"));
//        documentation.addDeploymentViewSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "07-deployment-view.adoc"));
//        documentation.addCrosscuttingConceptsSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "08-crosscutting-concepts.adoc"));
//        documentation.addArchitecturalDecisionsSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "09-architecture-decisions.adoc"));
//        documentation.addRisksAndTechnicalDebtSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "10-quality-requirements.adoc"));
//        documentation.addQualityRequirementsSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "11-risks-and-technical-debt.adoc"));
//        documentation.addGlossarySection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "12-glossary.adoc"));

        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}