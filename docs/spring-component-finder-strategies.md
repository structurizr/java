# Spring component finder strategies

Included in the ```structurizr-spring``` library are a number of component finder strategies that help you identify components in Spring applications, including those built using Spring Boot.

* __SpringMvcControllerComponentFinderStrategy__: A component finder strategy that finds Spring MVC controllers (classes annotated ```@Controller```).
* __SpringRestControllerComponentFinderStrategy__: A component finder strategy that finds Spring REST controllers (classes annotated ```@RestController```).
* __SpringServiceComponentFinderStrategy__: A component finder strategy that finds Spring services (classes annotated ```@Service```).
* __SpringComponentComponentFinderStrategy__: A component finder strategy that finds Spring components (classes annotated ```@Component```).
* __SpringRepositoryComponentFinderStrategy__: A component finder strategy for Spring repositories (classes annotated ```@Repository```, plus those that extend ```JpaRepository``` or ```CrudRepository```).
* __SpringComponentFinderStrategy__: A combined component finder strategy that uses all of the individual strategies listed above.

## Spring naming conventions and interfaces vs implementation classes

Some of the Spring annotations (e.g. ```@Component```, ```@Service``` and ```@Repository```) are typically used to annotate _implementation classes_. For example, a ```JdbcCustomerRepository``` class might be annotated ```@Repository```, rather than the ```CustomerRepository``` interface. This component finder strategy tries to refer to interface types rather than implementation classes, based upon the following assumptions:

1. Having a component named ```CustomerRepository``` is preferable to ```JdbcCustomerRespository```.
2. Other types in the codebase will likely have a dependency on the interface type (e.g. via dependency injection) rather than being coupled to the implementation class.

Given that a class can implement any number of interfaces, for a given implementation class, the component finder strategies will try to find the interface where the name of that interface is included in the name of the implementation class (i.e. ```*Interface```, ```Interface*``` and ```*Interface*```). For example, the following implementation classes will match an interface named ```CustomerRepository```:

* ```JdbcCustomerRepository```
* ```CustomerRepositoryImpl```
* ```JdbcCustomerRepositoryImpl```

## Public vs non-public types

By default, non-public types will be ignored so that, for example, you can hide repository implementations behind services, as described at [Whoops! Where did my architecture go](http://olivergierke.de/2013/01/whoops-where-did-my-architecture-go/). Use the ```setIncludePublicTypesOnly``` method to change this behaviour.

## Example

You can see an example of how to use the Spring component finder strategies in the [Spring PetClinic example](spring-petclinic.md).