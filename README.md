# Structurizr for Java

Structurizr is an implementation of the C4 model as described in Simon Brown's
[Software Architecture for Developers](https://leanpub.com/software-architecture-for-developers) book, which provides a way to easily and effectively communicate the software architecture of a software system. Structurizr allows you to create __software architecture models and diagrams as code__. This project contains the Java implementation and tooling.

Everything you see here is a work in progress. See [www.structurizr.com](http://www.structurizr.com) for more information and the [getting started guide](https://www.structurizr.com/getting-started/java) for a simple example.

## Building

To build Struturizr for Java from the sources (you'll need Java 8)...

```
git clone https://github.com/structurizr/java.git

./gradlew build
```

## Binaries
The Structurizr for Java binaries are hosted on [Bintray](https://bintray.com/structurizr/maven/structurizr-java) and the JCenter repository.
The dependencies for use with Maven, Ivy, Gradle, etc are as follows.

Name                                          | Description
-------------------------------------------   | ---------------------------------------------------------------------------------------------------------------------------
com.structurizr:structurizr-core:0.3.1        | The core library that can used to create models.</td>
com.structurizr:structurizr-client:0.3.1      | The structurizr.com API client for Java for uploading models.
com.structurizr:structurizr-spring:0.3.1      | The Spring integration to extract classes annotated @Controller, @Service and @Repository for identification as components.
com.structurizr:structurizr-annotations:0.3.1 | Annotations to add software architecture hints into your own code.
