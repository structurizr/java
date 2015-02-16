package com.structurizr.example.core;

import com.structurizr.Workspace;
import com.structurizr.io.json.JsonWriter;
import com.structurizr.model.*;
import com.structurizr.view.*;

import java.io.StringWriter;

/**
 * This is a model of the system context for the techtribes.je system,
 * the code for which can be found at https://github.com/techtribesje/techtribesje
 */
public class TechTribesSystemContextForPresentation {

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("techtribes.je", "This is a model of the system context for the techtribes.je system, for use in presentations.");
        Model model = workspace.getModel();

        // create a model and the software system we want to describe
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

        // now create the system context view based upon the model
        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createContextView(techTribes);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        // tag and style some elements
        techTribes.addTags("techtribesje");
        viewSet.getStyles().add(new ElementStyle(Tags.ELEMENT, 600, 450, "#041F37", "white", 40));
        viewSet.getStyles().add(new ElementStyle("techtribesje", null, null, "#041F37", "white", null));
        viewSet.getStyles().add(new ElementStyle(Tags.SOFTWARE_SYSTEM, null, null, "#2A4E6E", "white", null));
        viewSet.getStyles().add(new ElementStyle(Tags.PERSON, null, null, "#728da5", "white", 40));
        viewSet.getStyles().add(new RelationshipStyle(Tags.RELATIONSHIP, 5, null, 40, 500));
        contextView.setPaperSize(PaperSize.Slide_4_3);

        // and output the model and view to JSON
        JsonWriter jsonWriter = new JsonWriter(true);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(workspace, stringWriter);
        System.out.println(stringWriter.toString());
    }

}
