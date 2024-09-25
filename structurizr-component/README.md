# structurizr-component

[![Maven Central](https://img.shields.io/maven-central/v/com.structurizr/structurizr-component.svg?label=Maven%20Central)](https://search.maven.org/artifact/com.structurizr/structurizr-component)

This library provides a facility to discover components in a Java codebase, via a combination of
[Apache Commons BCEL](https://commons.apache.org/proper/commons-bcel/) and [JavaParser](https://javaparser.org),
using a pluggable and customisable set of matching and filtering rules.
It is also available via the Structurizr DSL `!components` keyword.

See the following tests for an example:

- https://github.com/structurizr/java/blob/master/structurizr-component/src/test/java/com/structurizr/component/SpringPetClinicTests.java
- https://github.com/structurizr/java/blob/master/structurizr-dsl/src/test/resources/dsl/spring-petclinic/workspace.dsl