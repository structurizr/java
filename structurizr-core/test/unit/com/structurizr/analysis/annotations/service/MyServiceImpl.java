package com.structurizr.analysis.annotations.service;

import com.structurizr.annotation.UsesComponent;
import com.structurizr.annotation.UsesSoftwareSystem;
import com.structurizr.analysis.annotations.AbstractComponent;
import com.structurizr.analysis.annotations.repository.MyRepository;

@UsesSoftwareSystem(name = "External software system", description = "Sends something to")
class MyServiceImpl extends AbstractComponent implements MyService {

    @UsesComponent(description = "Uses")
    private MyRepository myRepository;

    @Override
    public void doSomething() {
        myRepository.getData();
    }

}
