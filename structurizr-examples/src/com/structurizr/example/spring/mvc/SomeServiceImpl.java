package com.structurizr.example.spring.mvc;

import org.springframework.stereotype.Service;

@Service
public class SomeServiceImpl implements SomeService {

    private SomeRepository someRepository;

    public void doSomething() {
        someRepository.findSomething();
        System.out.println("Doing something");
    }

}
