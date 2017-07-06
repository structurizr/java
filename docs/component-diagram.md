# Component diagram

Following on from a Container Diagram, next you can zoom in and decompose each container further to identify the major structural building blocks and their interactions.

The Component diagram shows how a container is made up of a number of components, what each of those components are, their responsibilities and the technology/implementation details.

## Example

As an example, a Component diagram for the Web Application of a simplified, fictional Internet Banking System might look something like this. In summary, it shows the components that make up the Web Application, and the relationships between them.

![An example Component diagram](images/component-diagram-1.png)

With Structurizr for Java, you can create this diagram with code like the following:

```java
Component homePageController = webApplication.addComponent("Home Page Controller", "Serves up the home page.", "Spring MVC Controller");
Component signinController = webApplication.addComponent("Sign In Controller", "Allows users to sign in to the Internet Banking System.", "Spring MVC Controller");
Component accountsSummaryController = webApplication.addComponent("Accounts Summary Controller", "Provides customers with an summary of their bank accounts.", "Spring MVC Controller");
Component securityComponent = webApplication.addComponent("Security Component", "Provides functionality related to signing in, changing passwords, etc.", "Spring Bean");
Component mainframeBankingSystemFacade = webApplication.addComponent("Mainframe Banking System Facade", "A facade onto the mainframe banking system.", "Spring Bean");

webApplication.getComponents().stream().filter(c -> "Spring MVC Controller".equals(c.getTechnology())).forEach(c -> customer.uses(c, "Uses", "HTTPS"));
signinController.uses(securityComponent, "Uses");
accountsSummaryController.uses(mainframeBankingSystemFacade, "Uses");
securityComponent.uses(database, "Reads from and writes to", "JDBC");
mainframeBankingSystemFacade.uses(mainframeBankingSystem, "Uses", "XML/HTTPS");

ComponentView componentView = views.createComponentView(webApplication, "Components", "The components diagram for the Web Application");
componentView.addAllContainers();
componentView.addAllComponents();
componentView.add(customer);
componentView.add(mainframeBankingSystem);
```

See [BigBankPlc.java](https://github.com/structurizr/java/blob/master/structurizr-examples/src/com/structurizr/example/core/BigBankPlc.java) for the full code, and [https://structurizr.com/share/36141#Components](https://structurizr.com/share/36141#Components) for the diagram.

### Extracting components automatically

Please note that, in a real-world scenario, you would probably want to [extract components](extracting-components.md) automatically from a codebase, using static analysis and reflection techniques. 