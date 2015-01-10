package com.structurizr.model;

public interface IdGenerator {

    String generateId(Element element);

    String generateId(Relationship relationship);

}
