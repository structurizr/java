# Components and supporting types

In Structurizr, a [Component](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/model/Component.java) is described by a number of properties and can have a number of [CodeElement](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/model/CodeElement.java)s associated with it that represent the Java classes and interfaces that implement and/or support that component.

Because each codebase is different, the mechanism to find a component's supporting types is pluggable via a number of strategies ([SupportingTypesStrategy](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/componentfinder/SupportingTypesStrategy.java)), which can be used in combination. Let's look at the [Spring PetClinic example](spring-petclinic.md) to see how the various supporting types strategies affect the software architecture model.

## 1. No supporting types strategy

This simple example uses the SpringComponentFinderStrategy with no supporting types strategy.

```java
ComponentFinder componentFinder = new ComponentFinder(
    webApplication, "org.springframework.samples.petclinic",
    new SpringComponentFinderStrategy(),
    new SourceCodeComponentFinderStrategy(new File(sourceRoot, "/src/main/java/"), 150));

componentFinder.findComponents();
```

When executed against the compiled version of the Spring PetClinic codebase, each component identified will be made up of only the types found by the component finder strategy. For example, the ```VisitRepository``` component will be made up of an interface (```VisitRepository```) and the implementation class (```JdbcVisitRepositoryImpl```). You can visualise this using [Structurizr's tree exploration](https://structurizr.com/help/explorations).

![](images/supporting-types-1.png)
 
## 2. Referenced types in the same package

Next, let's use the [ReferencedTypesInSamePackageSupportingTypesStrategy](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/componentfinder/ReferencedTypesInSamePackageSupportingTypesStrategy.java) to find all supporting types in the same package as the component type. This is particularly useful when each component neatly resides in its own Java package, and you aren't interested in any other code outside of this package.

```java
ComponentFinder componentFinder = new ComponentFinder(
    webApplication, "org.springframework.samples.petclinic",
    new SpringComponentFinderStrategy(
            new ReferencedTypesInSamePackageSupportingTypesStrategy()
    ),
    new SourceCodeComponentFinderStrategy(new File(sourceRoot, "/src/main/java/"), 150));

componentFinder.findComponents();
```

This strategy finds all of the supporting types that are referenced by the types found by the component finder strategy. In real terms, we've now additionally picked up the ```JdbcVisitRowMapper``` class, since this is used by the ```JdbcVisitRepositoryImpl``` class.

![](images/supporting-types-2.png)

## 3. All referenced types

Finally, let's use the [ReferencedTypesSupportingTypesStrategy](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/componentfinder/ReferencedTypesSupportingTypesStrategy.java) to find all of the referenced types, irrespective of which package they reside in, provided that package sits somewhere underneath ```org.springframework.samples.petclinic```.

```java
ComponentFinder componentFinder = new ComponentFinder(
    webApplication, "org.springframework.samples.petclinic",
    new SpringComponentFinderStrategy(
            new ReferencedTypesSupportingTypesStrategy()
    ),
    new SourceCodeComponentFinderStrategy(new File(sourceRoot, "/src/main/java/"), 150));

componentFinder.findComponents();
```

As the following image illustrates, we now have many more classes that are supporting the implementation of the ```VisitRepository``` component.

![](images/supporting-types-3.png)

This collection of classes may look confusing at first, but the ```JdbcVisitRepositoryImpl``` class references the ```Visit``` class, which in turn references the ```Pet``` class, which in turn references the ```Owner``` class, etc. The Structurizr tree exploration shows that these classes are shared between the ```VisitRepository``` and other components by rendering their names in grey.

## Excluding types

The Structurizr ```ComponentFinder``` will also allow you to exclude types from the component scanning process using one or more regular expressions. If we wanted to exclude the ```BaseEntity``` and ```NamedEntity``` classes in the previous example, we could add the following line of code before calling ```componentFinder.findComponents()```.

```
componentFinder.exclude("org\\.springframework\\.samples\\.petclinic\\.model\\..*Entity");
```

The result is as follows.


![](images/supporting-types-4.png)

## Visualising shared code

[Structurizr's component size exploration](https://structurizr.com/share/1/explore/size-circles) allows you to easily see where types are shared between components.

![](images/supporting-types-5.gif)

The [component and code dependency exploration](https://structurizr.com/share/1/explore/component-and-code-dependencies) also provides a way to easily see shared code.

![](images/supporting-types-6.png)

![](images/supporting-types-7.png)
