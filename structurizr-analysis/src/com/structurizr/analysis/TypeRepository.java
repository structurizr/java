package com.structurizr.analysis;

import java.util.Set;

/**
 * This represents an abstraction for a repository of type information.
 */
public interface TypeRepository {

    /**
     * Gets the package that this type repository is associated with scanning.
     *
     * @return  a fully qualified package name
     */
    String getPackage();

    /**
     * Gets all of the types found by this type repository.
     *
     * @return  a Set of Class objects, or an empty set of no classes were found
     */
    Set<Class<?>> getAllTypes();

    /**
     * Finds the set of types referenced by the specified type.
     *
     * @param typeName                      the starting type
     * @return                              a Set of Class objects, or an empty set if none were found
     */
    Set<Class<?>> findReferencedTypes(String typeName);

    /**
     * Loads the specified type.
     * @param typeName the type to load
     * @return a Class object
     * @throws ClassNotFoundException if the class cannot be found and created
     */
    Class<?> loadClass(String typeName) throws ClassNotFoundException;

}