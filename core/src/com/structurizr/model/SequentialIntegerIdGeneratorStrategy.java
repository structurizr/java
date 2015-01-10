package com.structurizr.model;

class SequentialIntegerIdGeneratorStrategy implements IdGenerator {

    private int ID = 0;

    @Override
    public synchronized String generateId(Element element) {
        return "" + ++ID;
    }

    @Override
    public synchronized String generateId(Relationship relationship) {
        return "" + ++ID;
    }

}
