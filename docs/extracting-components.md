# Extracting components from your codebase

The Structurizr for Java library includes a component finder and a number of prebuilt pluggable strategies that allow you to extract components from a codebase.

## Background

The idea behind the [C4 model](c4.md) and Structurizr is that there are a number of levels of abstraction sitting above the code. Although we write Java code using interfaces and classes in packages, it's often useful to think about how that code is organised into "components". In its simplest form, a "component" is just a grouping of classes and interfaces.

If you reverse-engineer some Java code using a UML tool, you'll typically get a UML class diagram showing all of the classes and interfaces. The component diagram in the C4 model is about hiding some of this complexity and implementation detail. You can read more about this at [Components vs classes](https://structurizr.com/help/components-vs-classes).

## Purpose

The purpose of the [component finder](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/analysis/ComponentFinder.java) is to find components in your codebase. Since every codebase is different (i.e. code structure, naming conventions, frameworks used, etc), different pluggable component finder strategies allow you to customize the rules that you use to find components.
Some example rules that you might use to find components include:

- All classes where the name ends with ```Component```.
- All classes where the name ends with ```Controller```.
- All classes that are annotated with the Spring ```@Repository``` annotation.
- All classes that inherit from ```AbstractComponent```.
- etc

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
[TypeMatcherComponentFinderStrategy](type-matchers.md) | structurizr-core | A component finder strategy that uses type information to find components, based upon a number of pluggable TypeMatcher implementations (e.g. [NameSuffixTypeMatcher](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/analysis/NameSuffixTypeMatcher.java), [ImplementsInterfaceTypeMatcher](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/analysis/ImplementsInterfaceTypeMatcher.java), [RegexTypeMatcher](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/analysis/RegexTypeMatcher.java) and [AnnotationTypeMatcher](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/analysis/AnnotationTypeMatcher.java)). | Compiled bytecode
[SpringComponentFinderStrategy](https://github.com/structurizr/java/blob/master/structurizr-spring/src/com/structurizr/analysis/SpringComponentFinderStrategy.java) | structurizr-spring | Finds types annotated ```@Controller```, ```@RestController```, ```@Component```, ```@Service``` and ```@Repository```, plus classes that extend ```JpaRepository```. | Compiled bytecode
[StructurizrAnnotationsComponentFinderStrategy](structurizr-annotations.md) | structurizr-core | Finds the Structurizr annotations  ```@Component```, ```@UsedByPerson```, ```@UsedBySoftwareSystem```, ```@UsedByContainer```, ```@UsesSoftwareSystem```, ```@UsesContainer``` and ```@UsesComponent```. | Compiled bytecode
[SourceCodeComponentFinderStrategy](supplementing-from-source-code.md) | structurizr-core | This component finder strategy doesn't really find components, it instead extracts the top-level Javadoc comment from the code so that this can be added to existing component definitions. It also calculates the size of components, based upon the number of lines of source code. | Source code
