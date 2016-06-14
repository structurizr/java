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
                addEfferentDependencies(component, component.getType(), new HashSet<>());

                // and repeat for the first implementation class we can find
                ClassPool pool = ClassPool.getDefault();
                CtClass cc = pool.get(component.getType());
                if (cc.isInterface()) {
                    Class implementationType = getFirstImplementationOfInterface(cc.getName());
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

                Component destinationComponent = componentFinder.getContainer().getComponentOfType(referencedTypeName);
                if (destinationComponent != null) {
                    if (component != destinationComponent) {
                        component.uses(destinationComponent, "");
                    }
                } else if (!typesVisited.contains(referencedTypeName)) {
                    addEfferentDependencies(component, referencedTypeName, typesVisited);
                }
            }
        } catch (NotFoundException nfe) {
            System.err.println(nfe.getMessage() + " not found");
        } catch (Exception e) {
            System.err.println(e.getMessage());
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

    protected Class getFirstImplementationOfInterface(String interfaceTypeName) throws Exception {
        return getFirstImplementationOfInterface(Class.forName(interfaceTypeName));
    }

    protected Class getFirstImplementationOfInterface(Class interfaceType) throws Exception {
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
