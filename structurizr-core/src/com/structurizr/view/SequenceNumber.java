package com.structurizr.view;

class SequenceNumber {

    private SequenceCounter counter = new SequenceCounter();

    SequenceNumber() {
    }

    int getNext() {
        counter.increment();
        return counter.getSequence();
    }

    void startParallelSequence() {
        this.counter = new ParallelSequenceCounter(this.counter);
    }

    void endParallelSequence(boolean endAllParallelSequencesAndContinueNumbering) {
        if (endAllParallelSequencesAndContinueNumbering) {
            int sequence = this.counter.getSequence();
            this.counter = this.counter.getParent();
            this.counter.setSequence(sequence);
        } else {
            this.counter = this.counter.getParent();
        }
    }

}