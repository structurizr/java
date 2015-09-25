package com.structurizr.view;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HierarchicalSequenceCounterTests {

    private HierarchicalSequenceCounter counter;

    @Before
    public void setUp() {
        this.counter = new HierarchicalSequenceCounter();
    }

    @Test
    public void test_increment_IncrementsTheCounter() {
        assertEquals("1", counter.toString());

        counter.increment();
        assertEquals("2", counter.toString());

        counter.increment();
        assertEquals("3", counter.toString());
    }

    @Test
    public void test_counter_WhenThereIsOneParent() {
        assertEquals("1", counter.toString());

        HierarchicalSequenceCounter child = new HierarchicalSequenceCounter(counter);
        assertEquals("1.1", child.toString());

        child.increment();
        assertEquals("1.2", child.toString());

        child.increment();
        assertEquals("1.3", child.toString());
    }

    @Test
    public void test_counter_WhenThereAreTwoParents() {
        assertEquals("1", counter.toString());

        HierarchicalSequenceCounter child = new HierarchicalSequenceCounter(counter);
        assertEquals("1.1", child.toString());

        HierarchicalSequenceCounter grandChild = new HierarchicalSequenceCounter(child);
        assertEquals("1.1.1", grandChild.toString());

        grandChild.increment();
        assertEquals("1.1.2", grandChild.toString());

        grandChild.increment();
        assertEquals("1.1.3", grandChild.toString());

        grandChild = new HierarchicalSequenceCounter(child);
        assertEquals("1.1.1", grandChild.toString());

        grandChild.increment();
        assertEquals("1.1.2", grandChild.toString());

        grandChild.increment();
        assertEquals("1.1.3", grandChild.toString());

        child.increment();
        assertEquals("1.2", child.toString());

        counter.increment();
        assertEquals("2", counter.toString());
    }

}
