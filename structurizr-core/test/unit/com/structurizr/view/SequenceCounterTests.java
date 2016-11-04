package com.structurizr.view;

import org.junit.Before;
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

    @Test
    public void test_counter_WhenThereIsOneParent() {
        SequenceCounter parent = new SequenceCounter();
        parent.increment();
        assertEquals("1", parent.toString());

        SequenceCounter child = new SequenceCounter(parent);
        child.increment();
        assertEquals("1.1", child.toString());

        child.increment();
        assertEquals("1.2", child.toString());
    }

    @Test
    public void test_counter_WhenThereAreTwoParents() {
        SequenceCounter parent = new SequenceCounter();
        parent.increment();
        assertEquals("1", parent.toString());

        SequenceCounter child = new SequenceCounter(parent);
        child.increment();
        assertEquals("1.1", child.toString());

        SequenceCounter grandchild = new SequenceCounter(child);
        grandchild.increment();
        assertEquals("1.1.1", grandchild.toString());

        grandchild.increment();
        assertEquals("1.1.2", grandchild.toString());
    }

}
