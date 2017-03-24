# Documentation

In addition to diagrams, Structurizr lets you create supplementary documentation using the Markdown or AsciiDoc formats. See [Documentation](https://structurizr.com/help/documentation) on the Structurizr website for more information about this feature.

![Example documentation](images/documentation-1.png)

## Adding documentation to your workspace

The following [example code](https://github.com/structurizr/java/blob/master/structurizr-examples/src/com/structurizr/example/core/financialrisksystem/FinancialRiskSystem.java) shows how to add documentation to your workspace.

```java
SoftwareSystem financialRiskSystem = model.addSoftwareSystem("Financial Risk System", "Calculates the bank's exposure to risk for product X");

StructurizrDocumentation documentation = new StructurizrDocumentation(model);
workspace.setDocumentation(documentation);

File documentationRoot = new File("./structurizr-examples/src/com/structurizr/example/core/financialrisksystem");
documentation.addContextSection(financialRiskSystem, Format.AsciiDoc, new File(documentationRoot, "context.adoc"));
documentation.addFunctionalOverviewSection(financialRiskSystem, Format.Markdown, new File(documentationRoot, "functional-overview.md"));
documentation.addQualityAttributesSection(financialRiskSystem, Format.Markdown, new File(documentationRoot, "quality-attributes.md"));
documentation.addImages(documentationRoot);
```

In this example, three sections ("Context", "Functional Overview" and "Quality Attributes") are added to the workspace, each of which is associated with the "Financial Risk System" software system that exists in the model. The content for the sections is pulled from the specified file and included into the workspace.

The documentation is broken up into a number of sections, as defined by the implementation, the following of which are included:

- [Structurizr](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/documentation/StructurizrDocumentation.java)
- [arc42](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/documentation/Arc42Documentation.java)

You can add custom sections using the ```addCustomSection``` method, by specifying the section name (a String) and group (an integer, 1-5; this is used for colour coding section navigation buttons):

```java
documentation.addCustomSection(financialRiskSystem, "My other section", 3, Format.Markdown, new File(documentationRoot, "my-other-section.md"));
```

### Images

As shown in [this example Markdown file](https://github.com/structurizr/java/blob/master/structurizr-examples/src/com/structurizr/example/core/financialrisksystem/functional-overview.md), images can be included using the regular Markdown syntax. For this to work, the image files must be hosted externally (e.g. on your own web server) or uploaded with your workspace using the ```addImages()``` or ```addImage()``` methods on the [Documentation class](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/documentation/Documentation.java).

```java
documentation.addImages(documentationRoot);
```

### Software architecture diagrams

Software architecture diagrams from the workspace can be embedded within the documentation sections using an additional special syntax. See [Documentation](https://structurizr.com/help/documentation) on the Structurizr website for more information.
