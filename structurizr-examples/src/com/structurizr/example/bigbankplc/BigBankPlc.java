package com.structurizr.example.bigbankplc;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.StructurizrDocumentationTemplate;
import com.structurizr.model.*;
import com.structurizr.util.MapUtils;
import com.structurizr.view.*;

/**
 * This is an example workspace to illustrate the key features of Structurizr, based around a fictional bank.
 */
public abstract class BigBankPlc {

    static final String EXISTING_SYSTEM_TAG = "Existing System";
    static final String CUSTOMER_TAG = "Customer";
    static final String BANK_STAFF_TAG = "Bank Staff";

    protected Workspace workspace;
    protected Model model;
    protected ViewSet views;

    Person customer;
    Person customerServiceStaff;
    Person backOfficeStaff;
    SoftwareSystem internetBankingSystem;
    SoftwareSystem mainframeBankingSystem;
    SoftwareSystem emailSystem;
    SoftwareSystem atm;

    BigBankPlc() {
        workspace = new Workspace("", "");
        model = workspace.getModel();
        model.setEnterprise(new Enterprise("Big Bank plc"));
        model.setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy());

        customer = model.addPerson(Location.External, "Personal Banking Customer", "A customer of the bank, with personal bank accounts.");
        customer.addTags(CUSTOMER_TAG);

        internetBankingSystem = model.addSoftwareSystem(Location.Internal, "Internet Banking System", "Allows customers to view information about their bank accounts, and make payments.");
        customer.uses(internetBankingSystem, "Views account balances, and makes payments using");

        mainframeBankingSystem = model.addSoftwareSystem(Location.Internal, "Mainframe Banking System", "Stores all of the core banking information about customers, accounts, transactions, etc.");
        mainframeBankingSystem.addTags(EXISTING_SYSTEM_TAG);
        internetBankingSystem.uses(mainframeBankingSystem, "Gets account information from, and makes payments using");

        emailSystem = model.addSoftwareSystem(Location.Internal, "E-mail System", "The internal Microsoft Exchange e-mail system.");
        internetBankingSystem.uses(emailSystem, "Sends e-mail using");
        emailSystem.addTags(EXISTING_SYSTEM_TAG);
        emailSystem.delivers(customer, "Sends e-mails to");

        atm = model.addSoftwareSystem(Location.Internal, "ATM", "Allows customers to withdraw cash.");
        atm.addTags(EXISTING_SYSTEM_TAG);
        atm.uses(mainframeBankingSystem, "Uses");
        customer.uses(atm, "Withdraws cash using");

        customerServiceStaff = model.addPerson(Location.Internal, "Customer Service Staff", "Customer service staff within the bank.");
        customerServiceStaff.addTags(BANK_STAFF_TAG);
        customerServiceStaff.uses(mainframeBankingSystem, "Uses");
        customer.interactsWith(customerServiceStaff, "Asks questions to", "Telephone");

        backOfficeStaff = model.addPerson(Location.Internal, "Back Office Staff", "Administration and support staff within the bank.");
        backOfficeStaff.addTags(BANK_STAFF_TAG);
        backOfficeStaff.uses(mainframeBankingSystem, "Uses");

        views = workspace.getViews();
        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle(Tags.PERSON).color("#ffffff").shape(Shape.Person).fontSize(22);
        styles.addElementStyle(CUSTOMER_TAG).background("#08427b");
        styles.addElementStyle(BANK_STAFF_TAG).background("#999999");
    }

}