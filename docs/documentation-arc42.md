# arc42 documentation template

Structurizr for Java includes an implementation of the [arc42 documentation template](http://arc42.org), which can be used to document your software architecture.

## Example

To use this template, create an instance of the [Arc42DocumentationTemplate](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/documentation/Arc42DocumentationTemplate.java) class.
You can then add documentation sections as needed, each associated with a software system in your software architecture model, using Markdown or AsciiDoc. For example:

```java
Arc42DocumentationTemplate template = new Arc42DocumentationTemplate(workspace);

File documentationRoot = new File("./structurizr-examples/src/com/structurizr/example/documentation/arc42/markdown");
template.addIntroductionAndGoalsSection(softwareSystem, new File(documentationRoot, "01-introduction-and-goals.md"));
template.addConstraintsSection(softwareSystem, new File(documentationRoot, "02-architecture-constraints.md"));
template.addContextAndScopeSection(softwareSystem, new File(documentationRoot, "03-system-scope-and-context.md"));
template.addSolutionStrategySection(softwareSystem, new File(documentationRoot, "04-solution-strategy.md"));
template.addBuildingBlockViewSection(softwareSystem, new File(documentationRoot, "05-building-block-view.md"));
template.addRuntimeViewSection(softwareSystem, new File(documentationRoot, "06-runtime-view.md"));
template.addDeploymentViewSection(softwareSystem, new File(documentationRoot, "07-deployment-view.md"));
template.addCrosscuttingConceptsSection(softwareSystem, new File(documentationRoot, "08-crosscutting-concepts.md"));
template.addArchitecturalDecisionsSection(softwareSystem, new File(documentationRoot, "09-architecture-decisions.md"));
template.addRisksAndTechnicalDebtSection(softwareSystem, new File(documentationRoot, "10-quality-requirements.md"));
template.addQualityRequirementsSection(softwareSystem, new File(documentationRoot, "11-risks-and-technical-debt.md"));
template.addGlossarySection(softwareSystem, new File(documentationRoot, "12-glossary.md"));
```

Structurizr will create navigation controls based upon the the sections in the documentation, and the software systems they have been associated with. See [Arc42DocumentationExample.java](https://github.com/structurizr/java/blob/master/structurizr-examples/src/com/structurizr/example/Arc42DocumentationExample.java) for the full code, and [https://structurizr.com/share/27791/documentation](https://structurizr.com/share/27791/documentation) to see the rendered documentation.

## More information

See [Help - Documentation](https://structurizr.com/help/documentation) for more information about how headings are rendered, and how to embed diagrams from you workspace into the documentation.