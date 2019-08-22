package com.structurizr.view;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SequenceNumberTests {

    @Test
    public void test_increment() {
        SequenceNumber sequenceNumber = new SequenceNumber();
        assertEquals(1, sequenceNumber.getNext());
        assertEquals(2, sequenceNumber.getNext());
    }

    @Test
    public void test_parallelSequences() {
        SequenceNumber sequenceNumber = new SequenceNumber();
        assertEquals(1, sequenceNumber.getNext());

        sequenceNumber.startParallelSequence();
        assertEquals(2, sequenceNumber.getNext());
        sequenceNumber.endParallelSequence(false);

        sequenceNumber.startParallelSequence();
        assertEquals(2, sequenceNumber.getNext());
        sequenceNumber.endParallelSequence(true);

        assertEquals(3, sequenceNumber.getNext());
    }

}
