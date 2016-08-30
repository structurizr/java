package com.structurizr.componentfinder.func;

import com.structurizr.model.Component;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

enum DirectDependenciesScanner implements DependenciesScanner {
    INSTANCE;
    private static final Log LOGGER = LogFactory.getLog(DirectDependenciesScanner.class);

    @Override
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
            registerVisitedType(type);
            final CtClass currentClass = pool.getOrNull(type);
            if (currentClass != null)
                createDependencies(component, currentClass);

        }

        private void createDependencies(Component component, CtClass currentClass) {
            final RelationBuilder relationBuilder = buildRelationBuilder(component, currentClass);
            for (Object referencedType : currentClass.getRefClasses()) {
                final Optional<CtClass> referencedClass = getCtClass((String) referencedType);
                referencedClass.ifPresent(relationBuilder::buildRelation);
            }
        }

        private void registerVisitedType(String type) {
            typesVisited.add(type);
        }

        private RelationBuilder buildRelationBuilder(Component component, CtClass currentClass) {
            return new RelationBuilder(scanResult, currentClass, component);
        }


        private Optional<CtClass> getCtClass(String referencedTypeName) {
            try {
                return Optional.of(pool.get(referencedTypeName));
            } catch (NotFoundException e) {
                LOGGER.warn("Referenced class " + referencedTypeName + " was not present on classpath");
                return Optional.empty();
            }
        }


    }

    private class RelationBuilder {
        private final ScanResult scanResult;
        private final CtClass currentClass;
        private final Component component;

        public RelationBuilder(ScanResult scanResult, CtClass currentClass, Component component) {
            this.scanResult = scanResult;
            this.currentClass = currentClass;
            this.component = component;
        }


        private void buildRelation(CtClass referencedTypeAsClass) {
            if (!isImplementation(currentClass, referencedTypeAsClass)) {
                final Optional<Component> destinationComponent = findMatchingComponentReference(referencedTypeAsClass);
                destinationComponent.ifPresent(useRelation(component, currentClass, referencedTypeAsClass));
            }
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

        private Optional<Component> findMatchingComponentReference(final CtClass type) {
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


    }
}
