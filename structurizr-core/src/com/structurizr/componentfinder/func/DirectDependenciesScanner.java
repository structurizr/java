package com.structurizr.componentfinder.func;

import com.structurizr.model.Component;
import javassist.ClassPool;
import javassist.CtClass;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

enum DirectDependenciesScanner implements DependenciesScanner {
    INSTANCE;

    public void addDependencies(ScanResult scanResult) {
        final Processor processor = new Processor(scanResult);
        scanResult.stream()
                .filter(hasType())
                .forEach(processor::addDirectDependencies);
    }

    private Predicate<Component> hasType() {
        return component -> component.getType() != null;
    }


    private class Processor {
        final Set<String> typesVisited = new HashSet<>();
        final ClassPool pool = ClassPool.getDefault();
        final ScanResult scanResult;

        private Processor(ScanResult scanResult) {
            this.scanResult = scanResult;
        }


        private void addDirectDependencies(Component component) {
            final String type = component.getType();
            System.out.println("ComponentType " + type);
            typesVisited.add(type);

            try {
                final CtClass currentClass = pool.getOrNull(type);
                if (currentClass == null) return;
                for (Object referencedType : currentClass.getRefClasses()) {
                    addRelation(component, currentClass, (String) referencedType);
                }
            } catch (Exception nfe) {
                throw new RuntimeException(nfe);
            }
        }

        private void addRelation(Component component, CtClass currentClass, String referencedTypeName) throws Exception {
            final CtClass referencedTypeAsClass = pool.get(referencedTypeName);
            if (!isImplementation(currentClass, referencedTypeAsClass)) {
                final Optional<Component> destinationComponent = findMatchingComponentReference(referencedTypeAsClass);
                destinationComponent.ifPresent(useRelation(component, currentClass, referencedTypeAsClass));
            }
        }

        private Consumer<? super Component> useRelation(Component component, CtClass currentClass, CtClass referencedTypeAsClass) {
            return (Consumer<Component>) destinationComponent -> {
                if (component != destinationComponent) {
                    component.uses(destinationComponent, getRelationDescription(currentClass, referencedTypeAsClass));
                }
            };
        }


        //TODO make injectable
        private String getRelationDescription(CtClass source, CtClass target) {
            if (isImplementation(target, source))
                return "IMPLEMENTS";
            else
                return "USES";
        }

        private Optional<Component> findMatchingComponentReference(final CtClass type) throws Exception {
            final String name = type.getName();
            final Optional<Component> destinationComponent = scanResult.getFirstComponentOfType(name);
            // if there was no component of the interface type, perhaps there is one of the implementation type
            if (destinationComponent.isPresent())
                return destinationComponent;
            else
                return getFirstImplementation(type, name);
        }

        private Optional<Component> getFirstImplementation(CtClass type, String name) {
            if (type.isInterface()) {
                final Optional<Class> implementationClass = scanResult.getFirstImplementationOfInterface(name);
                if (implementationClass.isPresent()) {
                    return scanResult.getFirstComponentOfType(implementationClass.get().getCanonicalName());
                }
            }
            return Optional.empty();
        }


        private boolean isImplementation(CtClass possibleInterface, CtClass possibleImplementation) {
            final List<CtClass> implInterfaces = getImplInterfaces(possibleImplementation);
            return possibleInterface.isInterface() && implInterfaces.contains(possibleInterface);
        }

        private List<CtClass> getImplInterfaces(CtClass possibleImplementation) {
            try {
                return Arrays.asList(possibleImplementation.getInterfaces());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}
