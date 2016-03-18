package com.structurizr.componentfinder.annotations;

import com.structurizr.annotation.Component;
import com.structurizr.annotation.UsesComponent;
import com.structurizr.annotation.UsedByPerson;

@Component(description = "Allows users to do something")
@UsedByPerson(name = "User 1", description = "Uses to do something")
@UsedByPerson(name = "User 2", description = "Uses to do something too")
class MyHtmlController extends AbstractComponent {

    @UsesComponent(description = "Delegates business logic to")
    private MyService myService;

    public void doSomething() {
        myService.doSomething();
        loggingComponent.log();
    }

}
