package com.structurizr.element.example;

import com.structurizr.element.ComponentDependency;
import com.structurizr.element.ContainerDependency;

@ContainerDependency( target = Dependencies.RelationalDatabase, description = "Reads from and writes to" )
public class ComponentAImpl implements ComponentA {

    @ComponentDependency( description = "Uses this other thing to do something else" )
    private ComponentB componentB;

    @Override
    public void doSomething() {
    }

}



