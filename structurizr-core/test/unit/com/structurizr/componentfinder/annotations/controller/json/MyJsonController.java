package com.structurizr.componentfinder.annotations.controller.json;

import com.structurizr.annotation.*;
import com.structurizr.componentfinder.annotations.AbstractComponent;
import com.structurizr.componentfinder.annotations.service.MyService;

@Component(description = "Allows systems to do something")
@UsedBySoftwareSystem(name = "External software system", description = "Uses to do something")
@UsedByContainer(name = "Web Browser", description = "Does something via the API")
class MyJsonController extends AbstractComponent {

    @UsesComponent(description = "Delegates business logic to")
    private MyService myService;

    public void doSomething() {
        myService.doSomething();
        loggingComponent.log();
    }

}
