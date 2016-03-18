package com.structurizr.componentfinder.annotations;

import com.structurizr.annotation.Component;

@Component(description = "Manages some data")
public interface MyRepository {

    void getData();

}