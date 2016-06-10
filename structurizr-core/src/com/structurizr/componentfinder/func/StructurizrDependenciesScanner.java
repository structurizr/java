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
                final Class<?> type = getClass(component.getType());
                if (type.isInterface()) {
                    Optional<Class> implementationType = scanResult.getFirstImplementationOfInterface(component.getType());
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

    private Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void addEfferentDependencies(Component component, ScanResult scanResult, String type, Set<String> typesVisited) {
        typesVisited.add(type);

        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass cc = pool.get(type);
            for (Object referencedType : cc.getRefClasses()) {
                String referencedTypeName = (String) referencedType;
                Optional<Component> destinationComponent = scanResult.getFirstComponentOfType(referencedTypeName);

                // if there was no component of the interface type, perhaps there is one of the implementation type
                CtClass referencedTypeAsClass = pool.get(referencedTypeName);
                if (!destinationComponent.isPresent() && referencedTypeAsClass.isInterface()) {
                    Optional<Class> implementationClass = scanResult.getFirstImplementationOfInterface(referencedTypeName);
                    if (implementationClass.isPresent()) {
                        destinationComponent = scanResult.getFirstComponentOfType(implementationClass.get().getCanonicalName());
                    }
                }

                if (destinationComponent.isPresent()) {
                    if (component != destinationComponent.get()) {
                        component.uses(destinationComponent.get(), "");
                    }
                } else if (!typesVisited.contains(referencedTypeName)) {
                    addEfferentDependencies(component, scanResult, referencedTypeName, typesVisited);
                }
            }
        } catch (NotFoundException nfe) {
            System.err.println(nfe.getMessage() + " not found");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
