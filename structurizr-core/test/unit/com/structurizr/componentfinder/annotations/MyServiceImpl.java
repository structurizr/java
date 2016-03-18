package com.structurizr.componentfinder.annotations;

import com.structurizr.annotation.UsesComponent;
import com.structurizr.annotation.UsesSoftwareSystem;

@UsesSoftwareSystem(name = "External software system", description = "Sends something to")
class MyServiceImpl extends AbstractComponent implements MyService {

    @UsesComponent(description = "Uses")
    private MyRepository myRepository;

    @Override
    public void doSomething() {
        myRepository.getData();
    }

}
