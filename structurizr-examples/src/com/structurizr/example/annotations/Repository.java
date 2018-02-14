package com.structurizr.example.annotations;

import com.structurizr.annotation.Component;

@Component(description = "Provides access to data stored in the database.", technology = "Java and JPA")
public interface Repository {

    String getData(long id);

}