package com.structurizr.example;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.documentation.Arc42DocumentationTemplate;
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
    private static final String API_KEY = "key";
    private static final String API_SECRET = "secret";

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Documentation - arc42", "An empty software architecture document using the arc42 template.");
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

        Arc42DocumentationTemplate template = new Arc42DocumentationTemplate(workspace);

        // this is the Markdown version
        File documentationRoot = new File("./structurizr-examples/src/com/structurizr/example/documentation/arc42/markdown");
        template.addIntroductionAndGoalsSection(softwareSystem, Format.Markdown, new File(documentationRoot, "01-introduction-and-goals.md"));
        template.addConstraintsSection(softwareSystem, Format.Markdown, new File(documentationRoot, "02-architecture-constraints.md"));
        template.addContextAndScopeSection(softwareSystem, Format.Markdown, new File(documentationRoot, "03-system-scope-and-context.md"));
        template.addSolutionStrategySection(softwareSystem, Format.Markdown, new File(documentationRoot, "04-solution-strategy.md"));
        template.addBuildingBlockViewSection(softwareSystem, Format.Markdown, new File(documentationRoot, "05-building-block-view.md"));
        template.addRuntimeViewSection(softwareSystem, Format.Markdown, new File(documentationRoot, "06-runtime-view.md"));
        template.addDeploymentViewSection(softwareSystem, Format.Markdown, new File(documentationRoot, "07-deployment-view.md"));
        template.addCrosscuttingConceptsSection(softwareSystem, Format.Markdown, new File(documentationRoot, "08-crosscutting-concepts.md"));
        template.addArchitecturalDecisionsSection(softwareSystem, Format.Markdown, new File(documentationRoot, "09-architecture-decisions.md"));
        template.addRisksAndTechnicalDebtSection(softwareSystem, Format.Markdown, new File(documentationRoot, "10-quality-requirements.md"));
        template.addQualityRequirementsSection(softwareSystem, Format.Markdown, new File(documentationRoot, "11-risks-and-technical-debt.md"));
        template.addGlossarySection(softwareSystem, Format.Markdown, new File(documentationRoot, "12-glossary.md"));

        // this is the AsciiDoc version
//        File documentationRoot = new File("./structurizr-examples/src/com/structurizr/example/documentation/arc42/asciidoc");
//        template.addIntroductionAndGoalsSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "01-introduction-and-goals.adoc"));
//        template.addConstraintsSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "02-architecture-constraints.adoc"));
//        template.addContextAndScopeSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "03-system-scope-and-context.adoc"));
//        template.addSolutionStrategySection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "04-solution-strategy.adoc"));
//        template.addBuildingBlockViewSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "05-building-block-view.adoc"));
//        template.addRuntimeViewSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "06-runtime-view.adoc"));
//        template.addDeploymentViewSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "07-deployment-view.adoc"));
//        template.addCrosscuttingConceptsSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "08-crosscutting-concepts.adoc"));
//        template.addArchitecturalDecisionsSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "09-architecture-decisions.adoc"));
//        template.addRisksAndTechnicalDebtSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "10-quality-requirements.adoc"));
//        template.addQualityRequirementsSection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "11-risks-and-technical-debt.adoc"));
//        template.addGlossarySection(softwareSystem, Format.AsciiDoc, new File(documentationRoot, "12-glossary.adoc"));

        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}