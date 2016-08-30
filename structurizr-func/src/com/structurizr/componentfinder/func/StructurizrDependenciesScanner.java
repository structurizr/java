package com.structurizr.componentfinder.func;

import com.structurizr.model.Component;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Copy off how dependencies are calculated in AbstractReflectionsComponentFinderStrategy,
 * modified a bit to use scan result
 * <p>
 * This algorithm gives Interfaces:
 * <ol>
 * <li>a dependency on their implementation</li>
 * <li>a dependency on all the dependencies of the first implementation</li>
 * </ol>
 * <p>
 * I don't understand why  :)
 */
enum StructurizrDependenciesScanner implements DependenciesScanner {
    INSTANCE;

    @Override
    public void addDependencies(ScanResult scanResult) {

        for (Component component : scanResult) {
            if (component.getType() != null) {
                addEfferentDependencies(component, scanResult, component.getType(), new HashSet<>());

                // and repeat for the first implementation class we can find
                final CtClass cc = getCtClass(component.getType());
                if (cc.isInterface()) {
                    final Optional<Class> implementationType = scanResult.getFirstImplementationOfInterface(cc.getName());
                    if (implementationType.isPresent()) {
                        final String canonicalName = implementationType.get().getCanonicalName();
                        if (canonicalName != null) {
                            addEfferentDependencies(component, scanResult, canonicalName, new HashSet<>());
                        }
                    }
                }
            }
        }
    }


    private void addEfferentDependencies(Component component, ScanResult scanResult, String type, Set<String> typesVisited) {
        typesVisited.add(type);
        for (String referencedTypeName : getReferencedTypes(type)) {
            final Optional<Component> destinationComponent = scanResult.getFirstComponentOfType(referencedTypeName);
            if (destinationComponent.isPresent()) {
                if (component != destinationComponent.get()) {
                    component.uses(destinationComponent.get(), "");
                }
            } else if (!typesVisited.contains(referencedTypeName)) {
                addEfferentDependencies(component, scanResult, referencedTypeName, typesVisited);
            }
        }


    }

    private CtClass getCtClass(String type) {
        try {
            ClassPool pool = ClassPool.getDefault();
            return pool.get(type);
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    private Set<String> getReferencedTypes(String type) {
        final Set<String> referencedTypeNames = new HashSet<>();
        final CtClass cc = getCtClass(type);
        for (Object referencedType : cc.getRefClasses()) {
            String referencedTypeName = (String) referencedType;

            if (!isAJavaPlatformType(referencedTypeName)) {
                referencedTypeNames.add(referencedTypeName);
            }
        }

        return referencedTypeNames;
    }

    private boolean isAJavaPlatformType(String typeName) {
        return typeName.startsWith("java.") ||
                typeName.startsWith("javax.") ||
                typeName.startsWith("sun.");
    }


}
