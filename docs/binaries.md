# Binaries
The "Structurizr for Java" binaries are hosted on [Bintray](https://bintray.com/structurizr/maven/structurizr-java) and the JCenter repository.
The dependencies for use with Maven, Ivy, Gradle, etc are as follows.

Name                                          | Description
-------------------------------------------   | ---------------------------------------------------------------------------------------------------------------------------
com.structurizr:structurizr-core:1.0.0-RC4        | structurizr-core is the basic library that can used to create software architecture models.
com.structurizr:structurizr-spring:1.0.0-RC4      | structurizr-spring is a library that will help you create a software architecture model of your Spring-based application. It uses reflection to find components in your code that correspond to Java types annotated ```@Controller```, ```@RestController```, ```@Component```, ```@Service``` and ```@Repository```, plus those that extend ```JpaRepository```.
com.structurizr:structurizr-annotations:1.0.0-RC4 | structurizr-annotations is a very small library that allows you to add software architecture hints into your own code.
com.structurizr:structurizr-dot:1.0.0-RC4 | structurizr-dot is a library to export the view definitions to a DOT file, so they can be rendered with graphviz.