package com.structurizr.analysis;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExtendsClassTypeMatcherTests {

    @Test(expected = IllegalArgumentException.class)
    public void test_construction_ThrowsAnExceptionWhenAnInterfaceTypeIsSupplied()
    {
        new ExtendsClassTypeMatcher(MyRepository.class, "", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_construction_ThrowsAnExceptionWhenAnEnumTypeIsSupplied()
    {
        new ExtendsClassTypeMatcher(MyEnum.class, "", "");
    }

    @Test
    public void test_matches_ReturnsFalse_WhenTheGivenTypeDoesNotExtendTheClass()
    {
        ExtendsClassTypeMatcher matcher = new ExtendsClassTypeMatcher(AbstractComponent.class, "", "");
        assertFalse(matcher.matches(MyOtherClass.class));
    }

    @Test
    public void test_matches_ReturnsTrue_WhenTheGivenTypeDoesExtendTheClass()
    {
        ExtendsClassTypeMatcher matcher = new ExtendsClassTypeMatcher(AbstractComponent.class, "", "");
        assertTrue(matcher.matches(MyController.class));
        assertTrue(matcher.matches(MyRepositoryImpl.class));
    }

    private abstract class AbstractComponent {
    }

    private class MyController extends AbstractComponent {
    }

    private interface MyRepository {
    }

    private class MyRepositoryImpl extends AbstractComponent implements MyRepository {
    }

    private class MyOtherClass {
    }

    private enum MyEnum {
    }

}
