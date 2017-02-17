package com.structurizr.example.arc42;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.documentation.Arc42Documentation;
import com.structurizr.documentation.Format;
import com.structurizr.example.ExampleWorkspace;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;

import java.io.File;

/**
 * This is a simple example of the arc42 documentation template.
 *
 * See https://structurizr.com/share/27791/documentation for the live version.
 */
public class Arc42Example {

    public static void main(String[] args) throws Exception {
        Workspace workspace = ExampleWorkspace.create();
        workspace.setName("arc42");
        workspace.setDescription("An example of the arc42 documentation template.");
        Model model = workspace.getModel();
        SoftwareSystem softwareSystem = model.getSoftwareSystems().iterator().next();

        Arc42Documentation documentation = new Arc42Documentation(model);
        workspace.setDocumentation(documentation);
        File documentationRoot = new File("./structurizr-examples/src/com/structurizr/example/arc42");

        documentation.addIntroductionAndGoalsSection(softwareSystem, Format.Markdown, "Introduction and goals section...");
        documentation.addConstraintsSection(softwareSystem, Format.Markdown, "Constraints section...");
        documentation.addContextAndScopeSection(softwareSystem, Format.Markdown, new File(documentationRoot, "context.md"));
        documentation.addSolutionStrategySection(softwareSystem, Format.Markdown, "Solution strategy section...");
        documentation.addBuildingBlockViewSection(softwareSystem, Format.Markdown, new File(documentationRoot, "building-block-view"));
        documentation.addRuntimeViewSection(softwareSystem, Format.Markdown, "Runtime view section...");
        documentation.addDeploymentViewSection(softwareSystem, Format.Markdown, "Deployment view section...");
        documentation.addCrosscuttingConceptsSection(softwareSystem, Format.Markdown, "Crosscutting concepts section...");
        documentation.addArchitecturalDecisionsSection(softwareSystem, Format.Markdown, "Architectural decisions section...");
        documentation.addRisksAndTechnicalDebtSection(softwareSystem, Format.Markdown, "Risks and technical debt section...");
        documentation.addQualityRequirementsSection(softwareSystem, Format.Markdown, "Quality requirements section...");
        documentation.addGlossarySection(softwareSystem, Format.Markdown, "Glossary section...");

        StructurizrClient structurizrClient = new StructurizrClient("key", "secret");
        structurizrClient.putWorkspace(27791, workspace);
    }

}