package com.structurizr.example.annotations;

import com.structurizr.annotation.UsesContainer;

@UsesContainer(name = "Database", description = "Reads from", technology = "JDBC")
class JdbcRepository implements Repository {

    public String getData(long id) {
        return "...";
    }

}