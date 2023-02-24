# Model

This is the definition of the software architecture model, consisting of people, software systems, containers, components, deployment nodes, etc plus the relationships between them.

All of the Java classes representing people, software systems, containers, components, etc, and the functionality related to creating a software architecture model can be found in the [com.structurizr.model](https://github.com/structurizr/java/tree/master/structurizr-core/src/com/structurizr/model) package.

An empty model is created for you when you create a workspace.

```java
Workspace workspace = new Workspace("Name", "Description");
Model model = workspace.getModel();
```

Once you have a reference to a ```Model``` instance, you can add elements to it via the various public `add*` methods that you'll find on ```Model```, ```SoftwareSystem```, ```Container```, etc.

## Automatic model generation

Although there is nothing included in the Structurizr for Java library to support automatic model generation,
you could choose to parse an external definition of your software architecture (e.g. an AWS infrastructure topology, Terraform definition, another Architecture Description Language, your source code, etc)
and create model elements accordingly.