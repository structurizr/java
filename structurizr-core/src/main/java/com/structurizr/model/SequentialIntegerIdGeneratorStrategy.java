package com.structurizr.model;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * An ID generator that simply uses a sequential number when generating IDs for model elements and relationships.
 * This is the default ID generator; any non-numeric IDs are ignored.
 */
public class SequentialIntegerIdGeneratorStrategy implements IdGenerator {

    private final AtomicInteger id = new AtomicInteger();

    @Override
    public String generateId(Element element) {
        return Integer.toString(id.incrementAndGet());
    }

    @Override
    public String generateId(Relationship relationship) {
        return Integer.toString(id.incrementAndGet());
    }

    @Override
    public void found(String id) {
        try {
            int idAsInt = Integer.parseInt(id);
            if (idAsInt > this.id.get()) {
                this.id.set(idAsInt);
            }
        } catch (NumberFormatException e) {
            // ignore non-numeric IDs
        }
    }

}