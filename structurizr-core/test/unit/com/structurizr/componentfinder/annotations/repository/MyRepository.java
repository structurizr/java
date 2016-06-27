package com.structurizr.componentfinder.annotations.repository;

import com.structurizr.annotation.Component;

@Component(description = "Manages some data")
public interface MyRepository {

    void getData();

}