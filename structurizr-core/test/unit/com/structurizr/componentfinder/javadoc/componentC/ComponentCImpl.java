package com.structurizr.componentfinder.javadoc.componentC;

import com.structurizr.annotation.UsesComponent;
import com.structurizr.componentfinder.javadoc.componentA.ComponentA;

class ComponentCImpl implements com.structurizr.componentfinder.javadoc.componentC.ComponentC {

    @UsesComponent(description = "Uses to do something")
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