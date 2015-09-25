package com.structurizr.view;

class HierarchicalSequenceCounter {

    private HierarchicalSequenceCounter parent = null;
    private int sequence = 1;

    HierarchicalSequenceCounter() {
    }

    HierarchicalSequenceCounter(HierarchicalSequenceCounter parent) {
        this.parent = parent;
    }

    void increment() {
        this.sequence++;
    }

    public HierarchicalSequenceCounter getParent() {
        return parent;
    }

    @Override
    public String toString() {
        if (parent == null) {
            return "" + sequence;
        } else {
            return parent.toString() + "." + sequence;
        }
    }
}
