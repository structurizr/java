# Supplementing the model from source code

Most of the component finder strategies included in Structurizr for Java find components using reflection against the compiled bytecode. Some useful information exists in the source code though; including:

* The type-level doc comment (```/** ... */```, typically extracted using the javadoc tool). This can be used to populate the description property of components.
* The number of lines of source code. This can be a used to calculate the "size" of a component.

A pre-built [SourceCodeComponentFinderStrategy](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/analysis/SourceCodeComponentFinderStrategy.java) is provided to do this.

## Example

Here's an example of how to use the ```SourceCodeComponentFinderStrategy```, taken from the [Spring PetClinic example](spring-petclinic.md).

```java
File sourceRoot = new File("/some/path");

ComponentFinder componentFinder = new ComponentFinder(
    webApplication, "org.springframework.samples.petclinic",
    new SpringComponentFinderStrategy(
            new ReferencedTypesSupportingTypesStrategy(false)
    ),
    new SourceCodeComponentFinderStrategy(new File(sourceRoot, "/src/main/java/"), 150));

componentFinder.findComponents();
```

For every ```CodeElement``` that belongs to every ```Component``` in the ```Container```  passed to the ```ComponentFinder```, the ```SourceCodeComponentFinderStrategy``` will:

* Set the description property, based on the type-level doc comment. The doc comment will be truncated if necessary (e.g. to 150 characters in the example above).
* Set the size property to be the number of lines of the file that the type was found in. 

Additionally, the description property of the ```Component``` will be set to be that of the primary ```CodeElement```, if a description has not been set on the ```Component``` already. 