# structurizr-annotation

[![Maven Central](https://img.shields.io/maven-central/v/com.structurizr/structurizr-annotation.svg?label=Maven%20Central)](https://search.maven.org/artifact/com.structurizr/structurizr-annotation)

This library defines some custom annotations that you can add to your code.
These serve to either make it explicit how components should be extracted from your codebase (e.g. `@Component`),
or they help supplement the software architecture model (e.g. `@Property`, `@Tag`).

- This library has no dependencies.
- All annotations have a runtime retention policy, so they will be present in the compiled bytecode.