package com.structurizr.componentfinder.annotations;

import com.structurizr.annotation.UsesComponent;
import com.structurizr.componentfinder.annotations.logging.LoggingComponent;

public class AbstractComponent {

    @UsesComponent(description = "Writes log entries using")
    protected LoggingComponent loggingComponent;

}
