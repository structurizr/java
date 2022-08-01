package com.structurizr.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SequenceNumberTests {

    @Test
    void test_increment() {
        SequenceNumber sequenceNumber = new SequenceNumber();
        assertEquals("1", sequenceNumber.getNext());
        assertEquals("2", sequenceNumber.getNext());
    }

    @Test
    void test_parallelSequences() {
        SequenceNumber sequenceNumber = new SequenceNumber();
        assertEquals("1", sequenceNumber.getNext());

        sequenceNumber.startParallelSequence();
        assertEquals("2", sequenceNumber.getNext());
        sequenceNumber.endParallelSequence(false);

        sequenceNumber.startParallelSequence();
        assertEquals("2", sequenceNumber.getNext());
        sequenceNumber.endParallelSequence(true);

        assertEquals("3", sequenceNumber.getNext());
    }

}
