package com.structurizr.componentfinder.componentC;

import com.structurizr.annotation.ComponentDependency;
import com.structurizr.componentfinder.componentA.ComponentA;

class ComponentCImpl implements ComponentC {

    @ComponentDependency(description = "Uses to do something")
    private ComponentA componentA;

    public ComponentCImpl(ComponentA componentA) {
        this.componentA = componentA;
    }

    @Override
    public void doSomethingElse() {
        System.out.println("Doing something else");
        componentA.doSomething();
    }

}