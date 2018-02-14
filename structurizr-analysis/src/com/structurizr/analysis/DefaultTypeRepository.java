package com.structurizr.analysis;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.AbstractScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

/**
 * This is an implementation of a TypeRepository that uses a combination of:
 *  - The Reflections library (https://github.com/ronmamo/reflections).
 *  - Javassist (http://jboss-javassist.github.io/javassist/)
 *  - Java Reflection
 */
public class DefaultTypeRepository implements TypeRepository {

    private static final Log log = LogFactory.getLog(DefaultTypeRepository.class);

    private final Set<Class<?>> types;
    private final ClassLoader classLoader;

    private String packageToScan;
    private Set<Pattern> exclusions = new HashSet<>();

    private ClassPool classPool;
    private Map<String, Set<Class<?>>> referencedTypesCache = new HashMap<>();

    /**
     * Creates a new instance based upon a package to scan and a set of exclusions.
     *
     * @param packageToScan     the fully qualified package name
     * @param exclusions        a Set of Pattern objects
     */
    DefaultTypeRepository(String packageToScan, Set<Pattern> exclusions, URLClassLoader urlClassLoader) {
        final Collection<URL> urls;
        if (urlClassLoader==null) {
            classLoader = ClassLoader.getSystemClassLoader();
            urls = ClasspathHelper.forJavaClassPath();
            classPool = ClassPool.getDefault();
        }
        else {
            classLoader = urlClassLoader;
            urls = asList(urlClassLoader.getURLs());
            classPool = new ClassPool();
            classPool.insertClassPath(new LoaderClassPath(urlClassLoader));
        }

        this.packageToScan = packageToScan;
        if (exclusions != null) {
            this.exclusions.addAll(exclusions);
        }

        AllTypesScanner allTypesScanner = new AllTypesScanner();
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(urls)
                .filterInputsBy(new FilterBuilder().includePackage(packageToScan))
                .setScanners(new SubTypesScanner(false), allTypesScanner)
        );

        types = new HashSet<>();
        types.addAll(ReflectionUtils.forNames(allTypesScanner.types, classLoader));

        for (Class<?> c : types) {
            System.out.println("+ " + c);
        }
    }

    @Override
    public Class<?> loadClass(String typeName) throws ClassNotFoundException {
        return classLoader.loadClass(typeName);
    }

    /**
     * Gets the package that this type repository is associated with scanning.
     *
     * @return  a fully qualified package name
     */
    public String getPackage() {
        return packageToScan;
    }

    /**
     * Gets all of the types found by this type repository.
     *
     * @return  a Set of Class objects, or an empty set of no classes were found
     */
    public Set<Class<?>> getAllTypes() {
        return new HashSet<>(types);
    }

    /**
     * Finds the set of types referenced by the specified type.
     *
     * @param typeName  the starting type
     * @return          a Set of Class objects, or an empty set if none were found
     */
    public Set<Class<?>> findReferencedTypes(String typeName) {
        Set<Class<?>> referencedTypes = new HashSet<>();

        // use the cached version if possible
        if (referencedTypesCache.containsKey(typeName)) {
            return referencedTypesCache.get(typeName);
        }

        try {
            CtClass cc = classPool.get(typeName);
            for (Object referencedType : cc.getRefClasses()) {
                String referencedTypeName = (String)referencedType;

                if (!isExcluded(referencedTypeName)) {
                    try {
                        referencedTypes.add(loadClass(referencedTypeName));
                    } catch (Throwable t) {
                        log.debug("Could not find " + referencedTypeName + " ... ignoring.");
                    }
                }
            }

            // remove the type itself
            referencedTypes.remove(loadClass(typeName));
        } catch (Exception e) {
            log.debug("Error finding referenced types for " + typeName + " ... ignoring.");

            // since there was an error, we can't find the set of referenced types from it, so...
            referencedTypesCache.put(typeName, new HashSet<>());
        }

        // cache for the next time
        referencedTypesCache.put(typeName, referencedTypes);

        return referencedTypes;
    }

    private Set<Class<?>> filter(Set<Class<?>> types) {
        return types.stream().filter(c -> !isExcluded(c.getCanonicalName())).collect(Collectors.toSet());
    }

    private boolean isExcluded(String typeName) {
        if (typeName == null) {
            return true;
        }

        for (Pattern exclude : exclusions) {
            if (exclude.matcher(typeName).matches()) {
                return true;
            }
        }

        return false;
    }

    class AllTypesScanner extends AbstractScanner {

        Set<String> types = new HashSet<>();

        @Override
        public void scan(Object cls) {
            String typeName = getMetadataAdapter().getClassName(cls);

            if (!isExcluded(typeName)) {
                types.add(typeName);
            }
        }
    }

}