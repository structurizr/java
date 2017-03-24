# Binaries
The "Structurizr for Java" binaries are hosted on [Bintray](https://bintray.com/structurizr/maven/structurizr-java) and the JCenter repository.
The dependencies for use with Maven, Ivy, Gradle, etc are as follows.

Name                                          | Description
-------------------------------------------   | ---------------------------------------------------------------------------------------------------------------------------
com.structurizr:structurizr-core:1.0.0-RC3        | The core library that can used to create and upload models to Structurizr.
com.structurizr:structurizr-spring:1.0.0-RC3      | The Spring integration to extract classes annotated ```@Controller```, ```@RestController```, ```@Component```, ```@Service``` and ```@Repository```, plus classes that extend ```JpaRepository```.
com.structurizr:structurizr-annotations:1.0.0-RC3 | Annotations to add software architecture hints into your own code.
com.structurizr:structurizr-dot:1.0.0-RC3 | Export the Structurizr views to a DOT file, for use with graphviz.