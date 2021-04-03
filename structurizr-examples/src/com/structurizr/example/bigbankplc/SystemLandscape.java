package com.structurizr.example.bigbankplc;

import com.structurizr.api.StructurizrClient;
import com.structurizr.model.*;
import com.structurizr.view.*;

/**
 * This is an example workspace to illustrate the key features of Structurizr,
 * based around a fictional bank. This workspace contains the system landscape.
 *
 * The live workspace is available to view at https://structurizr.com/share/28201
 */
public final class SystemLandscape extends BigBankPlc {

    private static final long WORKSPACE_ID = 28201;
    private static final String API_KEY = "key";
    private static final String API_SECRET = "secret";

    private SystemLandscape() throws Exception {
        workspace.setName("Big Bank plc - System Landscape");
        workspace.setDescription("The system landscape for Big Bank plc.");

        internetBankingSystem.setUrl("https://structurizr.com/share/36141/diagrams#SystemContext");

        SystemLandscapeView systemLandscapeView = views.createSystemLandscapeView("SystemLandscape", "The system landscape diagram for Big Bank plc.");
        systemLandscapeView.addAllElements();

        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#999999").color("#ffffff");

        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

    public static void main(String[] args) throws Exception {
        new SystemLandscape();
    }

}