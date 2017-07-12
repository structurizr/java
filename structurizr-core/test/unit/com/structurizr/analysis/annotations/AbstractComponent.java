package com.structurizr.analysis.annotations;

import com.structurizr.annotation.UsesComponent;
import com.structurizr.analysis.annotations.logging.LoggingComponent;

public class AbstractComponent {

    @UsesComponent(description = "Writes log entries using")
    protected LoggingComponent loggingComponent;

}
