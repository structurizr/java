package com.structurizr.analysis.source.componentB;

import com.structurizr.analysis.source.componentA.ComponentA;

class ComponentBImpl implements ComponentB {

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