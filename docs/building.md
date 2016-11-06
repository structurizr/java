# Building

[![Build Status](https://travis-ci.org/structurizr/java.svg?branch=master)](https://travis-ci.org/structurizr/java)

To build "Structurizr for Java" from the sources (you'll need Java 8)...

```
git clone https://github.com/structurizr/java.git
cd java
./gradlew build
```

If necessary, after building, you can install "Structurizr for Java" into your local Maven repo using:

```
./gradlew publishToMavenLocal
```