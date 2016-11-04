package com.structurizr.view;

class ParallelSequenceCounter extends SequenceCounter {

    private SequenceCounter root = null;

    ParallelSequenceCounter(SequenceCounter parent) {
        super(parent);
        this.root = (SequenceCounter)parent.clone();

        setSequence(parent.getSequence());
    }

    @Override
    void increment() {
        getParent().increment();
    }

    SequenceCounter getRoot() {
        return this.root;
    }

    @Override
    public String toString() {
        return getParent().toString();
    }
}
