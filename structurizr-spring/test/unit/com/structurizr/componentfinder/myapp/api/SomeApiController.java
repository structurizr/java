package com.structurizr.componentfinder.myapp.api;

import com.structurizr.componentfinder.myapp.service.SomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SomeApiController {

    @Autowired
    private SomeService someService;

    //@RequestMapping(value = "/do/something") - commenting this out removes a dependency on Spring MVC
    public String findSomething() {
        someService.doSomething();

        return "{some json}";
    }

}
