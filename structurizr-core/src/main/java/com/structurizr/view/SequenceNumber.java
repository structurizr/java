package com.structurizr.view;

class SequenceNumber {

    private SequenceCounter counter = new SequenceCounter();

    SequenceNumber() {
    }

    String getNext() {
        counter.increment();
        return counter.toString();
    }

    void startParallelSequence() {
        this.counter = new ParallelSequenceCounter(this.counter);
    }

    void endParallelSequence(boolean endAllParallelSequencesAndContinueNumbering) {
        if (endAllParallelSequencesAndContinueNumbering) {
            if (counter.incremented()) {
                // relationships were added in this parallel sequence
                int sequence = this.counter.getSequence();
                this.counter = this.counter.getParent();
                this.counter.setSequence(sequence);
            } else {
                // no relationships were added in this parallel sequence, so treat this as a group of parallel sequences
                int sequence = this.counter.getSequence();
                this.counter = this.counter.getParent();
                this.counter.setSequence(sequence);
                this.counter.increment();
            }
        } else {
            this.counter = this.counter.getParent();
        }
    }

}