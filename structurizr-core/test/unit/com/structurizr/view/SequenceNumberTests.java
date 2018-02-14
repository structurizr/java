package com.structurizr.view;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SequenceNumberTests {

    @Test
    public void test_increment() {
        SequenceNumber sequenceNumber = new SequenceNumber();
        assertEquals("1", sequenceNumber.getNext());
        assertEquals("2", sequenceNumber.getNext());
    }

    @Test
    public void test_childSequence() {
        SequenceNumber sequenceNumber = new SequenceNumber();
        assertEquals("1", sequenceNumber.getNext());

        sequenceNumber.startChildSequence();
        assertEquals("1.1", sequenceNumber.getNext());
        assertEquals("1.2", sequenceNumber.getNext());

        sequenceNumber.endChildSequence();
        assertEquals("2", sequenceNumber.getNext());
    }

    @Test
    public void test_parallelSequences() {
        SequenceNumber sequenceNumber = new SequenceNumber();
        assertEquals("1", sequenceNumber.getNext());

        sequenceNumber.startParallelSequence();
        assertEquals("2", sequenceNumber.getNext());
        sequenceNumber.endParallelSequence();

        sequenceNumber.startParallelSequence();
        assertEquals("2", sequenceNumber.getNext());
        sequenceNumber.endParallelSequence();

        assertEquals("2", sequenceNumber.getNext());
    }

}
