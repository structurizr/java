package com.structurizr.componentfinder.myapp.web;

import com.structurizr.componentfinder.myapp.service.SomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class SomeController {

    @Autowired
    private SomeService someService;

    //@RequestMapping(value = "/do/something") - commenting this out removes a dependency on Spring MVC
    public String showHomePage() {
        someService.doSomething();

        return "/did/something";
    }

}
