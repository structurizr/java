package com.structurizr.analysis;

import com.structurizr.annotation.Component;
import com.structurizr.annotation.UsedByPerson;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class TypeUtilsTests {

    @Test
    public void test_getCategory_ThrowsAnException_WhenTheSpecifiedTypeCouldNotBeFound() throws Exception {
        try {
            TypeUtils.getCategory("com.company.app.Class");
            fail();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            assertEquals("The specified type could not be found.", iae.getMessage());
        }
    }

    @Test
    public void test_getCategory_ReturnsInterface_WhenTheSpecifiedTypeIsAnInterface() throws Exception {
        TypeCategory typeCategory = TypeUtils.getCategory("com.structurizr.analysis.defaultTypeRepository.SomeInterface");
        assertSame(TypeCategory.INTERFACE, typeCategory);
    }

    @Test
    public void test_getCategory_ReturnsAbstractClass_WhenTheSpecifiedTypeIsAnAbstractClass() throws Exception {
        TypeCategory typeCategory = TypeUtils.getCategory("com.structurizr.analysis.defaultTypeRepository.SomeAbstractClass");
        assertSame(TypeCategory.ABSTRACT_CLASS, typeCategory);
    }

    @Test
    public void test_getCategory_ReturnsAbstractClass_WhenTheSpecifiedTypeIsAClass() throws Exception {
        TypeCategory typeCategory = TypeUtils.getCategory("com.structurizr.analysis.defaultTypeRepository.SomeClass");
        assertSame(TypeCategory.CLASS, typeCategory);
    }

    @Test
    public void test_getCategory_ReturnsEnum_WhenTheSpecifiedTypeIsAnEnum() throws Exception {
        TypeCategory typeCategory = TypeUtils.getCategory("com.structurizr.analysis.defaultTypeRepository.SomeEnum");
        assertSame(TypeCategory.ENUM, typeCategory);
    }

    @Test
    public void test_getVisibility_ThrowsAnException_WhenTheSpecifiedTypeCouldNotBeFound() throws Exception {
        try {
            TypeUtils.getVisibility("com.company.app.Class");
            fail();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            assertEquals("The specified type could not be found.", iae.getMessage());
        }
    }

    @Test
    public void test_getVisibility_ReturnsPublic_WhenTheSpecifiedTypeIsPublic() throws Exception {
        TypeVisibility typeCategory= TypeUtils.getVisibility("com.structurizr.analysis.defaultTypeRepository.SomeInterface");
        assertSame(TypeVisibility.PUBLIC, typeCategory);
    }

    @Test
    public void test_getVisibility_ReturnsPackage_WhenTheSpecifiedTypeIsPackageScoped() throws Exception {
        TypeVisibility typeCategory= TypeUtils.getVisibility("com.structurizr.analysis.defaultTypeRepository.SomeClass");
        assertSame(TypeVisibility.PACKAGE, typeCategory);
    }

    @Test
    public void test_findTypesAnnotatedWith_ThrowsAnException_WhenANullAnnotationTypeIsSpecified() {
        try {
            TypeUtils.findTypesAnnotatedWith(null, new HashSet<>());
            fail();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            assertEquals("An annotation type must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_findTypesAnnotatedWith_ReturnsAnEmptySet_WhenNoTypesWithTheSpecifiedAnnotationAreFound() throws Exception {
        Set<Class<?>> typesToSearch = new HashSet<>();
        typesToSearch.add(ClassLoader.getSystemClassLoader().loadClass("com.structurizr.analysis.defaultTypeRepository.SomeClass"));
        Set<Class<?>> types = TypeUtils.findTypesAnnotatedWith(UsedByPerson.class, typesToSearch);
        assertTrue(types.isEmpty());
    }

    @Test
    public void test_findTypesAnnotatedWith_ReturnsANonEmptySet_WhenTypesWithTheSpecifiedAnnotationAreFound() throws Exception {
        Set<Class<?>> typesToSearch = new HashSet<>();
        typesToSearch.add(ClassLoader.getSystemClassLoader().loadClass("com.structurizr.analysis.defaultTypeRepository.SomeClass"));
        Set<Class<?>> types = TypeUtils.findTypesAnnotatedWith(Component.class, typesToSearch);
        assertEquals(1, types.size());
        assertEquals("com.structurizr.analysis.defaultTypeRepository.SomeClass", types.iterator().next().getCanonicalName());
    }

    @Test
    public void test_getFirstImplementationOfInterface_ThrowsAnException_WhenANullInterfaceIsSpecified() {
        try {
            TypeUtils.findFirstImplementationOfInterface(null, new HashSet<>());
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("An interface type must be provided.", iae.getMessage());
        }
    }

    @Test
    public void test_getFirstImplementationOfInterface_ThrowsAnException_WhenANonInterfaceIsSpecified() {
        try {
            TypeUtils.findFirstImplementationOfInterface(this.getClass(), new HashSet<>());
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The interface type must represent an interface.", iae.getMessage());
        }
    }

    @Test
    public void test_getFirstImplementationOfInterface_ThrowsAnException_WhenANullSetOfTypesIsSpecified() {
        try {
            TypeUtils.findFirstImplementationOfInterface(SomeInterface.class, null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The set of types to search through must be provided.", iae.getMessage());
        }
    }

    @Test
    public void test_getFirstImplementationOfInterface_ReturnsNull_WhenAnImplementationCannotBeFound() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(AnotherClass.class);
        Class implementationClass = TypeUtils.findFirstImplementationOfInterface(SomeInterface.class, classes);
        assertNull(implementationClass);
    }

    @Test
    public void test_getFirstImplementationOfInterface_ReturnsNull_WhenOnlyTheInterfaceIsFound() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(SomeInterface.class);
        Class implementationClass = TypeUtils.findFirstImplementationOfInterface(SomeInterface.class, classes);
        assertNull(implementationClass);
    }

    @Test
    public void test_getFirstImplementationOfInterface_ReturnsNull_WhenOnlyAnAbstractImplementationIsFound() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(SomeAbstractImplementationClass.class);
        Class implementationClass = TypeUtils.findFirstImplementationOfInterface(SomeInterface.class, classes);
        assertNull(implementationClass);
    }

    @Test
    public void test_getFirstImplementationOfInterface_ReturnsAnImplementation_WhenAnConcreteImplementationIsFound() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(SomeInterface.class);
        classes.add(SomeAbstractImplementationClass.class);
        classes.add(SomeImplementationClass.class);
        Class implementationClass = TypeUtils.findFirstImplementationOfInterface(SomeInterface.class, classes);
        assertSame(SomeImplementationClass.class, implementationClass);
    }

    private interface SomeInterface {}
    private abstract class SomeAbstractImplementationClass implements SomeInterface {}
    private class SomeImplementationClass extends SomeAbstractImplementationClass {}
    private class AnotherClass {}

}