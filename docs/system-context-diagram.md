# System Context diagram

A System Context diagram is a good starting point for diagramming and documenting a software system, allowing you to step back and see the big picture. Draw a diagram showing your system as a box in the centre, surrounded by its users and the other systems that it interacts with.

Detail isn't important here as this is your zoomed out view showing a big picture of the system landscape. The focus should be on people (actors, roles, personas, etc) and software systems rather than technologies, protocols and other low-level details. It's the sort of diagram that you could show to non-technical people.

## Example

As an example, a System Context diagram for a simplified, fictional Internet Banking System might look something like this. In summary, it shows that customers of the bank use the Internet Banking System, which itself uses the internal Mainframe Banking System.

![An example System Context diagram](images/system-context-diagram-1.png)

With Structurizr for Java, you can create this diagram with code like the following:

```java
Person customer = model.addPerson(Location.External, "Customer", "A customer of the bank.");

SoftwareSystem internetBankingSystem = model.addSoftwareSystem(Location.Internal, "Internet Banking System", "Allows customers to view information about their bank accounts and make payments.");
customer.uses(internetBankingSystem, "Uses");

SoftwareSystem mainframeBankingSystem = model.addSoftwareSystem(Location.Internal, "Mainframe Banking System", "Stores all of the core banking information about customers, accounts, transactions, etc.");
internetBankingSystem.uses(mainframeBankingSystem, "Uses");

SystemContextView systemContextView = views.createSystemContextView(internetBankingSystem, "SystemContext", "The system context diagram for the Internet Banking System.");
systemContextView.addNearestNeighbours(internetBankingSystem);
```

See [BigBankPlc.java](https://github.com/structurizr/java/blob/master/structurizr-examples/src/com/structurizr/example/BigBankPlc.java) for the full code, and [https://structurizr.com/share/36141#SystemContext](https://structurizr.com/share/36141#SystemContext) for the diagram.