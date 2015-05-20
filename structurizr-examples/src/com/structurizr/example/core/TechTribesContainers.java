package com.structurizr.example.core;

import com.structurizr.Workspace;
import com.structurizr.io.json.JsonWriter;
import com.structurizr.model.*;
import com.structurizr.view.ContainerView;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

import java.io.StringWriter;

/**
 * This is a simple (incomplete) example C4 model based upon the techtribes.je system,
 * the code for which can be found at https://github.com/techtribesje/techtribesje
 */
public class TechTribesContainers {

    public static void main(String[] args) throws Exception {
        // create a model and the software system we want to describe
        Workspace workspace = new Workspace("techtribes.je", "This is a model of the system context for the techtribes.je system, the code for which can be found at https://github.com/techtribesje/techtribesje");
        Model model = workspace.getModel();

        SoftwareSystem techTribes = model.addSoftwareSystem(Location.Internal, "techtribes.je", "techtribes.je is the only way to keep up to date with the IT, tech and digital sector in Jersey and Guernsey, Channel Islands");

        // create the various types of people (roles) that use the software system
        Person anonymousUser = model.addPerson(Location.External, "Anonymous User", "Anybody on the web.");
        anonymousUser.uses(techTribes, "View people, tribes (businesses, communities and interest groups), content, events, jobs, etc from the local tech, digital and IT sector.");

        Person authenticatedUser = model.addPerson(Location.External, "Aggregated User", "A user or business with content that is aggregated into the website.");
        authenticatedUser.uses(techTribes, "Manage user profile and tribe membership.");

        Person adminUser = model.addPerson(Location.External, "Administration User", "A system administration user.");
        adminUser.uses(techTribes, "Add people, add tribes and manage tribe membership.");

        // create the various software systems that techtribes.je has a dependency on
        SoftwareSystem twitter = model.addSoftwareSystem(Location.External, "Twitter", "twitter.com");
        techTribes.uses(twitter, "Gets profile information and tweets from.");

        SoftwareSystem gitHub = model.addSoftwareSystem(Location.External, "GitHub", "github.com");
        techTribes.uses(gitHub, "Gets information about public code repositories from.");

        SoftwareSystem blogs = model.addSoftwareSystem(Location.External, "Blogs", "RSS and Atom feeds");
        techTribes.uses(blogs, "Gets content using RSS and Atom feeds from.");

        // create the containers that techtribes.je is made up from
        Container webApplication = techTribes.addContainer("Web Application", "Allows users to view people, tribes, content, events, jobs, etc from the local tech, digital and IT sector.", "Apache Tomcat 7.x");
        Container contentUpdater = techTribes.addContainer("Content Updater", "Updates profiles, tweets, GitHub repos and content on a scheduled basis.", "Standalone Java 7 application");
        Container relationalDatabase = techTribes.addContainer("Relational Database", "Stores people, tribes, tribe membership, talks, events, jobs, badges, GitHub repos, etc.", "MySQL 5.5.x");
        Container noSqlStore = techTribes.addContainer("NoSQL Data Store", "Stores content from RSS/Atom feeds (blog posts) and tweets.", "MongoDB 2.2.x");
        Container fileSystem = techTribes.addContainer("File System", "Stores search indexes.", null);

        anonymousUser.uses(webApplication, "View people, tribes (businesses, communities and interest groups), content, events, jobs, etc from the local tech, digital and IT sector.");
        authenticatedUser.uses(webApplication, "Manage user profile and tribe membership.");
        adminUser.uses(webApplication, "Add people, add tribes and manage tribe membership.");

        webApplication.uses(relationalDatabase, "Reads from and writes data to");
        webApplication.uses(noSqlStore, "Reads from");
        webApplication.uses(fileSystem, "Reads from");

        contentUpdater.uses(relationalDatabase, "Reads from and writes data to");
        contentUpdater.uses(noSqlStore, "Reads from and writes data to");
        contentUpdater.uses(fileSystem, "Writes to");
        contentUpdater.uses(twitter, "Gets profile information and recent tweets using the REST API from.", "JSON over HTTPS");
        contentUpdater.uses(twitter, "Subscribes to tweets using the Twitter Streaming API from.", "JSON over HTTPS");
        contentUpdater.uses(gitHub, "Gets information about public code repositories using the GitHub API from.", "JSON over HTTPS");
        contentUpdater.uses(blogs, "Gets blog posts and news from.", "RSS and Atom over HTTP");

        // now create the system context view based upon the model
        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createContextView(techTribes);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        // and the container view
        ContainerView containerView = viewSet.createContainerView(techTribes);
        containerView.addAllSoftwareSystems();
        containerView.addAllPeople();
        containerView.addAllContainers();

        // and output the model and view to JSON
        JsonWriter jsonWriter = new JsonWriter(true);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(workspace, stringWriter);
        System.out.println(stringWriter.toString());
    }

}
