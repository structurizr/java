package com.structurizr.componentfinder.annotations.componentB;

import com.structurizr.annotation.ComponentDependency;
import com.structurizr.componentfinder.annotations.componentA.ComponentA;

class ComponentBImpl implements ComponentB {

    @ComponentDependency(description = "Does something with")
    private ComponentA componentA;

    public ComponentBImpl(ComponentA componentA) {
        this.componentA = componentA;
    }

    @Override
    public void doSomethingElse() {
        System.out.println("Doing something else");
        componentA.doSomething();
    }

}