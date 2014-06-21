package com.structurizr;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.structurizr.domain.*;
import jdepend.framework.JDepend;
import jdepend.framework.JavaClass;
import jdepend.framework.JavaPackage;
import jdepend.framework.PackageFilter;
import org.scannotation.AnnotationDB;

import java.io.File;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Model model = new Model();

        SoftwareSystem techTribes = model.createSoftwareSystem("techtribes.je", "techtribes.je is the only way to keep up to date with the IT, tech and digital sector in Jersey and Guernsey, Channel Islands");

        Person anonymousUser = model.createPerson("Anonymous User", "Anybody on the web.");
        Person authenticatedUser = model.createPerson("Authenticated User", "A user or business who's content is aggregated into the website.");
        Person adminUser = model.createPerson("Admin User", "A system administration user.");

        SoftwareSystem twitter = model.createSoftwareSystem("Twitter", null);
        techTribes.uses(twitter, "Gets profile information and tweets from.");

        SoftwareSystem gitHub = model.createSoftwareSystem("GitHub", null);
        techTribes.uses(gitHub, "Gets information about public code repositories from.");

        SoftwareSystem blogs = model.createSoftwareSystem("Blogs", null);
        techTribes.uses(blogs, "Gets content using RSS and Atom feeds from.");

        Container webApplication = techTribes.createContainer("Web Application", "Allows users to view people, tribes, content, events, jobs, etc from the local tech, digital and IT sector.", "Apache Tomcat 7.x");
        Container contentUpdater = techTribes.createContainer("Content Updater", "Updates profiles, tweets, GitHub repos and content on a scheduled basis.", "Standalone Java 7 application");
        Container relationalDatabase = techTribes.createContainer("Relational Database", "Stores people, tribes, tribe membership, talks, events, jobs, badges, GitHub repos, etc.", "MySQL 5.5.x");
        Container noSqlStore = techTribes.createContainer("NoSQL Data Store", "Stores content from RSS/Atom feeds (blog posts) and tweets.", "MongoDB 2.2.x");
        Container fileSystem = techTribes.createContainer("File System", "Stores search indexes.", null);

        anonymousUser.uses(webApplication, "View people, tribes (businesses, communities and interest groups), content, events, jobs, etc from the local tech, digital and IT sector.");
        authenticatedUser.uses(webApplication, "Manage user profile and tribe membership.");
        adminUser.uses(webApplication, "Add people, add tribes and manage tribe membership.");

        webApplication.uses(relationalDatabase, "Reads from and writes data to");
        webApplication.uses(noSqlStore, "Reads from");
        webApplication.uses(fileSystem, "Reads from");

        contentUpdater.uses(relationalDatabase, "Reads from and writes data to");
        contentUpdater.uses(noSqlStore, "Reads from and writes data to");
        contentUpdater.uses(fileSystem, "Writes to");
        contentUpdater.uses(twitter, "Gets profile information and tweets from.");
        contentUpdater.uses(gitHub, "Gets information about public code repositories from.");
        contentUpdater.uses(blogs, "Gets content using RSS and Atom feeds from.");

        Collection<String> paths = new ArrayList<>();
        paths.add("/Users/simon/sandbox/techtribesje/build/techtribes-core/production-classes/");
        paths.add("/Users/simon/sandbox/techtribesje/build/techtribes-updater/production-classes/");

        Collection<Component> components = findComponents(contentUpdater, paths);
        analyseDependencies(components, paths);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        System.out.println(objectMapper.writeValueAsString(model));
    }

    private static Collection<Component> findComponents(Container container, Collection<String> paths) throws Exception {
        AnnotationDB db = new AnnotationDB();
        for (String path : paths) {
            db.scanArchives(new File(path).toURI().toURL());
        }
        Set<String> entityClasses = db.getAnnotationIndex().get(com.structurizr.element.Component.class.getName());

        Collection<Component> components = new ArrayList<>();
        for (String entityClass : entityClasses) {
            Component component = container.createComponentWithClass(entityClass, trim(db.getAnnotationValue(entityClass, "com.structurizr.element.Component", "responsibility")));
            components.add(component);
        }

        return components;
    }

    private static String trim(String string) {
        if (string != null) {
            return string.replaceAll("\\\"", "");
        }

        return "";
    }

    private static void analyseDependencies(Collection<Component> components, Collection<String> paths) throws Exception {
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

        JDepend jdepend = new JDepend(filter);
        for (String path : paths) {
            jdepend.addDirectory(path);
        }
        jdepend.analyze();

        Map<String,Component> componentsByPackage = new HashMap<>();
        for (Component component : components) {
            componentsByPackage.put(component.getPackage(), component);
        }

        for (Component component : components) {
            findEfferentDependencies(jdepend, componentsByPackage, component, jdepend.getPackage(component.getPackage()));
        }
    }

    private static void findEfferentDependencies(JDepend jdepend, Map<String,Component> componentsByPackage, Component component, JavaPackage javaPackage) {
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

    private static void calculateEfferents(Map<String,Component> componentsByPackage, Component component, JavaClass javaClass) {
        Collection efferents = javaClass.getImportedPackages();
        for (Object efferent : efferents) {
            JavaPackage efferentJavaPackage = (JavaPackage)efferent;
            Component destination = findComponent(componentsByPackage, efferentJavaPackage.getName());
            if (destination != null) {
                component.uses(destination, "");
            }
        }
    }

    private static Component findComponent(Map<String,Component> componentsByPackage, String fullyQualifiedClassName) {
        Component component = componentsByPackage.get(fullyQualifiedClassName);
        if (component == null) {
            System.out.println("Unrecognised package: " + fullyQualifiedClassName);
        }

        return component;
    }

    private static String getPackageForClass(String fullyQualifiedClassName) {
        return fullyQualifiedClassName.substring(0, fullyQualifiedClassName.lastIndexOf("."));
    }

}
