package com.structurizr.view;

class SequenceCounter implements Cloneable {

    private SequenceCounter parent;
    private int sequence = 0;

    SequenceCounter() {
    }

    SequenceCounter(SequenceCounter parent) {
        this.parent = parent;
    }

    void increment() {
        this.sequence++;
    }

    int getSequence() {
        return this.sequence;
    }

    void setSequence(int sequence) {
        this.sequence = sequence;
    }

    SequenceCounter getParent() {
        return this.parent;
    }

    @Override
    public String toString() {
        return "" + getSequence();
    }

}