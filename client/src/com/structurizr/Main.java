package com.structurizr;

import jdepend.framework.JDepend;
import jdepend.framework.JavaClass;
import jdepend.framework.JavaPackage;
import jdepend.framework.PackageFilter;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.scannotation.AnnotationDB;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main {

    private static Map<String, Component> components = new HashMap<>();

    public static void main(String[] args) throws Exception {
        String path1 = "/Users/simon/sandbox/structurizr/build/structurizr-core/production-classes/";
        String path2 = "/Users/simon/sandbox/structurizr/build/structurizr-web/production-classes/";

        AnnotationDB db = new AnnotationDB();
        db.scanArchives(new File(path1).toURI().toURL());
        db.scanArchives(new File(path2).toURI().toURL());
        Set<String> entityClasses = db.getAnnotationIndex().get(com.structurizr.element.Component.class.getName());

        PackageFilter filter = new PackageFilter();
        filter.addPackage("java.*");
        filter.addPackage("javax.*");
        filter.addPackage("org.springframework.*");
        filter.addPackage("org.apache.*");
        filter.addPackage("com.mongodb");
        filter.addPackage("twitter4j");
        filter.addPackage("twitter4j.*");
        filter.addPackage("com.sun.*");
        filter.addPackage("org.eclipse.*");

        filter.addPackage("com.structurizr.domain");

        JDepend jdepend = new JDepend(filter);
        jdepend.addDirectory(path1);
        jdepend.addDirectory(path2);
        jdepend.analyze();

        for (String entityClass : entityClasses) {
            Component component = new Component(entityClass);
            component.setResponsibility(db.getAnnotationValue(entityClass, "com.structurizr.element.Component", "responsibility"));
            components.put(component.getPackage(), component);
        }

        for (Component component : components.values()) {
            calculateEfferents(jdepend, component, jdepend.getPackage(component.getPackage()));
        }

        System.out.println();
        System.out.println("Components");
        System.out.println("----------");
        for (Component component : components.values()) {
            System.out.println(component.getName() + "(" + component.getResponsibility() + ")");
        }
        System.out.println("----------");

        System.out.println();
        System.out.println("Dependencies");
        System.out.println("------------");
        for (Component component : components.values()) {
            for (Component dependency : component.getDependencies()) {
                System.out.println(component.getName() + " -> " + dependency.getName());
            }
        }
        System.out.println("------------");
    }

    private static void calculateEfferents(JDepend jdepend, Component component, JavaPackage javaPackage) {
        Collection efferents = javaPackage.getEfferents();
        for (Object efferent : efferents) {
            JavaPackage efferentJavaPackage = (JavaPackage)efferent;
            Component destination = findComponent(efferentJavaPackage.getName());
            if (destination != null) {
                component.addDependency(destination);
            }
        }

        for (Object o : javaPackage.getClasses()) {
            JavaClass javaClass = (JavaClass)o;
            System.out.println("Name: " + javaClass.getName());
            JavaPackage superClassPackage = jdepend.getPackage(getPackageForClass(javaClass.getSuperClassName()));
            if (superClassPackage != null) {
                System.out.println("Superclass name: " + javaClass.getSuperClassName());
                JavaClass superClass = superClassPackage.getClass(javaClass.getSuperClassName());
                if (superClass != null) {
                    calculateEfferents(component, superClass);
                }
            }
        }
    }

    private static void calculateEfferents(Component component, JavaClass javaClass) {
        Collection efferents = javaClass.getImportedPackages();
        for (Object efferent : efferents) {
            JavaPackage efferentJavaPackage = (JavaPackage)efferent;
            Component destination = findComponent(efferentJavaPackage.getName());
            if (destination != null) {
                component.addDependency(destination);
            }
        }
    }

    private static Component findComponent(String fullyQualifiedClassName) {
        Component component = components.get(fullyQualifiedClassName);
        if (component == null) {
            System.out.println("Unrecognised package: " + fullyQualifiedClassName);
        }

        return component;
    }

    private static String getPackageForClass(String fullyQualifiedClassName) {
        return fullyQualifiedClassName.substring(0, fullyQualifiedClassName.lastIndexOf("."));
    }

}
