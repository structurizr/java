# Documentation

In addition to diagrams, Structurizr lets you create supplementary documentation using the Markdown format. See [Documentation](https://structurizr.com/help/documentation) on the Structurizr website for more information about this feature.

![Example documentation](images/documentation-1.png)

## Adding documentation to your workspace

The following [example code](https://github.com/structurizr/java/blob/master/structurizr-examples/src/com/structurizr/example/core/financialrisksystem/FinancialRiskSystem.java) shows how to add documentation to your workspace.

```java
SoftwareSystem financialRiskSystem = model.addSoftwareSystem("Financial Risk System", "Calculates the bank's exposure to risk for product X");

Documentation documentation = workspace.getDocumentation();
File documentationRoot = new File("./structurizr-examples/src/com/structurizr/example/core/financialrisksystem");

documentation.add(financialRiskSystem, Type.Context, Format.Markdown, new File(documentationRoot, "context.md"));
documentation.add(financialRiskSystem, Type.FunctionalOverview, Format.Markdown, new File(documentationRoot, "functional-overview.md"));
documentation.add(financialRiskSystem, Type.QualityAttributes, Format.Markdown, new File(documentationRoot, "quality-attributes.md"));
```

In this example, three sections (Context, Functional Overview and Quality Attributes) are added to the workspace, each of which is associated with the "Financial Risk System" software system that exists in the model. The content for the sections is pulled from the specified file and included into the workspace.

The documentation is broken up into a number of sections as follows, which are represented by the [Type enum](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/documentation/Type.java).

- Context
- Functional Overview
- Quality Attributes
- Constraints
- Principles
- Software Architecture
- Containers
- Components
- Code
- Data
- Infrastructure Architecture
- Deployment
- Development Environment
- Operation and Support
- Decision Log

All sections must be associated with a software system in the model, except for "Components", which needs to be associated with a container.

### Images

As shown in [this example Markdown file](https://github.com/structurizr/java/blob/master/structurizr-examples/src/com/structurizr/example/core/financialrisksystem/functional-overview.md), images can be included using the regular Markdown syntax. For this to work, the image files must be hosted externally (e.g. on your own web server) or uploaded with your workspace using the ```addImages()``` or ``addImage()``` method on the [Documentation class](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/documentation/Documentation.java).

```java
documentation.addImages(documentationRoot);
```