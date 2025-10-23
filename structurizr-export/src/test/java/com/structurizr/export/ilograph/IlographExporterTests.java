package com.structurizr.export.ilograph;

import com.structurizr.Workspace;
import com.structurizr.export.AbstractExporterTests;
import com.structurizr.export.WorkspaceExport;
import com.structurizr.http.HttpClient;
import com.structurizr.model.CustomElement;
import com.structurizr.model.Model;
import com.structurizr.util.WorkspaceUtils;
import com.structurizr.view.ThemeUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IlographExporterTests extends AbstractExporterTests {

    @Test
    public void test_BigBankPlcExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/big-bank-plc.json"));
        IlographExporter ilographExporter = new IlographExporter();
        WorkspaceExport export = ilographExporter.export(workspace);

        assertEquals("""
resources:
  - id: "1"
    name: "Personal Banking Customer"
    subtitle: "[Person]"
    description: "A customer of the bank, with personal bank accounts."
    backgroundColor: "#08427b"
    color: "#ffffff"

  - id: "2"
    name: "Customer Service Staff"
    subtitle: "[Person]"
    description: "Customer service staff within the bank."
    backgroundColor: "#999999"
    color: "#ffffff"

  - id: "3"
    name: "Back Office Staff"
    subtitle: "[Person]"
    description: "Administration and support staff within the bank."
    backgroundColor: "#999999"
    color: "#ffffff"

  - id: "4"
    name: "Mainframe Banking System"
    subtitle: "[Software System]"
    description: "Stores all of the core banking information about customers, accounts, transactions, etc."
    backgroundColor: "#999999"
    color: "#ffffff"

  - id: "5"
    name: "E-mail System"
    subtitle: "[Software System]"
    description: "The internal Microsoft Exchange e-mail system."
    backgroundColor: "#999999"
    color: "#ffffff"

  - id: "6"
    name: "ATM"
    subtitle: "[Software System]"
    description: "Allows customers to withdraw cash."
    backgroundColor: "#999999"
    color: "#ffffff"

  - id: "7"
    name: "Internet Banking System"
    subtitle: "[Software System]"
    description: "Allows customers to view information about their bank accounts, and make payments."
    backgroundColor: "#1168bd"
    color: "#ffffff"

    children:
      - id: "10"
        name: "Web Application"
        subtitle: "[Container: Java and Spring MVC]"
        description: "Delivers the static content and the Internet banking single page application."
        backgroundColor: "#438dd5"
        color: "#ffffff"

      - id: "11"
        name: "API Application"
        subtitle: "[Container: Java and Spring MVC]"
        description: "Provides Internet banking functionality via a JSON/HTTPS API."
        backgroundColor: "#438dd5"
        color: "#ffffff"

        children:
          - id: "12"
            name: "Sign In Controller"
            subtitle: "[Component: Spring MVC Rest Controller]"
            description: "Allows users to sign in to the Internet Banking System."
            backgroundColor: "#85bbf0"
            color: "#000000"

          - id: "13"
            name: "Accounts Summary Controller"
            subtitle: "[Component: Spring MVC Rest Controller]"
            description: "Provides customers with a summary of their bank accounts."
            backgroundColor: "#85bbf0"
            color: "#000000"

          - id: "14"
            name: "Reset Password Controller"
            subtitle: "[Component: Spring MVC Rest Controller]"
            description: "Allows users to reset their passwords with a single use URL."
            backgroundColor: "#85bbf0"
            color: "#000000"

          - id: "15"
            name: "Security Component"
            subtitle: "[Component: Spring Bean]"
            description: "Provides functionality related to signing in, changing passwords, etc."
            backgroundColor: "#85bbf0"
            color: "#000000"

          - id: "16"
            name: "Mainframe Banking System Facade"
            subtitle: "[Component: Spring Bean]"
            description: "A facade onto the mainframe banking system."
            backgroundColor: "#85bbf0"
            color: "#000000"

          - id: "17"
            name: "E-mail Component"
            subtitle: "[Component: Spring Bean]"
            description: "Sends e-mails to users."
            backgroundColor: "#85bbf0"
            color: "#000000"

      - id: "18"
        name: "Database"
        subtitle: "[Container: Oracle Database Schema]"
        description: "Stores user registration information, hashed authentication credentials, access logs, etc."
        backgroundColor: "#438dd5"
        color: "#ffffff"

      - id: "8"
        name: "Single-Page Application"
        subtitle: "[Container: JavaScript and Angular]"
        description: "Provides all of the Internet banking functionality to customers via their web browser."
        backgroundColor: "#438dd5"
        color: "#ffffff"

      - id: "9"
        name: "Mobile App"
        subtitle: "[Container: Xamarin]"
        description: "Provides a limited subset of the Internet banking functionality to customers via their mobile device."
        backgroundColor: "#438dd5"
        color: "#ffffff"

  - id: "50"
    name: "Developer Laptop"
    subtitle: "[Deployment Node: Microsoft Windows 10 or Apple macOS]"
    backgroundColor: "#ffffff"
    color: "#444444"

    children:
      - id: "51"
        name: "Web Browser"
        subtitle: "[Deployment Node: Chrome, Firefox, Safari, or Edge]"
        backgroundColor: "#ffffff"
        color: "#444444"

        children:
          - id: "52"
            name: "Single-Page Application"
            subtitle: "[Container: JavaScript and Angular]"
            description: "Provides all of the Internet banking functionality to customers via their web browser."
            backgroundColor: "#438dd5"
            color: "#ffffff"

      - id: "53"
        name: "Docker Container - Web Server"
        subtitle: "[Deployment Node: Docker]"
        backgroundColor: "#ffffff"
        color: "#444444"

        children:
          - id: "54"
            name: "Apache Tomcat"
            subtitle: "[Deployment Node: Apache Tomcat 8.x]"
            backgroundColor: "#ffffff"
            color: "#444444"

            children:
              - id: "55"
                name: "Web Application"
                subtitle: "[Container: Java and Spring MVC]"
                description: "Delivers the static content and the Internet banking single page application."
                backgroundColor: "#438dd5"
                color: "#ffffff"

              - id: "57"
                name: "API Application"
                subtitle: "[Container: Java and Spring MVC]"
                description: "Provides Internet banking functionality via a JSON/HTTPS API."
                backgroundColor: "#438dd5"
                color: "#ffffff"

      - id: "59"
        name: "Docker Container - Database Server"
        subtitle: "[Deployment Node: Docker]"
        backgroundColor: "#ffffff"
        color: "#444444"

        children:
          - id: "60"
            name: "Database Server"
            subtitle: "[Deployment Node: Oracle 12c]"
            backgroundColor: "#ffffff"
            color: "#444444"

            children:
              - id: "61"
                name: "Database"
                subtitle: "[Container: Oracle Database Schema]"
                description: "Stores user registration information, hashed authentication credentials, access logs, etc."
                backgroundColor: "#438dd5"
                color: "#ffffff"

  - id: "63"
    name: "Big Bank plc"
    subtitle: "[Deployment Node: Big Bank plc data center]"
    backgroundColor: "#ffffff"
    color: "#444444"

    children:
      - id: "64"
        name: "bigbank-dev001"
        subtitle: "[Deployment Node]"
        backgroundColor: "#ffffff"
        color: "#444444"

        children:
          - id: "65"
            name: "Mainframe Banking System"
            subtitle: "[Software System]"
            description: "Stores all of the core banking information about customers, accounts, transactions, etc."
            backgroundColor: "#999999"
            color: "#ffffff"

  - id: "67"
    name: "Customer's mobile device"
    subtitle: "[Deployment Node: Apple iOS or Android]"
    backgroundColor: "#ffffff"
    color: "#444444"

    children:
      - id: "68"
        name: "Mobile App"
        subtitle: "[Container: Xamarin]"
        description: "Provides a limited subset of the Internet banking functionality to customers via their mobile device."
        backgroundColor: "#438dd5"
        color: "#ffffff"

  - id: "69"
    name: "Customer's computer"
    subtitle: "[Deployment Node: Microsoft Windows or Apple macOS]"
    backgroundColor: "#ffffff"
    color: "#444444"

    children:
      - id: "70"
        name: "Web Browser"
        subtitle: "[Deployment Node: Chrome, Firefox, Safari, or Edge]"
        backgroundColor: "#ffffff"
        color: "#444444"

        children:
          - id: "71"
            name: "Single-Page Application"
            subtitle: "[Container: JavaScript and Angular]"
            description: "Provides all of the Internet banking functionality to customers via their web browser."
            backgroundColor: "#438dd5"
            color: "#ffffff"

  - id: "72"
    name: "Big Bank plc"
    subtitle: "[Deployment Node: Big Bank plc data center]"
    backgroundColor: "#ffffff"
    color: "#444444"

    children:
      - id: "73"
        name: "bigbank-web***"
        subtitle: "[Deployment Node: Ubuntu 16.04 LTS]"
        backgroundColor: "#ffffff"
        color: "#444444"

        children:
          - id: "74"
            name: "Apache Tomcat"
            subtitle: "[Deployment Node: Apache Tomcat 8.x]"
            backgroundColor: "#ffffff"
            color: "#444444"

            children:
              - id: "75"
                name: "Web Application"
                subtitle: "[Container: Java and Spring MVC]"
                description: "Delivers the static content and the Internet banking single page application."
                backgroundColor: "#438dd5"
                color: "#ffffff"

      - id: "77"
        name: "bigbank-api***"
        subtitle: "[Deployment Node: Ubuntu 16.04 LTS]"
        backgroundColor: "#ffffff"
        color: "#444444"

        children:
          - id: "78"
            name: "Apache Tomcat"
            subtitle: "[Deployment Node: Apache Tomcat 8.x]"
            backgroundColor: "#ffffff"
            color: "#444444"

            children:
              - id: "79"
                name: "API Application"
                subtitle: "[Container: Java and Spring MVC]"
                description: "Provides Internet banking functionality via a JSON/HTTPS API."
                backgroundColor: "#438dd5"
                color: "#ffffff"

      - id: "82"
        name: "bigbank-db01"
        subtitle: "[Deployment Node: Ubuntu 16.04 LTS]"
        backgroundColor: "#ffffff"
        color: "#444444"

        children:
          - id: "83"
            name: "Oracle - Primary"
            subtitle: "[Deployment Node: Oracle 12c]"
            backgroundColor: "#ffffff"
            color: "#444444"

            children:
              - id: "84"
                name: "Database"
                subtitle: "[Container: Oracle Database Schema]"
                description: "Stores user registration information, hashed authentication credentials, access logs, etc."
                backgroundColor: "#438dd5"
                color: "#ffffff"

      - id: "86"
        name: "bigbank-db02"
        subtitle: "[Deployment Node: Ubuntu 16.04 LTS]"
        backgroundColor: "#ffffff"
        color: "#444444"

        children:
          - id: "87"
            name: "Oracle - Secondary"
            subtitle: "[Deployment Node: Oracle 12c]"
            backgroundColor: "#ffffff"
            color: "#444444"

            children:
              - id: "88"
                name: "Database"
                subtitle: "[Container: Oracle Database Schema]"
                description: "Stores user registration information, hashed authentication credentials, access logs, etc."
                backgroundColor: "#438dd5"
                color: "#ffffff"

      - id: "90"
        name: "bigbank-prod001"
        subtitle: "[Deployment Node]"
        backgroundColor: "#ffffff"
        color: "#444444"

        children:
          - id: "91"
            name: "Mainframe Banking System"
            subtitle: "[Software System]"
            description: "Stores all of the core banking information about customers, accounts, transactions, etc."
            backgroundColor: "#999999"
            color: "#ffffff"

perspectives:
  - name: Static Structure
    relations:
      - from: "1"
        to: "7"
        label: "Views account balances, and makes payments using"
        color: "#444444"

      - from: "1"
        to: "2"
        label: "Asks questions to"
        description: "Telephone"
        color: "#444444"

      - from: "1"
        to: "6"
        label: "Withdraws cash using"
        color: "#444444"

      - from: "2"
        to: "4"
        label: "Uses"
        color: "#444444"

      - from: "3"
        to: "4"
        label: "Uses"
        color: "#444444"

      - from: "5"
        to: "1"
        label: "Sends e-mails to"
        color: "#444444"

      - from: "6"
        to: "4"
        label: "Uses"
        color: "#444444"

      - from: "7"
        to: "4"
        label: "Gets account information from, and makes payments using"
        color: "#444444"

      - from: "7"
        to: "5"
        label: "Sends e-mail using"
        color: "#444444"

      - from: "1"
        to: "10"
        label: "Visits bigbank.com/ib using"
        description: "HTTPS"
        color: "#444444"

      - from: "1"
        to: "8"
        label: "Views account balances, and makes payments using"
        color: "#444444"

      - from: "1"
        to: "9"
        label: "Views account balances, and makes payments using"
        color: "#444444"

      - from: "10"
        to: "8"
        label: "Delivers to the customer's web browser"
        color: "#444444"

      - from: "11"
        to: "18"
        label: "Reads from and writes to"
        description: "SQL/TCP"
        color: "#444444"

      - from: "11"
        to: "4"
        label: "Makes API calls to"
        description: "XML/HTTPS"
        color: "#444444"

      - from: "11"
        to: "5"
        label: "Sends e-mail using"
        color: "#444444"

      - from: "8"
        to: "11"
        label: "Makes API calls to"
        description: "JSON/HTTPS"
        color: "#444444"

      - from: "9"
        to: "11"
        label: "Makes API calls to"
        description: "JSON/HTTPS"
        color: "#444444"

      - from: "12"
        to: "15"
        label: "Uses"
        color: "#444444"

      - from: "13"
        to: "16"
        label: "Uses"
        color: "#444444"

      - from: "14"
        to: "15"
        label: "Uses"
        color: "#444444"

      - from: "14"
        to: "17"
        label: "Uses"
        color: "#444444"

      - from: "15"
        to: "18"
        label: "Reads from and writes to"
        description: "SQL/TCP"
        color: "#444444"

      - from: "16"
        to: "4"
        label: "Makes API calls to"
        description: "XML/HTTPS"
        color: "#444444"

      - from: "17"
        to: "5"
        label: "Sends e-mail using"
        color: "#444444"

      - from: "8"
        to: "12"
        label: "Makes API calls to"
        description: "JSON/HTTPS"
        color: "#444444"

      - from: "8"
        to: "13"
        label: "Makes API calls to"
        description: "JSON/HTTPS"
        color: "#444444"

      - from: "8"
        to: "14"
        label: "Makes API calls to"
        description: "JSON/HTTPS"
        color: "#444444"

      - from: "9"
        to: "12"
        label: "Makes API calls to"
        description: "JSON/HTTPS"
        color: "#444444"

      - from: "9"
        to: "13"
        label: "Makes API calls to"
        description: "JSON/HTTPS"
        color: "#444444"

      - from: "9"
        to: "14"
        label: "Makes API calls to"
        description: "JSON/HTTPS"
        color: "#444444"

  - name: Dynamic: Internet Banking System - API Application
    sequence:
      start: "8"
      steps:
      - to: "12"
        label: "1. Submits credentials to"
        description: "JSON/HTTPS"
        color: "#444444"

      - to: "15"
        label: "2. Validates credentials using"
        color: "#444444"

      - to: "18"
        label: "3. select * from users where username = ?"
        description: "SQL/TCP"
        color: "#444444"

      - to: "15"
        label: "4. Returns user data to"
        description: "SQL/TCP"
        color: "#444444"

      - to: "12"
        label: "5. Returns true if the hashed password matches"
        color: "#444444"

      - to: "8"
        label: "6. Sends back an authentication token to"
        description: "JSON/HTTPS"
        color: "#444444"

  - name: Deployment - Development
    relations:
      - from: "52"
        to: "57"
        label: "Makes API calls to"
        description: "JSON/HTTPS"
        color: "#444444"
      - from: "55"
        to: "52"
        label: "Delivers to the customer's web browser"
        color: "#444444"
      - from: "57"
        to: "61"
        label: "Reads from and writes to"
        description: "SQL/TCP"
        color: "#444444"
      - from: "57"
        to: "65"
        label: "Makes API calls to"
        description: "XML/HTTPS"
        color: "#444444"
  - name: Deployment - Live
    relations:
      - from: "68"
        to: "79"
        label: "Makes API calls to"
        description: "JSON/HTTPS"
        color: "#444444"
      - from: "71"
        to: "79"
        label: "Makes API calls to"
        description: "JSON/HTTPS"
        color: "#444444"
      - from: "75"
        to: "71"
        label: "Delivers to the customer's web browser"
        color: "#444444"
      - from: "79"
        to: "84"
        label: "Reads from and writes to"
        description: "SQL/TCP"
        color: "#444444"
      - from: "79"
        to: "88"
        label: "Reads from and writes to"
        description: "SQL/TCP"
        color: "#444444"
      - from: "79"
        to: "91"
        label: "Makes API calls to"
        description: "XML/HTTPS"
        color: "#444444\"""", export.getDefinition());
    }

    @Test
    @Tag("IntegrationTest")
    void test_AmazonWebServicesExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/amazon-web-services.json"));
        workspace.getViews().getConfiguration().getStyles().addElementStyle("Amazon Web Services - Route 53").addProperty(IlographExporter.ILOGRAPH_ICON, "AWS/Networking/Route-53.svg");

        HttpClient httpClient = new HttpClient();
        httpClient.allow(".*");
        ThemeUtils.loadThemes(workspace, httpClient);

        IlographExporter ilographExporter = new IlographExporter();
        WorkspaceExport export = ilographExporter.export(workspace);

        assertEquals("""
                resources:
                  - id: "1"
                    name: "X"
                    subtitle: "[Software System]"
                    backgroundColor: "#ffffff"
                    color: "#444444"
                
                    children:
                      - id: "2"
                        name: "Web Application"
                        subtitle: "[Container: Java and Spring Boot]"
                        backgroundColor: "#ffffff"
                        color: "#444444"
                
                      - id: "3"
                        name: "Database Schema"
                        subtitle: "[Container]"
                        backgroundColor: "#ffffff"
                        color: "#444444"
                
                  - id: "5"
                    name: "Amazon Web Services"
                    subtitle: "[Deployment Node]"
                    backgroundColor: "#ffffff"
                    color: "#232f3e"
                    icon: "https://static.structurizr.com/themes/amazon-web-services-2020.04.30/aws-cloud.png"
                
                    children:
                      - id: "6"
                        name: "US-East-1"
                        subtitle: "[Deployment Node]"
                        backgroundColor: "#ffffff"
                        color: "#147eba"
                        icon: "https://static.structurizr.com/themes/amazon-web-services-2020.04.30/region.png"
                
                        children:
                          - id: "10"
                            name: "Autoscaling group"
                            subtitle: "[Deployment Node]"
                            backgroundColor: "#ffffff"
                            color: "#cc2264"
                            icon: "https://static.structurizr.com/themes/amazon-web-services-2020.04.30/aws-auto-scaling.png"
                
                            children:
                              - id: "11"
                                name: "Amazon EC2 - Ubuntu server"
                                subtitle: "[Deployment Node]"
                                backgroundColor: "#ffffff"
                                color: "#d86613"
                                icon: "https://static.structurizr.com/themes/amazon-web-services-2020.04.30/amazon-ec2.png"
                
                                children:
                                  - id: "12"
                                    name: "Web Application"
                                    subtitle: "[Container: Java and Spring Boot]"
                                    backgroundColor: "#ffffff"
                                    color: "#444444"
                
                          - id: "14"
                            name: "Amazon RDS"
                            subtitle: "[Deployment Node]"
                            backgroundColor: "#ffffff"
                            color: "#3b48cc"
                            icon: "https://static.structurizr.com/themes/amazon-web-services-2020.04.30/amazon-rds.png"
                
                            children:
                              - id: "15"
                                name: "MySQL"
                                subtitle: "[Deployment Node]"
                                backgroundColor: "#ffffff"
                                color: "#3b48cc"
                                icon: "https://static.structurizr.com/themes/amazon-web-services-2020.04.30/amazon-rds-mysql-instance.png"
                
                                children:
                                  - id: "16"
                                    name: "Database Schema"
                                    subtitle: "[Container]"
                                    backgroundColor: "#ffffff"
                                    color: "#444444"
                
                          - id: "7"
                            name: "DNS router"
                            subtitle: "[Infrastructure Node: Route 53]"
                            description: "Routes incoming requests based upon domain name."
                            backgroundColor: "#ffffff"
                            color: "#693cc5"
                            icon: "AWS/Networking/Route-53.svg"
                
                          - id: "8"
                            name: "Load Balancer"
                            subtitle: "[Infrastructure Node: Elastic Load Balancer]"
                            description: "Automatically distributes incoming application traffic."
                            backgroundColor: "#ffffff"
                            color: "#693cc5"
                            icon: "https://static.structurizr.com/themes/amazon-web-services-2020.04.30/elastic-load-balancing.png"
                
                perspectives:
                  - name: Static Structure
                    relations:
                      - from: "2"
                        to: "3"
                        label: "Reads from and writes to"
                        description: "MySQL Protocol/SSL"
                        color: "#444444"
                
                  - name: Deployment - Live
                    relations:
                      - from: "12"
                        to: "16"
                        label: "Reads from and writes to"
                        description: "MySQL Protocol/SSL"
                        color: "#444444"
                      - from: "7"
                        to: "8"
                        label: "Forwards requests to"
                        description: "HTTPS"
                        color: "#444444"
                      - from: "8"
                        to: "12"
                        label: "Forwards requests to"
                        description: "HTTPS"
                        color: "#444444\"""", export.getDefinition());
    }

    @Test
    void test_renderCustomElements() {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();

        CustomElement a = model.addCustomElement("A");
        CustomElement b = model.addCustomElement("B", "Custom", "Description");
        a.uses(b, "Uses");

        WorkspaceExport export = new IlographExporter().export(workspace);
        assertEquals("""
                resources:
                  - id: "1"
                    name: "A"
                    subtitle: ""
                    backgroundColor: "#ffffff"
                    color: "#444444"
                
                  - id: "2"
                    name: "B"
                    subtitle: "[Custom]"
                    description: "Description"
                    backgroundColor: "#ffffff"
                    color: "#444444"
                
                perspectives:
                  - name: Static Structure
                    relations:
                      - from: "1"
                        to: "2"
                        label: "Uses"
                        color: "#444444"
                """, export.getDefinition());
    }

    @Test
    void test_imports() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.addProperty(IlographExporter.ILOGRAPH_IMPORTS, "NAMESPACE1:path1,NAMESPACE2:path2");

        WorkspaceExport export = new IlographExporter().export(workspace);
        assertEquals("""
imports:
- from: path1
  namespace: NAMESPACE1
- from: path2
  namespace: NAMESPACE2

resources:
perspectives:
  - name: Static Structure
    relations:""", export.getDefinition());
    }

}