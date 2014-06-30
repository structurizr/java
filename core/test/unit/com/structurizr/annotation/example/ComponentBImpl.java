package com.structurizr.annotation.example;

import com.structurizr.annotation.SoftwareSystemDependency;

@SoftwareSystemDependency( target = Dependencies.ExternalSoftwareSystemA, description = "Gets data from" )
public class ComponentBImpl implements ComponentB {

    @Override
    public void doSomethingElse() {
    }

}
