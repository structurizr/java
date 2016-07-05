package com.structurizr.componentfinder;

import com.structurizr.Workspace;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;

public class TestConstants {

    public static final String MY_APP_TEST_PACKAGE_TO_SCAN = "com.structurizr.componentfinder.typeBased.myapp";
    public static final String PAPERBOY_APP_PACKAGE_TO_SCAN = "com.structurizr.testapp.paperboy";
    public static final String CONTROLLER_SUFFIX = "Controller";
    public static final String REPOSITORY_SUFFIX = "Repository";
    public static final String JAVA_INTERFACE = "JAVA_INTERFACE";
    public static final String JAVA_CLASS = "JAVA_CLASS";
    public static final String MYAPP_TAG = "MYAPP";
    public static final String PAPERBOY_TAG = "PAPERBOY";
    public static final String FACTORY_TAG = "FACTORY";
    public static final String DOMAIN_TAG = "DOMAIN";
    public static final String SERVICE_TAG = "SERVICE";
    public static final String MATCH_PAPERBOY_PACKAGE_REGEX = ".*\\.paperboy\\..*";

    public static final String MATCH_ANY_PACKAGE_REGEX = "[\\w*\\.]*";
    public static final String MATCH_MYAPP_PACKAGE_REGEX = ".*\\.basic\\..*";
    public static final String PAPERBOY_MODEL_PACKAGE_REGEX = MATCH_ANY_PACKAGE_REGEX + "paperboy" + "." + MATCH_ANY_PACKAGE_REGEX + "model";
    public static final String MATCH_ALL_UNDER_PAPERBOY_MODEL_PACKAGE_REGEX = PAPERBOY_MODEL_PACKAGE_REGEX + "\\..*";
    public static final String PAPERBOY_SERVICE_PACKAGE_REGEX = MATCH_ANY_PACKAGE_REGEX + "paperboy" + "." + MATCH_ANY_PACKAGE_REGEX + "service";
    public static final String MATCH_ALL_UNDER_PAPERBOY_SERVICE_PACKAGE_REGEX = PAPERBOY_SERVICE_PACKAGE_REGEX + "\\..*";
    public static final String MATCH_ALL_UNDER_PAPERBOY_PACKAGE_REGEX = ".*\\.paperboy\\..*";
    public static final String MATCH_ALL_UNDER_MYAPP_PACKAGE_REGEX = ".*typeBased\\.myapp\\..*";
    public static final String MATCH_EVERYTHING_REGEX = ".*";
    public static final String MATCH_CONTROLLER_SUFFIX_IN_MY_APP_REGEX = MATCH_ALL_UNDER_MYAPP_PACKAGE_REGEX + CONTROLLER_SUFFIX;

    private TestConstants() {
    }

    public static Container createDefaultContainer() {
        final Workspace workspace = new Workspace("Name", "Description");
        final Model model = workspace.getModel();
        final SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        return softwareSystem.addContainer("Name", "Description", "Technology");
    }
}
