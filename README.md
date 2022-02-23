![Structurizr](docs/images/structurizr-banner.png)

# Structurizr for Java

This GitHub repository is an official client library for the [Structurizr](https://structurizr.com) cloud service and on-premises installation, both of which are web-based publishing platforms for software architecture models based upon the [C4 model](https://c4model.com). The component finder, adr-tools importer, and alternative diagram export formats (e.g. PlantUML) can be found at [Structurizr for Java extensions](https://github.com/structurizr/java-extensions).

## A quick example

As an example, the following Java code can be used to create a software architecture __model__ and an associated __view__ that describes a user using a software system.

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

The view can then be exported to be visualised using the [Structurizr cloud service/on-premises installation](https://structurizr.com), or other formats including PlantUML and WebSequenceDiagrams via the [Structurizr for Java extensions](https://github.com/structurizr/java-extensions).

![Views can be exported and visualised in many ways; e.g. PlantUML, Structurizr and Graphviz](docs/images/readme-1.png)

## Table of contents

* Introduction
    * [Getting started](docs/getting-started.md)
    * [About Structurizr and how it compares to other tooling](https://structurizr.com/help/about)
    * [Why use code?](https://structurizr.com/help/code)
    * [Basic concepts](https://structurizr.com/help/concepts) (workspaces, models, views and documentation)
    * [C4 model](https://c4model.com)
    * [Examples](https://github.com/structurizr/examples)
    * [Binaries](docs/binaries.md)
    * [Building from source](docs/building.md)
    * [API client](docs/api-client.md)
    * [Usage patterns](docs/usage-patterns.md)
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
    * [Graphviz automatic layout](https://github.com/structurizr/java-extensions/blob/master/structurizr-graphviz)
* Documentation
    * [Documentation overview](docs/documentation.md)
    * [Structurizr](docs/documentation-structurizr.md)
    * [arc42](docs/documentation-arc42.md)
    * [Viewpoints and Perspectives](docs/documentation-viewpoints-and-perspectives.md)
    * [Automatic template](docs/documentation-automatic.md)
    * [Architecture decision records](docs/decisions.md)
* Other
	* [HTTP-based health checks](docs/health-checks.md)
    * [Client-side encryption](docs/client-side-encryption.md)
    * [Corporate branding](docs/corporate-branding.md)
* Related projects
    * [java-quickstart](https://github.com/structurizr/java-quickstart): A simple starting point for using Structurizr for Java
    * [java-extensions](https://github.com/structurizr/java-extensions): A collection of Structurizr for Java extensions; including the ability to extract software architecture information from code, export views to PlantUML, etc.
    * [arch-as-code](https://github.com/nahknarmi/arch-as-code): A tool to store software architecture diagrams/documentation as YAML, and publish it to Structurizr.
    * [structurizr-kotlin](https://github.com/Catalysts/structurizr-extensions/tree/master/structurizr-kotlin): An extension for Structurizr that lets you create your models in a fluent way.
    * [structurizr-spring-boot](https://github.com/Catalysts/structurizr-extensions/tree/master/structurizr-spring-boot): A way to apply dependency management to help modularise Structurizr code.
    * [structurizr-groovy](https://github.com/tidyjava/structurizr-groovy): An initial version of a Groovy wrapper around Structurizr for Java.
    * [structurizr-dotnet](https://github.com/structurizr/dotnet): Structurizr for .NET
* [changelog](docs/changelog.md)

[![Build Status](https://travis-ci.org/structurizr/java.svg?branch=master)](https://travis-ci.org/structurizr/java)

