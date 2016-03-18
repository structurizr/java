package com.structurizr.componentfinder.annotations;

import com.structurizr.annotation.UsesComponent;

public class AbstractComponent {

    @UsesComponent(description = "Writes log entries using")
    protected LoggingComponent loggingComponent;

}
