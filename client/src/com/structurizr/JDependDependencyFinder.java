package com.structurizr;

import com.structurizr.model.Component;
import com.structurizr.model.Container;
import jdepend.framework.JDepend;
import jdepend.framework.JavaClass;
import jdepend.framework.JavaPackage;
import jdepend.framework.PackageFilter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JDependDependencyFinder implements DependencyFinder {

    public void findDependencies(Container container, Collection<String> pathsToScan, Collection<String> packagesToFilter) throws Exception {
        PackageFilter filter = new PackageFilter();
        packagesToFilter.forEach(filter::addPackage);

        JDepend jdepend = new JDepend(filter);
        for (String path : pathsToScan) {
            jdepend.addDirectory(path);
        }
        jdepend.analyze();

        Map<String,Component> componentsByPackage = new HashMap<>();
        for (Component component : container.getComponents()) {
            componentsByPackage.put(component.getPackage(), component);
        }

        for (Component component : container.getComponents()) {
            findEfferentDependencies(jdepend, componentsByPackage, component, jdepend.getPackage(component.getPackage()));
        }
    }

    private void findEfferentDependencies(JDepend jdepend, Map<String,Component> componentsByPackage, Component component, JavaPackage javaPackage) {
        Collection efferents = javaPackage.getEfferents();
        for (Object efferent : efferents) {
            JavaPackage efferentJavaPackage = (JavaPackage)efferent;
            Component destination = findComponent(componentsByPackage, efferentJavaPackage.getName());
            if (destination != null) {
                component.uses(destination, "");
            }
        }

        for (Object o : javaPackage.getClasses()) {
            JavaClass javaClass = (JavaClass)o;
            JavaPackage superClassPackage = jdepend.getPackage(getPackageForClass(javaClass.getSuperClassName()));
            if (superClassPackage != null) {
                JavaClass superClass = superClassPackage.getClass(javaClass.getSuperClassName());
                if (superClass != null) {
                    calculateEfferents(componentsByPackage, component, superClass);
                }
            }
        }
    }

    private void calculateEfferents(Map<String,Component> componentsByPackage, Component component, JavaClass javaClass) {
        Collection efferents = javaClass.getImportedPackages();
        for (Object efferent : efferents) {
            JavaPackage efferentJavaPackage = (JavaPackage)efferent;
            Component destination = findComponent(componentsByPackage, efferentJavaPackage.getName());
            if (destination != null) {
                component.uses(destination, "");
            }
        }
    }

    private Component findComponent(Map<String,Component> componentsByPackage, String fullyQualifiedClassName) {
        Component component = componentsByPackage.get(fullyQualifiedClassName);
        if (component == null) {
            System.out.println("Unrecognised package: " + fullyQualifiedClassName);
        }

        return component;
    }

    private String getPackageForClass(String fullyQualifiedClassName) {
        return fullyQualifiedClassName.substring(0, fullyQualifiedClassName.lastIndexOf("."));
    }

}