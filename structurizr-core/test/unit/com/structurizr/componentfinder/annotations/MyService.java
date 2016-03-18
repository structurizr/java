package com.structurizr.componentfinder.annotations;

import com.structurizr.annotation.Component;
import com.structurizr.annotation.UsedByPerson;

@Component(description = "A component that does some business logic")
public interface MyService {

    void doSomething();

}