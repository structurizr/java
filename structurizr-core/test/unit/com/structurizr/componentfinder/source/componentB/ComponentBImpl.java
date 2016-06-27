package com.structurizr.componentfinder.source.componentB;

import com.structurizr.componentfinder.source.componentA.ComponentA;

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