package com.structurizr.componentfinder;

import com.structurizr.componentfinder.myapp.MyController;
import com.structurizr.componentfinder.myapp.MyRepository;
import com.structurizr.componentfinder.myapp.MyRepositoryImpl;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class InterfaceImplementationTypeMatcherTests {


    @Test
    public void test_construction_DoesNotThrowAnExceptionWhenAnInterfaceTypeIsSupplied()
    {
        InterfaceImplementationTypeMatcher matcher = new InterfaceImplementationTypeMatcher(MyRepository.class, "", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void Test_construction_ThrowsAnExceptionWhenANonInterfaceTypeIsSupplied()
    {
        InterfaceImplementationTypeMatcher matcher = new InterfaceImplementationTypeMatcher(MyRepositoryImpl.class, "", "");
    }

    @Test
    public void Test_matches_ReturnsFalse_WhenTheGivenTypeDoesNotImplementTheInterface()
    {
        InterfaceImplementationTypeMatcher matcher = new InterfaceImplementationTypeMatcher(MyRepository.class, "", "");
        assertFalse(matcher.matches(MyController.class));
    }

    @Test
    public void Test_matches_ReturnsTrue_WhenTheGivenTypeDoesImplementTheInterface()
    {
        InterfaceImplementationTypeMatcher matcher = new InterfaceImplementationTypeMatcher(MyRepository.class, "", "");
        assertTrue(matcher.matches(MyRepositoryImpl.class));
    }


}
