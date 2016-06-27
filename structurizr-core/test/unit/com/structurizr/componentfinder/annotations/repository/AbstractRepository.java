package com.structurizr.componentfinder.annotations.repository;

import com.structurizr.annotation.UsesContainer;
import com.structurizr.componentfinder.annotations.AbstractComponent;

@UsesContainer(name = "Database", description = "Reads from and writes to")
public class AbstractRepository extends AbstractComponent {
}