package com.structurizr.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SequenceCounterTests {

    @Test
    void increment_IncrementsTheCounter_WhenThereIsNoParent() {
        SequenceCounter counter = new SequenceCounter();
        assertEquals("0", counter.toString());

        counter.increment();
        assertEquals("1", counter.toString());

        counter.increment();
        assertEquals("2", counter.toString());
    }

}