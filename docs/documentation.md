# Documentation

In addition to diagrams, Structurizr lets you create supplementary documentation using the Markdown format. See [Documentation](https://structurizr.com/help/documentation) for more information.

![Example documentation](images/documentation-1.png)

## Adding documentation to your workspace

The following example code shows how to add documentation to your workspace.

```java
SoftwareSystem financialRiskSystem = model.addSoftwareSystem("Financial Risk System", "Calculates the bank's exposure to risk for product X");

Documentation documentation = workspace.getDocumentation();
File documentationRoot = new File("./structurizr-examples/src/com/structurizr/example/core/financialrisksystem");
documentation.add(financialRiskSystem, Type.Context, Format.Markdown, new File(documentationRoot, "context.md"));
documentation.add(financialRiskSystem, Type.FunctionalOverview, Format.Markdown, new File(documentationRoot, "functional-overview.md"));
documentation.add(financialRiskSystem, Type.QualityAttributes, Format.Markdown, new File(documentationRoot, "quality-attributes.md"));
documentation.addImages(documentationRoot);

```

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

With the exception of "Components", all sections are linked to a specific software system in the model.

### Images

As shown in [this example](https://github.com/structurizr/java/blob/master/structurizr-examples/src/com/structurizr/example/core/financialrisksystem/functional-overview.md), images can be included using the regular Markdown syntax. For this to work, the image files must be hosted externally (e.g. on your own web server) or uploaded alongside your workspace using the ```addImages()``` method on the [Documentation class](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/documentation/Documentation.java).