package com.structurizr.analysis;

import com.structurizr.annotation.Component;
import com.structurizr.annotation.UsedByPerson;
import org.junit.Test;
import test.TypeUtils.AnotherClass;
import test.TypeUtils.SomeInterface;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TypeUtilsTests {

    private static final TypeRepository types = new DefaultTypeRepository(emptyList(), emptySet(), null);

    @Test
    public void test_getCategory_ReturnsNull_WhenTheSpecifiedTypeCouldNotBeFound() throws Exception {
        assertNull(TypeUtils.getCategory(types, "com.company.app.Class"));
    }

    @Test
    public void test_getCategory_ReturnsInterface_WhenTheSpecifiedTypeIsAnInterface() throws Exception {
        TypeCategory typeCategory = TypeUtils.getCategory(types, "test.TypeUtils.SomeInterface");
        assertSame(TypeCategory.INTERFACE, typeCategory);
    }

    @Test
    public void test_getCategory_ReturnsAbstractClass_WhenTheSpecifiedTypeIsAnAbstractClass() throws Exception {
        TypeCategory typeCategory = TypeUtils.getCategory(types, "test.TypeUtils.SomeAbstractClass");
        assertSame(TypeCategory.ABSTRACT_CLASS, typeCategory);
    }

    @Test
    public void test_getCategory_ReturnsAbstractClass_WhenTheSpecifiedTypeIsAClass() throws Exception {
        TypeCategory typeCategory = TypeUtils.getCategory(types, "test.TypeUtils.SomeClass");
        assertSame(TypeCategory.CLASS, typeCategory);
    }

    @Test
    public void test_getCategory_ReturnsEnum_WhenTheSpecifiedTypeIsAnEnum() throws Exception {
        TypeCategory typeCategory = TypeUtils.getCategory(types, "test.TypeUtils.SomeEnum");
        assertSame(TypeCategory.ENUM, typeCategory);
    }

    @Test
    public void test_getVisibility_ReturnsNull_WhenTheSpecifiedTypeCouldNotBeFound() throws Exception {
        assertNull(TypeUtils.getVisibility(types, "com.company.app.Class"));
    }

    @Test
    public void test_getVisibility_ReturnsPublic_WhenTheSpecifiedTypeIsPublic() throws Exception {
        TypeVisibility typeCategory= TypeUtils.getVisibility(types, "test.TypeUtils.SomeInterface");
        assertSame(TypeVisibility.PUBLIC, typeCategory);
    }

    @Test
    public void test_getVisibility_ReturnsPackage_WhenTheSpecifiedTypeIsPackageScoped() throws Exception {
        TypeVisibility typeCategory= TypeUtils.getVisibility(types, "test.TypeUtils.SomeClass");
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
        typesToSearch.add(ClassLoader.getSystemClassLoader().loadClass("test.TypeUtils.SomeClass"));
        Set<Class<?>> types = TypeUtils.findTypesAnnotatedWith(UsedByPerson.class, typesToSearch);
        assertTrue(types.isEmpty());
    }

    @Test
    public void test_findTypesAnnotatedWith_ReturnsANonEmptySet_WhenTypesWithTheSpecifiedAnnotationAreFound() throws Exception {
        Set<Class<?>> typesToSearch = new HashSet<>();
        typesToSearch.add(ClassLoader.getSystemClassLoader().loadClass("test.TypeUtils.SomeClass"));
        Set<Class<?>> types = TypeUtils.findTypesAnnotatedWith(Component.class, typesToSearch);
        assertEquals(1, types.size());
        assertEquals("test.TypeUtils.SomeClass", types.iterator().next().getCanonicalName());
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
    public void test_getFirstImplementationOfInterface_ReturnsNull_WhenOnlyAnAbstractImplementationIsFound() throws Exception {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(Class.forName("test.TypeUtils.SomeAbstractClass"));
        Class implementationClass = TypeUtils.findFirstImplementationOfInterface(SomeInterface.class, classes);
        assertNull(implementationClass);
    }

    @Test
    public void test_getFirstImplementationOfInterface_ReturnsAnImplementation_WhenAnConcreteImplementationIsFound() throws Exception {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(SomeInterface.class);
        classes.add(Class.forName("test.TypeUtils.SomeAbstractClass"));
        classes.add(Class.forName("test.TypeUtils.SomeClass"));
        Class implementationClass = TypeUtils.findFirstImplementationOfInterface(SomeInterface.class, classes);
        assertSame("test.TypeUtils.SomeClass", implementationClass.getCanonicalName());
    }

}