[![Conference recommends](https://devternity.com/shields/recommends.svg)](https://devternity.com)


![Structurizr](docs/images/structurizr-banner.png)

# Structurizr for Java

This GitHub repository is a collection of tooling to help you visualise, document and explore the software architecture of a software system. In summary, it allows you to create a software architecture model based upon Simon Brown's "C4 model" using Java code, and then export that model to be visualised using tools such as:

1. [Structurizr](https://structurizr.com): a web-based service to render web-based software architecture diagrams and supplementary Markdown/AsciiDoc documentation. Diagrams can be viewed online directly or by [embedding them in Atlassian Confluence](https://structurizr.com/help/atlassian-confluence).
1. [PlantUML](docs/plantuml.md): a tool to create UML diagrams using a simple textual domain specific language.
1. [graphviz](docs/graphviz-and-dot.md): a tool to render directed graphs using the DOT format.

As a simple example, the following Java code can be used to create a software architecture model to describe a user using a software system.

```java
Workspace workspace = new Workspace("My model", "This is a model of my software system.");
Model model = workspace.getModel();

Person user = model.addPerson("User", "A user of my software system.");
SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "My software system.");
user.uses(softwareSystem, "Uses");

ViewSet viewSet = workspace.getViews();
SystemContextView contextView = viewSet.createSystemContextView(softwareSystem, "context", "A simple example of a System Context diagram.");
contextView.addAllSoftwareSystems();
contextView.addAllPeople();
```

If using [Structurizr](https://structurizr.com), the end-result, after adding some styling and positioning the diagram elements, is a system context diagram like this:

![Getting Started with Structurizr for Java](docs/images/getting-started.png)

## Table of contents

1. [Building from source](docs/building.md)
1. [Binaries](docs/binaries.md)
1. [Basic concepts](docs/basic-concepts.md)
1. [Getting started](docs/getting-started.md)
1. [API Client](docs/api-client.md)
1. [Styling elements](docs/styling-elements.md)
1. [Styling relationships](docs/styling-relationships.md)
1. [Corporate branding](docs/corporate-branding.md)
1. [Hiding relationships](docs/hiding-relationships.md)
1. [Extracting components from your codebase](docs/extracting-components.md)
1. [The Spring PetClinic example](docs/spring-petclinic.md)
1. [Dynamic views](docs/dynamic-views.md)
1. [Client-side encryption](docs/client-side-encryption.md)
1. [Documentation](docs/documentation.md)
1. [Graphviz and DOT](docs/graphviz-and-dot.md)
1. [PlantUML](docs/plantuml.md)
