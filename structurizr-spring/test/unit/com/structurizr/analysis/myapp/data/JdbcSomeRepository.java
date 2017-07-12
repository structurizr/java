package com.structurizr.analysis.myapp.data;

import com.structurizr.analysis.myapp.domain.Something;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcSomeRepository implements SomeRepository {

    @Override
    public Something findSomething() {
        return new Something();
    }

}
