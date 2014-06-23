package com.structurizr;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.structurizr.model.*;
import com.structurizr.view.ComponentView;
import com.structurizr.view.ContainerView;
import com.structurizr.view.ContextView;

public class Main {

    public static void main(String[] args) throws Exception {
        Model model = new Model();
        SoftwareSystem techTribes = model.createSoftwareSystem(Location.Internal, "techtribes.je", "techtribes.je is the only way to keep up to date with the IT, tech and digital sector in Jersey and Guernsey, Channel Islands");

        Person anonymousUser = model.createPerson(Location.External, "Anonymous User", "Anybody on the web.");
        Person authenticatedUser = model.createPerson(Location.External, "Aggregated User", "A user or business with content that is aggregated into the website.");
        Person adminUser = model.createPerson(Location.External, "Administration User", "A system administration user.");
        anonymousUser.uses(techTribes, "View people, tribes (businesses, communities and interest groups), content, events, jobs, etc from the local tech, digital and IT sector.");
        authenticatedUser.uses(techTribes, "Manage user profile and tribe membership.");
        adminUser.uses(techTribes, "Add people, add tribes and manage tribe membership.");

        SoftwareSystem twitter = model.createSoftwareSystem(Location.External, "Twitter", "twitter.com");
        techTribes.uses(twitter, "Gets profile information and tweets from.");

        SoftwareSystem gitHub = model.createSoftwareSystem(Location.External, "GitHub", "github.com");
        techTribes.uses(gitHub, "Gets information about public code repositories from.");

        SoftwareSystem blogs = model.createSoftwareSystem(Location.External, "Blogs", "RSS and Atom feeds");
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

        ComponentFinder componentFinder = new ComponentFinder(contentUpdater, "je.techtribes");
        componentFinder.findComponents();
        componentFinder.findComponentDependencies();
        componentFinder.findSoftwareSystemDependencies();
        componentFinder.findContainerDependencies();

        System.out.println("Context view");
        System.out.println("============");
        ContextView contextView = model.createContextView(techTribes);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();
        contextView.getElements().forEach(System.out::println);
        contextView.getRelationships().forEach(System.out::println);
        System.out.println("============");

        System.out.println();

        System.out.println("Container view");
        System.out.println("============");
        ContainerView containerView = model.createContainerView(techTribes);
        containerView.addAllSoftwareSystems();
        containerView.addAllPeople();
        containerView.addAllContainers();
        containerView.getElements().forEach(System.out::println);
        containerView.getRelationships().forEach(System.out::println);
        System.out.println("============");

        System.out.println();

        System.out.println("Component view - content updater");
        System.out.println("============");
        ComponentView componentView = model.createComponentView(techTribes, contentUpdater);
        componentView.add(twitter);
        componentView.add(gitHub);
        componentView.add(blogs);
        componentView.addAllContainers();
        componentView.remove(webApplication);
        componentView.addAllComponents();
        componentView.remove(contentUpdater.getComponentWithName("LoggingComponent"));
        componentView.remove(contentUpdater.getComponentWithName("ContentSourceComponent"));
        componentView.remove(contentUpdater.getComponentWithName("ActivityComponent"));
        componentView.removeElementsWithNoRelationships();
        componentView.getElements().forEach(System.out::println);
        componentView.getRelationships().forEach(System.out::println);
        System.out.println("============");

        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        System.out.println(objectMapper.writeValueAsString(model));
    }

}
