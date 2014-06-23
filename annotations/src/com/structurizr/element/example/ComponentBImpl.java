package com.structurizr.element.example;

import com.structurizr.element.SoftwareSystemDependency;

@SoftwareSystemDependency( target = Dependencies.System1, description = "Gets data from" )
public class ComponentBImpl implements ComponentB {

    @Override
    public void doSomethingElse() {
    }

}
