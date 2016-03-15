package com.structurizr.componentfinder.myapp.data;

import com.structurizr.componentfinder.myapp.domain.Something;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcSomeRepository implements SomeRepository {

    @Override
    public Something findSomething() {
        return new Something();
    }

}
