package com.structurizr.view;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SequenceCounterTests {

    @Test
    public void test_increment_IncrementsTheCounter_WhenThereIsNoParent() {
        SequenceCounter counter = new SequenceCounter();
        assertEquals("0", counter.toString());

        counter.increment();
        assertEquals("1", counter.toString());

        counter.increment();
        assertEquals("2", counter.toString());
    }

}