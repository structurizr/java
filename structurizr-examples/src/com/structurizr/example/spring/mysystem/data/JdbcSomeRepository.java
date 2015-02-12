package com.structurizr.example.spring.mysystem.data;

import com.structurizr.example.spring.mysystem.domain.Something;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcSomeRepository implements SomeRepository {

    @Override
    public Something findSomething() {
        return new Something();
    }

}
