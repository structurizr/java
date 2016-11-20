package com.structurizr.example.core;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.*;
import com.structurizr.view.*;

/**
 * This is a model of the system context for the techtribes.je system.
 *
 * You can see the workspace online at https://structurizr.com/public/101
 */
public class TechTribesSystemContext {

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("techtribes.je", "This is a model of the system context for the techtribes.je system, for use in presentations.");
        Model model = workspace.getModel();

        // create a model and the software system we want to describe
        SoftwareSystem techTribes = model.addSoftwareSystem("techtribes.je", "techtribes.je is the only way to keep up to date with the IT, tech and digital sector in Jersey and Guernsey, Channel Islands");

        // create the various types of people (roles) that use the software system
        Person anonymousUser = model.addPerson("Anonymous User", "Anybody on the web.");
        anonymousUser.uses(techTribes, "View people, tribes (businesses, communities and interest groups), content, events, jobs, etc from the local tech, digital and IT sector.");

        Person authenticatedUser = model.addPerson("Aggregated User", "A user or business with content aggregated into the website.");
        authenticatedUser.uses(techTribes, "Manage user profile and tribe membership.");

        Person adminUser = model.addPerson("Administration User", "A system administration user.");
        adminUser.uses(techTribes, "Add people, add tribes and manage tribe membership.");

        // create the various software systems that techtribes.je has a dependency on
        SoftwareSystem twitter = model.addSoftwareSystem("Twitter", "twitter.com");
        techTribes.uses(twitter, "Gets profile information and tweets from.");

        SoftwareSystem gitHub = model.addSoftwareSystem("GitHub", "github.com");
        techTribes.uses(gitHub, "Gets information about public code repositories from.");

        SoftwareSystem blogs = model.addSoftwareSystem("Blogs", "RSS and Atom feeds");
        techTribes.uses(blogs, "Gets content using RSS and Atom feeds from.");

        // now create the system context view based upon the model
        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createSystemContextView(techTribes, "context", "The system context diagram for techtribes.je");
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        // tag and style some elements
        techTribes.addTags("techtribes.je");

        Styles styles = viewSet.getConfiguration().getStyles();
        styles.addElementStyle(Tags.ELEMENT).width(600).height(450).color("#ffffff").fontSize(40).shape(Shape.RoundedBox);
        styles.addElementStyle("techtribes.je").background("#041F37");
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#2A4E6E");
        styles.addElementStyle(Tags.PERSON).width(575).background("#728da5").shape(Shape.Person);
        styles.addRelationshipStyle(Tags.RELATIONSHIP).thickness(5).fontSize(40).width(500);
        contextView.setPaperSize(PaperSize.Slide_4_3);

        StructurizrClient structurizrClient = new StructurizrClient("key", "secret");
        structurizrClient.putWorkspace(101, workspace);
    }

}
