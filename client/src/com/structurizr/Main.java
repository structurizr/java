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
        String path1 = "/Users/simon/sandbox/techtribesje/build/techtribes-core/production-classes/";
        String path2 = "/Users/simon/sandbox/techtribesje/build/techtribes-updater/production-classes/";

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

        filter.addPackage("je.techtribes.domain");
        filter.addPackage("je.techtribes.util");

        JDepend jdepend = new JDepend(filter);
        jdepend.addDirectory(path1);
        jdepend.addDirectory(path2);

        for (String entityClass : entityClasses) {
            Component component = new Component(entityClass);
            component.setResponsibility(db.getAnnotationValue(entityClass, "com.structurizr.element.Component", "responsibility"));
            components.put(component.getPackage(), component);
            jdepend.addPackage(component.getPackage());
        }

        jdepend.analyze();

        Collection javaPackages = jdepend.getPackages();
        for (Object javaPackage : javaPackages) {
            JavaPackage jp = (JavaPackage)javaPackage;

            Collection efferents = jp.getEfferents();
            for (Object efferent : efferents) {
                JavaPackage efferentJavaPackage = (JavaPackage)efferent;
                System.out.println(findComponent(jp.getName()) + " --> " + findComponent(efferentJavaPackage.getName()));
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

}
