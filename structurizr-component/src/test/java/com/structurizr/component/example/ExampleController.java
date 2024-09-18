package com.structurizr.component.example;

import com.structurizr.annotation.Property;
import com.structurizr.annotation.Tag;

@Tag(name = "Controller")
@Property(name = "Documentation", value = "https://example.com")
class ExampleController implements Controller {

    private Repository exampleRepository = new ExampleRepository();

}