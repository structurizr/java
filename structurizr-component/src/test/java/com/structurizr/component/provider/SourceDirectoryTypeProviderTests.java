package com.structurizr.component.provider;

import com.structurizr.component.Type;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SourceDirectoryTypeProviderTests {

    private static final File sources = new File("src/main/java");

    @Test
    void construction_ThrowsAnException_WhenPassedANullDirectory() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new SourceDirectoryTypeProvider(null));
    }

    @Test
    void construction_ThrowsAnException_WhenPassedAPathThatDoesNotExist() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new SourceDirectoryTypeProvider(new File(sources, "com/example")));
    }

    @Test
    void construction_ThrowsAnException_WhenPassedAFile() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new SourceDirectoryTypeProvider(new File(sources, "com/structurizr/component/provider/SourceDirectoryTypeProviderTests.java")));
    }

    @Test
    void getTypes() {
        TypeProvider typeProvider = new SourceDirectoryTypeProvider(sources);
        Set<Type> types = typeProvider.getTypes();

        assertTrue(types.size() > 0);
        assertNotNull(types.stream().filter(t -> t.getFullyQualifiedName().equals("com.structurizr.component.provider.SourceDirectoryTypeProviderTests")));
    }

}