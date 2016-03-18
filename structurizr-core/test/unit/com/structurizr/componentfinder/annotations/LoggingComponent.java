package com.structurizr.componentfinder.annotations;

import com.structurizr.annotation.Component;
import com.structurizr.annotation.UsesContainer;

@Component(description = "Writes log entries", technology = "Java and Logstash")
@UsesContainer(name = "Logstash", description = "Writes log entries to")
public interface LoggingComponent {

    void log();

}
