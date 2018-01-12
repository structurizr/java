# System Landscape diagram

A System Landscape diagram is really the same as the [System Context diagram](system-context-diagram.md), without a focus on a specific software system. It can help to provide a broader view of the people and software systems that are related to and reside within a given enterprise context (e.g. a business or organisation).

## Example

As an example, a System Landscape diagram for a simplified, fictional bank might look something like this.

![An example System Landscape diagram](images/system-landscape-diagram-1.png)

With Structurizr for Java, you can create this diagram with code like the following:

```java
Person customer = model.addPerson(Location.External, "Customer", "A customer of the bank.");

SoftwareSystem internetBankingSystem = model.addSoftwareSystem(Location.Internal, "Internet Banking System", "Allows customers to view information about their bank accounts and make payments.");
customer.uses(internetBankingSystem, "Uses");

SoftwareSystem mainframeBankingSystem = model.addSoftwareSystem(Location.Internal, "Mainframe Banking System", "Stores all of the core banking information about customers, accounts, transactions, etc.");
internetBankingSystem.uses(mainframeBankingSystem, "Uses");

SoftwareSystem atm = model.addSoftwareSystem(Location.Internal, "ATM", "Allows customers to withdraw cash.");
atm.uses(mainframeBankingSystem, "Uses");
customer.uses(atm, "Withdraws cash using");

Person bankStaff = model.addPerson(Location.Internal, "Bank Staff", "Staff within the bank.");
bankStaff.uses(mainframeBankingSystem, "Uses");

EnterpriseContextView enterpriseContextView = views.createEnterpriseContextView("EnterpriseContext", "The system context diagram for the Internet Banking System.");
enterpriseContextView.addAllElements();
```

See [BigBankPlc.java](https://github.com/structurizr/java/blob/master/structurizr-examples/src/com/structurizr/example/BigBankPlc.java) for the full code, and [https://structurizr.com/share/36141#SystemLandscape](https://structurizr.com/share/36141#SystemLandscape) for the diagram.