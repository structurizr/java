package com.structurizr.componentfinder;

import com.google.common.base.Predicates;
import com.structurizr.model.Component;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractReflectionsComponentFinderStrategy extends ComponentFinderStrategy {

    protected Reflections reflections;

    public AbstractReflectionsComponentFinderStrategy() {
    }

    @Override
    public void setComponentFinder(ComponentFinder componentFinder) {
        super.setComponentFinder(componentFinder);

        this.reflections = new Reflections(new ConfigurationBuilder()
                  .filterInputsBy(new FilterBuilder().includePackage(componentFinder.getPackageToScan()))
                  .setUrls(ClasspathHelper.forPackage(componentFinder.getPackageToScan()))
                  .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(false), new FieldAnnotationsScanner()));
    }

    @Override
    public void findDependencies() throws Exception {
        for (Component component : componentFinder.getContainer().getComponents()) {
            if (component.getType() != null) {
                Class type = Class.forName(component.getType());
                addEfferentDependencies(component, component.getType(), new HashSet<>());

                // and repeat for the first implementation class we can find
                if (type.isInterface()) {
                    Class implementationType = getFirstImplementationOfInterface(type);
                    if (implementationType != null && implementationType.getCanonicalName() != null) {
                        addEfferentDependencies(component, implementationType.getCanonicalName(), new HashSet<>());
                    }
                }
            }
        }
    }

    private void addEfferentDependencies(Component component, String type, Set<String> typesVisited) {
        typesVisited.add(type);

        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass cc = pool.get(type);
            for (Object referencedType : cc.getRefClasses()) {
                String referencedTypeName = (String)referencedType;
                if (referencedTypeName.startsWith(componentFinder.getPackageToScan())) {
                    Component destinationComponent = componentFinder.getContainer().getComponentOfType(referencedTypeName);

                    // if there was no component of the interface type, perhaps there is one of the implementation type
                    Class referencedTypeAsClass = Class.forName(referencedTypeName);
                    if (destinationComponent == null && referencedTypeAsClass.isInterface()) {
                        Class implementationClass = getFirstImplementationOfInterface(referencedTypeAsClass);
                        if (implementationClass != null) {
                            destinationComponent = componentFinder.getContainer().getComponentOfType(implementationClass.getCanonicalName());
                        }
                    }
                    if (destinationComponent != null) {
                        if (component != destinationComponent) {
                            component.uses(destinationComponent, "");
                        }
                    } else if (!typesVisited.contains(referencedTypeName)) {
                        addEfferentDependencies(component, referencedTypeName, typesVisited);
                    }
                }
            }
        } catch (NotFoundException nfe) {
            System.err.println(type + " not found");
            nfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation);
    }

    protected Set<Class<?>> getAllTypes() {
        return reflections.getSubTypesOf(Object.class);
    }

    protected Set<Class> getInterfacesThatExtend(Class interfaceType) {
        return reflections.getSubTypesOf(interfaceType);
    }

    protected Class getFirstImplementationOfInterface(Class interfaceType) {
        Set<Class> implementationClasses = reflections.getSubTypesOf(interfaceType);

        if (implementationClasses.isEmpty()) {
            return null;
        } else {
            return implementationClasses.iterator().next();
        }
    }

//    protected Set<Field> getFieldsAnnotatedWith(Class<? extends Annotation> annotation) {
//        return reflections.getFieldsAnnotatedWith(annotation);
//    }

    protected Set<Class<?>> findSuperTypesAnnotatedWith(Class<?> implementationType, Class annotation) {
        return ReflectionUtils.getAllSuperTypes(implementationType, Predicates.and(ReflectionUtils.withAnnotation(annotation)));
    }

}
