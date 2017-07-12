package com.structurizr.analysis.source.componentC;

import com.structurizr.annotation.UsesComponent;
import com.structurizr.analysis.source.componentA.ComponentA;

class ComponentCImpl implements com.structurizr.analysis.source.componentC.ComponentC {

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