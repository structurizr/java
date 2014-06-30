package com.structurizr.annotation.example;

import com.structurizr.annotation.ComponentDependency;
import com.structurizr.annotation.ContainerDependency;

@ContainerDependency( target = Dependencies.RelationalDatabase, description = "Reads from and writes to" )
public class ComponentAImpl implements ComponentA {

    @ComponentDependency( description = "Uses component B to do something else" )
    private ComponentB componentB;

    @Override
    public void doSomething() {
    }

}



