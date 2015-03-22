package com.structurizr.componentfinder.javadoc.componentB;

import com.structurizr.componentfinder.javadoc.componentA.ComponentA;

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