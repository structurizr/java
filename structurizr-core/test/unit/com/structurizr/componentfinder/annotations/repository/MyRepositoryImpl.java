package com.structurizr.componentfinder.annotations.repository;

import com.structurizr.annotation.UsesContainer;

@UsesContainer(name = "Database", description = "Reads from and writes to")
class MyRepositoryImpl extends AbstractRepository implements MyRepository {

    @Override
    public void getData() {
    }

}