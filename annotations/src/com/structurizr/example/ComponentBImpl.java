package com.structurizr.example;

import com.structurizr.annotation.SoftwareSystemDependency;

@SoftwareSystemDependency( target = Dependencies.System1, description = "Gets data from" )
public class ComponentBImpl implements ComponentB {

    @Override
    public void doSomethingElse() {
    }

}
