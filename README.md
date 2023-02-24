![Structurizr](docs/images/structurizr-banner.png)

# Structurizr for Java

This GitHub repository is (1) a client library for the [Structurizr](https://structurizr.com) cloud service and on-premises installation
and (2) a way to create a Structurizr workspace using Java code. Looking for the [Structurizr DSL](https://github.com/structurizr/dsl) instead?

## A quick example

As an example, the following Java code can be used to create a software architecture __model__ and an associated __view__ that describes a user using a software system, based upon the [C4 model](https://c4model.com).

```java
public static void main(String[] args) throws Exception {
    Workspace workspace = new Workspace("Getting Started", "This is a model of my software system.");
    Model model = workspace.getModel();
    
    Person user = model.addPerson("User", "A user of my software system.");
    SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "My software system.");
    user.uses(softwareSystem, "Uses");
    
    ViewSet views = workspace.getViews();
    SystemContextView contextView = views.createSystemContextView(softwareSystem, "SystemContext", "An example of a System Context diagram.");
    contextView.addAllSoftwareSystems();
    contextView.addAllPeople();
}
```

The view can then be exported to be visualised using the [Structurizr cloud service/on-premises installation/Lite](https://structurizr.com),
or other formats including PlantUML, Mermaid, DOT, and WebSequenceDiagrams via the [structurizr-export library](https://github.com/structurizr/export).

## Table of contents

* [changelog](docs/changelog.md)
* Introduction
    * [Getting started](docs/getting-started.md)
    * [Basic concepts](https://structurizr.com/help/concepts) (workspaces, models, views and documentation)
    * [C4 model](https://c4model.com)
    * [Examples](https://github.com/structurizr/examples)
    * [Binaries](docs/binaries.md)
    * [Building from source](docs/building.md)
    * [API client](docs/api-client.md)
    * [FAQ](docs/faq.md)
* Model
	* [Creating your model](docs/model.md)
	* [Implied relationships](docs/implied-relationships.md)
* Views
	* [Creating views](docs/views.md)
    * [System Context diagram](docs/system-context-diagram.md)
    * [Container diagram](docs/container-diagram.md)
    * [Component diagram](docs/component-diagram.md)
    * [Dynamic diagram](docs/dynamic-diagram.md)
    * [Deployment diagram](docs/deployment-diagram.md)
    * [System Landscape diagram](docs/system-landscape-diagram.md)
    * [Styling elements](docs/styling-elements.md)
    * [Styling relationships](docs/styling-relationships.md)
    * [Filtered views](docs/filtered-views.md)
* Other
    * [Client-side encryption](docs/client-side-encryption.md)
* Related projects
    * [structurizr-dsl](https://github.com/structurizr/dsl): A text-based DSL for authoring Structurizr workspaces.
    * [structurizr-export](https://github.com/structurizr/export): Export model and views to external formats (e.g. PlantUML, Mermaid, etc).
    * [structurizr-import](https://github.com/structurizr/import): Import Markdown/AsciiDoc documentation/ADRs into a Structurizr workspace.

