package com.structurizr.model;

class SequentialIntegerIdGeneratorStrategy implements IdGenerator {

    private int ID = 0;

    public void found(String id) {
        try {
            int idAsInt = Integer.parseInt(id);
            if (idAsInt > ID) {
                ID = idAsInt;
            }
        }
        catch(NumberFormatException e) {
            // ignore non-numeric ids
        }
    }

    @Override
    public synchronized String generateId(Element element) {
        return "" + ++ID;
    }

    @Override
    public synchronized String generateId(Relationship relationship) {
        return "" + ++ID;
    }

}
