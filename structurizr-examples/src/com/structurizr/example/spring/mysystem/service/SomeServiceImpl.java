package com.structurizr.example.spring.mysystem.service;

import com.structurizr.example.spring.mysystem.data.SomeRepository;
import com.structurizr.example.spring.mysystem.domain.Something;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SomeServiceImpl implements SomeService {

    @Autowired
    private SomeRepository someRepository;

    public void doSomething() {
        Something something = someRepository.findSomething();

        // do some "business" stuff
    }

}
