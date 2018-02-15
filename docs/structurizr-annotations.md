# Structurizr annotations

Structurizr for Java includes some custom annotations that you can add to your code. These serve to either make it explicit how components should be extracted from your codebase, or they help supplement the software architecture model.

The annotations can be found in the [structurizr-annotations](https://bintray.com/structurizr/maven/structurizr-java) artifact, which is a very small standalone JAR file containing only the Structurizr annotations. All annotations have a runtime retention policy, so they will be present in the compiled bytecode.

## @Component

A type-level annotation that can be used to signify that the annotated type (an interface or class) can be considered to be a "component". The properties are as follows:

- description: The description of the component (optional).
- technology: The technology of component (optional).

## @UsedByPerson

A type-level annotation that can be used to signify that the named person uses the component on which this annotation is placed, creating a relationship form the person to the component. The properties are as follows:

- name: The name of the person (required).
- description: The description of the relationship (optional).
- technology: The technology of relationship (optional).

## @UsedBySoftwareSystem

A type-level annotation that can be used to signify that the named software system uses the component on which this annotation is placed, creating a relationship from the software system to the component. The properties are as follows:

- name: The name of the software system (required).
- description: The description of the relationship (optional).
- technology: The technology of relationship (optional).

## @UsedByContainer

A type-level annotation that can be used to signify that the named container uses the component on which this annotation is placed, creating a relationship from the container to the component. The properties are as follows:

- name: The name of the container (required).
- description: The description of the relationship (optional).
- technology: The technology of relationship (optional).

If the container resides in the same software system as the component, the simple name can be used to identify the container (e.g. "Database"). Otherwise, the full canonical name of the form "Software System/Container" must be used (e.g. "Some Other Software System/Database").

## @UsesSoftwareSystem

A type-level annotation that can be used to signify that the component on which this annotation is placed has a relationship to the named software system, creating a relationship from the component to the software system. The properties are as follows:

- name: The name of the software system (required).
- description: The description of the relationship (optional).
- technology: The technology of relationship (optional).

## @UsesContainer

A type-level annotation that can be used to signify that the component on which this annotation is placed has a relationship to the named container, creating a relationship from the component to the container. The properties are as follows:

- name: The name of the container (required).
- description: The description of the relationship (optional).
- technology: The technology of relationship (optional).

If the container resides in the same software system as the component, the simple name can be used to identify the container (e.g. "Database"). Otherwise, the full canonical name of the form "Software System/Container" must be used (e.g. "Some Other Software System/Database").

## @UsesComponent

A field-level annotation that can be used to supplement the existing relationship (i.e. add a description and/or technology) between two components.

When using the various component finder strategies, Structurizr for Java will identify components along with the relationships between those components. Since this is typically done using reflection against the compiled bytecode, you'll notice that the description and technology properties of the resulting relationships is always empty. The ```@UsesComponent``` annotation provides a simple way to ensure that such information is added into the model.

The properties are as follows:

- description: The description of the relationship (required).
- technology: The technology of relationship (optional).

## Example

Here are some examples of the annotations, which have been used to create the following diagram.

![](images/structurizr-annotations-1.png)

```java
@Component(description = "Serves HTML pages to users.", technology = "Java")
@UsedByPerson(name = "User", description = "Uses", technology = "HTTPS")
class HtmlController {

    @UsesComponent(description = "Gets data using")
    private Repository repository = new JdbcRepository();

}
```

```java
@Component(description = "Provides access to data stored in the database.", technology = "Java and JPA")
public interface Repository {

    String getData(long id);

}
```

```java
@UsesContainer(name = "Database", description = "Reads from", technology = "JDBC")
class JdbcRepository implements Repository {

    public String getData(long id) {
        return "...";
    }

}
```

See [StructurizrAnnotations.java](https://github.com/structurizr/java/blob/master/structurizr-examples/src/com/structurizr/example/StructurizrAnnotations.java) for the full source code illustrating how to use the various annotations in conjunction with the component finder. The resulting diagrams can be found at [https://structurizr.com/share/36571](https://structurizr.com/share/36571).