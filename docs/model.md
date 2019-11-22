# Model

This is the definition of the software architecture model, consisting of people, software systems, containers, components, code elements and deployment nodes, plus the relationships between them.

All of the Java classes representing people, software systems, containers, components, etc, and the functionality related to creating a software architecture model can be found in the [com.structurizr.model](https://github.com/structurizr/java/tree/master/structurizr-core/src/com/structurizr/model) package.

An empty model is created for you when you create a workspace.

```java
Workspace workspace = new Workspace("Getting Started", "This is a model of my software system.");
Model model = workspace.getModel();
```

Once you have a reference to a ```Model``` instance, you can add elements to it manually or automatically, using static analysis and reflection techniques.

## 1. Manual model creation

Manually adding elements to the model is the simplest way to use the Structurizr for Java client library. This can be done using the various public ```add*``` methods that you'll find on ```Model```, ```SoftwareSystem```, ```Container```, ```Component```, etc.

## 2. Automatic extraction

You can also extract components (and add them to a ```Container``` instance) automatically from a given codebase, using a number of different component finder strategies. See [Component finder](https://github.com/structurizr/java-extensions/blob/master/docs/component-finder.md) for more details.

Although there is nothing included in the Structurizr for Java library to support this, you could also choose to parse an external definition of your software architecture (e.g. an AWS infrastructure topology, another Architecture Description Language, etc) and create model elements accordingly.