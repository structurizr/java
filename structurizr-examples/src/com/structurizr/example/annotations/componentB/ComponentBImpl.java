package com.structurizr.example.annotations.componentB;

import com.structurizr.annotation.UsesComponent;
import com.structurizr.example.annotations.componentA.ComponentA;

class ComponentBImpl implements ComponentB {

    @UsesComponent(description = "Uses to do something")
    private ComponentA componentA;

    /**
     * Constructor-based dependency injection.
     */
    public ComponentBImpl(ComponentA componentA) {
        this.componentA = componentA;
    }

    @Override
    public void doSomethingElse() {
        System.out.println("Doing something else");
        componentA.doSomething();
    }

}