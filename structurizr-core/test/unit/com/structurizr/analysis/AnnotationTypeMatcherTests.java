package com.structurizr.analysis;

import com.structurizr.annotation.Component;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AnnotationTypeMatcherTests {

    @Test
    public void test_construction_DoesNotThrowAnExceptionWhenAnAnnotationClassIsSupplied()
    {
        AnnotationTypeMatcher matcher = new AnnotationTypeMatcher(Component.class, "", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void Test_construction_ThrowsAnExceptionWhenANullAnnotationClassIsSupplied()
    {
        AnnotationTypeMatcher matcher = new AnnotationTypeMatcher(null, "", "");
    }

    @Test
    public void Test_matches_ReturnsFalse_WhenTheGivenTypeDoesNotHaveTheAnnotation()
    {
        AnnotationTypeMatcher matcher = new AnnotationTypeMatcher(Component.class, "", "");
        assertFalse(matcher.matches(MyService.class));
    }

    @Test
    public void Test_Matches_ReturnsTrue_WhenTheGivenTypeDoesHaveTheAnnotation()
    {
        AnnotationTypeMatcher matcher = new AnnotationTypeMatcher(Component.class, "", "");
        assertTrue(matcher.matches(MyController.class));
    }

    @Component
    private class MyController {
    }

    private class MyService {
    }

}
