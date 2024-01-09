package com.structurizr.view;

class ParallelSequenceCounter extends SequenceCounter {

    ParallelSequenceCounter(SequenceCounter parent) {
        super(parent);
        setSequence(parent.getSequence());
    }

}