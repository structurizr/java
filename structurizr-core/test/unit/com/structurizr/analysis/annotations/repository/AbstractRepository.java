package com.structurizr.analysis.annotations.repository;

import com.structurizr.annotation.UsesContainer;
import com.structurizr.analysis.annotations.AbstractComponent;

@UsesContainer(name = "Database", description = "Reads from and writes to")
public class AbstractRepository extends AbstractComponent {
}