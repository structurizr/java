package com.structurizr.view;

class SequenceNumber {

    private SequenceCounter counter = new SequenceCounter();

    SequenceNumber() {
    }

    String getNext() {
        counter.increment();
        return counter.toString();
    }

    void startChildSequence() {
        this.counter = new SequenceCounter(counter);
    }

    void endChildSequence() {
        counter = this.counter.getParent();
    }

    void startParallelSequence() {
        this.counter = new ParallelSequenceCounter(this.counter);
    }

    void endParallelSequence() {
        this.counter = ((ParallelSequenceCounter)this.counter).getRoot();
    }

}
