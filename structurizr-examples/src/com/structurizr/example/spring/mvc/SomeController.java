package com.structurizr.example.spring.mvc;

import org.springframework.stereotype.Controller;

@Controller
public class SomeController {

    private SomeService someService;

    // this should be annotated with @RequestMapping
    public String showHomePage() {
        someService.doSomething();
        return "home";
    }

}
