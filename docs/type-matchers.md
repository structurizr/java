# Type matchers

Structurizr for Java includes a number "type matchers", which when used in conjunction with the [TypeMatcherComponentFinderStrategy](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/analysis/TypeMatcherComponentFinderStrategy.java), provides a simple way to find components in your codebase based upon specific types. The following pre-built type matchers are included.

## NameSuffixTypeMatcher

Matches types where the (simple) name of the type ends with the specified suffix. For example, to find all types named ```*Controller```:

```java
new NameSuffixTypeMatcher("Controller", "", "");
```

## RegexTypeMatcher

Matches types using a regex against the fully qualified type name. For example, to find all types with a fully qualified name of ```*.web.*Controller```:

```java
new RegexTypeMatcher(".*\.web\..*Controller", "", "");
```

## AnnotationTypeMatcher

Matches types based upon the presence of a type-level annotation. For example, to find all types that are annotated with the Java EE ```@Stateless``` annotation:

```java
new AnnotationTypeMatcher(javax.ejb.Stateless.class, "", "");
```

## ImplementsInterfaceTypeMatcher

Matches types where the type implements the specified interface. For example, to find all types that implement the ```Repository``` interface:

```java
new ImplementsInterfaceTypeMatcher(Repository.class, "", "");
```

## ExtendsClassTypeMatcher

Matches types where the type extends the specified class. For example, to find all types that extend the ```AbstractComponent``` class:

```java
new ExtendsClassTypeMatcher(AbstractComponent.class, "", "");
```

## Example

Here is an example of how you might use the type matchers in conjunction with the component finder:

```java
ComponentFinder componentFinder = new ComponentFinder(
	someContainer,
	"com.mycompany.myapp",
	new TypeMatcherComponentFinderStrategy(
		new NameSuffixTypeMatcher("Controller", "Controller description", "Controller technology"),
		new NameSuffixTypeMatcher("Repository", "Repository description", "Repository technology")
	)
);
componentFinder.findComponents();
```

The description and technology properties specified on the type matchers will be used to set the corresponding properties on the ```Component``` instances that are created when matching types are found.

## Writing your own type matchers

You can write your own type matchers by implementing the [TypeMatcher](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/analysis/TypeMatcher.java) interface, or by extending the [AbstractTypeMatcher](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/analysis/AbstractTypeMatcher.java) class.