# Component finder

The Structurizr for Java library includes a component finder and a number of prebuilt pluggable strategies that allow you to extract components from a codebase.

## Basic usage

To use a component finder, simply create an instance of the ```ComponentFinder``` class and configure it as needed.

```java
Container webApplication = mySoftwareSystem.addContainer("Web Application", "Description", "Apache Tomcat 7.x");

ComponentFinder componentFinder = new ComponentFinder(
    webApplication, "com.mycompany.mysoftwaresystem",
    ... a number of component finder strategies ...);
componentFinder.findComponents();
```

In this case, we're going to find components and associate them with the ```webApplication``` container, and we're only going to find components that reside somewhere underneath the ```com.mycompany.mysoftwaresystem``` package to avoid accidentally finding components residing in frameworks that we might be using.

We also need to plug in one or more component finder strategies, which actually implement the logic to find and extract components from a codebase.

## Component finder strategies

The are a number of component finder strategies already implemented in this GitHub repository and, since the code is open source, you can build your own too. Some of the component finder strategies work using static analysis and reflection techniques against the compiled version of the code (you will need this on your classpath), others by parsing the source code.

Name | Dependency | Description | Extracted from
---- | ---------- | ----------- | --------------
[NameSuffixComponentFinderStrategy](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/componentfinder/NameSuffixComponentFinderStrategy.java) | structurizr-core | Finds components based upon the end of the class name. For example ```*Controller```. | Compiled bytecode
[NamePrefixComponentFinderStrategy](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/componentfinder/NamePrefixComponentFinderStrategy.java) | structurizr-core | Finds components based upon the start of the class name. For example ```Controller*```. | Compiled bytecode
[JavadocComponentFinderStrategy](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/componentfinder/JavadocComponentFinderStrategy.java) | structurizr-core | This component finder strategy doesn't really find components, it instead extracts the top-level Javadoc comment from the code so that this can be added to existing component definitions. | Source code
[SpringComponentFinderStrategy](https://github.com/structurizr/java/blob/master/structurizr-spring/src/com/structurizr/componentfinder/SpringComponentFinderStrategy.java) | structurizr-spring | Finds classes annotated ```@Controller```, ```@Component```, ```@Service``` and ```@Repository``` to extract them as components. | Compiled bytecode<

See the [Spring PetClinic example](spring-petclinic.md) for an illustration of how to use the component finder.