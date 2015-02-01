package com.structurizr.example.spring.mvc;

import org.springframework.stereotype.Repository;

@Repository
public class JdbcSomeRepository implements SomeRepository {

    @Override
    public String findSomething() {
        return "Something";
    }

}
