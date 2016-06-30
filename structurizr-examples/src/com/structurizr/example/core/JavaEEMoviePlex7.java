package com.structurizr.example.core;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.*;
import com.structurizr.view.*;

/**
 * A software architecture model to describe the Java EE Hands on Lab Movie Plex 7 sample application. The goal is to
 * create a better illustration of the software architecture than this diagram:
 *  - https://github.com/javaee-samples/javaee7-hol/blob/master/docs/images/2.0-problem-statement.png
 *
 * For more information, see:
 *  - https://github.com/javaee-samples/javaee7-hol
 *
 *  And the live software architecture diagrams are hosted here:
 *  - https://www.structurizr.com/public/511
 */
public class JavaEEMoviePlex7 {

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Java EE Hands on Lab - Movie Plex 7", "A cohesive example application using the a number of Java EE 7 technologies.");
        Model model = workspace.getModel();

        // create the model
        SoftwareSystem moviePlex = model.addSoftwareSystem("Movie Plex 7", "Allows customers to view the show timings for a movie in a 7-theater Cineplex and make reservations.");
        Person customer = model.addPerson("Customer", "A customer of the cinema.");
        customer.uses(moviePlex, "View show timings, make reservations and chat using");

        Container webBrowser = moviePlex.addContainer("Web Browser", "The point of access for all Movie Plex functionality.", null);
        Container webApplication = moviePlex.addContainer("Web Application", "The main Movie Plex web application, providing all functionality.", "GlassFish 4.1");
        Container database = moviePlex.addContainer("Database", "Data store for all Movie Plex data", "Derby");
        Container messageBus = moviePlex.addContainer("Message Bus", "Used to communicate movie point accruals.", "GlassFish");
        Container fileSystem = moviePlex.addContainer("File System", "Stores sales information for batch processing.", null);

        customer.uses(webBrowser, "View show timings, make reservations and chat using");
        webBrowser.uses(webApplication, "Uses [HTTP and WebSockets]");
        webApplication.uses(database, "Reads from and writes to [JDBC]");
        webApplication.uses(messageBus, "Movie points accrual messages [JMS]");
        messageBus.uses(webApplication, "Movie points accrual messages [JMS]");
        webApplication.uses(fileSystem, "Reads from");

        // TODO: the goal is to have all components and their dependencies defined automatically by extracting them from
        // the codebase using something like a JavaEEComponentFinderStrategy (which hasn't been built yet)

        // booking
        Component bookingComponent = webApplication.addComponent("Booking", "Allows customers to book movie showings", "EJB");
        bookingComponent.setSourcePath("https://github.com/javaee-samples/javaee7-hol/blob/master/solution/movieplex7/src/main/java/org/javaee7/movieplex7/booking/Booking.java");
        Component bookingUI = webApplication.addComponent("Booking UI", "Allows customers to book movie showings", "JavaServer Faces");
        bookingUI.setSourcePath("https://github.com/javaee-samples/javaee7-hol/tree/master/solution/movieplex7/src/main/webapp/booking");
        webBrowser.uses(bookingUI, "uses");
        bookingUI.uses(bookingComponent, "uses");
        bookingComponent.uses(database, "Reads to and writes from");

        // movies
        Component moviesComponent = webApplication.addComponent("Movies", "CRUD operations for movies", "RESTful EJB");
        moviesComponent.setSourcePath("https://github.com/javaee-samples/javaee7-hol/blob/master/solution/movieplex7/src/main/java/org/javaee7/movieplex7/rest/MovieFacadeREST.java");
        bookingUI.uses(moviesComponent, "uses");
        moviesComponent.uses(database, "uses");

        Component movieManagementComponent = webApplication.addComponent("Movie Management", "Allows movies to be added and removed", "Java API for RESTful Web Services");
        movieManagementComponent.setSourcePath("https://github.com/javaee-samples/javaee7-hol/blob/master/solution/movieplex7/src/main/java/org/javaee7/movieplex7/client/MovieClientBean.java");
        Component movieManagementComponentUI = webApplication.addComponent("Movie Management UI", "Allows movies to be added and removed", "JavaServer Faces");
        movieManagementComponentUI.setSourcePath("https://github.com/javaee-samples/javaee7-hol/tree/master/solution/movieplex7/src/main/webapp/client");
        webBrowser.uses(movieManagementComponentUI, "uses");
        movieManagementComponentUI.uses(movieManagementComponent, "uses");
        movieManagementComponent.uses(moviesComponent, "uses [HTTP]");

        // time slot
        Component timeSlotComponent = webApplication.addComponent("Time Slot", "Finds movie time slots", "RESTful EJB");
        timeSlotComponent.setSourcePath("https://github.com/javaee-samples/javaee7-hol/blob/master/solution/movieplex7/src/main/java/org/javaee7/movieplex7/rest/TimeslotFacadeREST.java");
        bookingUI.uses(timeSlotComponent, "uses");
        timeSlotComponent.uses(database, "uses");

        // movie points
        Component moviePointsComponent = webApplication.addComponent("Movie Points", "Sends and receives movie point accrual messages");
        moviePointsComponent.setSourcePath("https://github.com/javaee-samples/javaee7-hol/tree/master/solution/movieplex7/src/main/java/org/javaee7/movieplex7/points");
        moviePointsComponent.uses(messageBus, "Movie points accrual messages [JMS]");
        messageBus.uses(moviePointsComponent, "Movie points accrual messages [JMS]");
        Component moviePointsUI = webApplication.addComponent("Movie Points UI", "Allows customers to send/receive points accrual messages", "JavaServer Faces");
        moviePointsUI.setSourcePath("https://github.com/javaee-samples/javaee7-hol/blob/master/solution/movieplex7/src/main/webapp/points/points.xhtml");
        webBrowser.uses(moviePointsUI, "uses");
        moviePointsUI.uses(moviePointsComponent, "uses");

        // chat server
        Component chatServerComponent = webApplication.addComponent("ChatServer", "Allows customers to chat about movies", "Java API for WebSocket");
        chatServerComponent.setSourcePath("https://github.com/javaee-samples/javaee7-hol/blob/master/solution/movieplex7/src/main/java/org/javaee7/movieplex7/chat/ChatServer.java");
        webBrowser.uses(chatServerComponent, "Sends/receives messages using [WebSocket]");

        // ticket sales batch
        Component ticketSalesBatchComponent = webApplication.addComponent("Ticket Sales", "", "Java API for Batch Processing");
        ticketSalesBatchComponent.setSourcePath("https://github.com/javaee-samples/javaee7-hol/tree/master/solution/movieplex7/src/main/java/org/javaee7/movieplex7/batch");
        Component ticketSalesUI = webApplication.addComponent("Ticket Sales UI", "Used to trigger to ticket sales batch process", "JavaServer Faces");
        ticketSalesUI.setSourcePath("https://github.com/javaee-samples/javaee7-hol/blob/master/solution/movieplex7/src/main/webapp/batch/sales.xhtml");
        webBrowser.uses(ticketSalesUI, "uses");
        ticketSalesUI.uses(ticketSalesBatchComponent, "initiates");
        ticketSalesBatchComponent.uses(database, "uses");
        ticketSalesBatchComponent.uses(fileSystem, "Reads from");

        // create some views
        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createSystemContextView(moviePlex, "context", null);
        contextView.setPaperSize(PaperSize.A4_Landscape);
        contextView.addAllElements();

        ContainerView containerView = viewSet.createContainerView(moviePlex, "containers", null);
        contextView.setPaperSize(PaperSize.A4_Landscape);
        containerView.addAllElements();

        ComponentView componentView = viewSet.createComponentView(webApplication, "components", null);
        componentView.setPaperSize(PaperSize.A3_Landscape);
        componentView.addAllElements();

        // tag and style some elements
        moviePlex.addTags("System Under Construction");
        viewSet.getConfiguration().getStyles().add(new ElementStyle(Tags.ELEMENT, 600, 450, null, null, 40));
        viewSet.getConfiguration().getStyles().add(new ElementStyle("System Under Construction", null, null, "#041F37", "#ffffff", null));
        viewSet.getConfiguration().getStyles().add(new ElementStyle(Tags.SOFTWARE_SYSTEM, null, null, "#2A4E6E", "#ffffff", null));
        viewSet.getConfiguration().getStyles().add(new ElementStyle(Tags.PERSON, null, null, "#728da5", "white", 40));
        viewSet.getConfiguration().getStyles().add(new ElementStyle(Tags.CONTAINER, null, null, "#041F37", "#ffffff", null));
        viewSet.getConfiguration().getStyles().add(new RelationshipStyle(Tags.RELATIONSHIP, 5, null, null, null, 40, 500, null));

        // upload it to structurizr.com
        StructurizrClient structurizrClient = new StructurizrClient("key", "secret");
        structurizrClient.mergeWorkspace(511, workspace);
   }

}