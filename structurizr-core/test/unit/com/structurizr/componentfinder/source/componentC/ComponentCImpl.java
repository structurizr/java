package com.structurizr.componentfinder.source.componentC;

import com.structurizr.annotation.UsesComponent;
import com.structurizr.componentfinder.source.componentA.ComponentA;

class ComponentCImpl implements com.structurizr.componentfinder.source.componentC.ComponentC {

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