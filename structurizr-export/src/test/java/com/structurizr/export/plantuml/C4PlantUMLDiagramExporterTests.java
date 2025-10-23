package com.structurizr.export.plantuml;

import com.structurizr.Workspace;
import com.structurizr.export.AbstractExporterTests;
import com.structurizr.export.Diagram;
import com.structurizr.http.HttpClient;
import com.structurizr.model.*;
import com.structurizr.util.WorkspaceUtils;
import com.structurizr.view.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class C4PlantUMLDiagramExporterTests extends AbstractExporterTests {

    @Test
    public void test_BigBankPlcExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/big-bank-plc.json"));
        workspace.getViews().getConfiguration().addProperty(C4PlantUMLExporter.C4PLANTUML_TAGS_PROPERTY, "true");

        C4PlantUMLExporter exporter = new C4PlantUMLExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(7, diagrams.size());

        Diagram diagram = diagrams.stream().filter(d -> d.getKey().equals("SystemLandscape")).findFirst().get();
        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                
                AddElementTag("Software System", $bgColor="#1168bd", $borderColor="#0b4884", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Person,Bank Staff", $bgColor="#999999", $borderColor="#6b6b6b", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Software System,Existing System", $bgColor="#999999", $borderColor="#6b6b6b", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Person,Customer", $bgColor="#08427b", $borderColor="#052e56", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                
                AddRelTag("Relationship", $textColor="#444444", $lineColor="#444444", $lineStyle = DashedLine())
                
                AddBoundaryTag("Big Bank plc", $borderColor="#cccccc", $fontColor="#cccccc", $borderStyle="dashed")
                Boundary(group_1, "Big Bank plc", $tags="Big Bank plc") {
                  Person(CustomerServiceStaff, "Customer Service Staff", $descr="Customer service staff within the bank.", $tags="Person,Bank Staff", $link="")
                  Person(BackOfficeStaff, "Back Office Staff", $descr="Administration and support staff within the bank.", $tags="Person,Bank Staff", $link="")
                  System(MainframeBankingSystem, "Mainframe Banking System", $descr="Stores all of the core banking information about customers, accounts, transactions, etc.", $tags="Software System,Existing System", $link="")
                  System(EmailSystem, "E-mail System", $descr="The internal Microsoft Exchange e-mail system.", $tags="Software System,Existing System", $link="")
                  System(ATM, "ATM", $descr="Allows customers to withdraw cash.", $tags="Software System,Existing System", $link="")
                  System(InternetBankingSystem, "Internet Banking System", $descr="Allows customers to view information about their bank accounts, and make payments.", $tags="Software System", $link="")
                }
                
                Person(PersonalBankingCustomer, "Personal Banking Customer", $descr="A customer of the bank, with personal bank accounts.", $tags="Person,Customer", $link="")
                
                Rel(PersonalBankingCustomer, InternetBankingSystem, "Views account balances, and makes payments using", $techn="", $tags="Relationship", $link="")
                Rel(InternetBankingSystem, MainframeBankingSystem, "Gets account information from, and makes payments using", $techn="", $tags="Relationship", $link="")
                Rel(InternetBankingSystem, EmailSystem, "Sends e-mail using", $techn="", $tags="Relationship", $link="")
                Rel(EmailSystem, PersonalBankingCustomer, "Sends e-mails to", $techn="", $tags="Relationship", $link="")
                Rel(PersonalBankingCustomer, CustomerServiceStaff, "Asks questions to", $techn="Telephone", $tags="Relationship", $link="")
                Rel(CustomerServiceStaff, MainframeBankingSystem, "Uses", $techn="", $tags="Relationship", $link="")
                Rel(PersonalBankingCustomer, ATM, "Withdraws cash using", $techn="", $tags="Relationship", $link="")
                Rel(ATM, MainframeBankingSystem, "Uses", $techn="", $tags="Relationship", $link="")
                Rel(BackOfficeStaff, MainframeBankingSystem, "Uses", $techn="", $tags="Relationship", $link="")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());

        diagram = diagrams.stream().filter(d -> d.getKey().equals("SystemContext")).findFirst().get();
        assertEquals("""
                @startuml
                title <size:24>System Context View: Internet Banking System</size>\\n<size:24>The system context diagram for the Internet Banking System.</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                
                AddElementTag("Software System", $bgColor="#1168bd", $borderColor="#0b4884", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Software System,Existing System", $bgColor="#999999", $borderColor="#6b6b6b", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Person,Customer", $bgColor="#08427b", $borderColor="#052e56", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                
                AddRelTag("Relationship", $textColor="#444444", $lineColor="#444444", $lineStyle = DashedLine())
                
                AddBoundaryTag("Big Bank plc", $borderColor="#cccccc", $fontColor="#cccccc", $borderStyle="dashed")
                Boundary(group_1, "Big Bank plc", $tags="Big Bank plc") {
                  System(MainframeBankingSystem, "Mainframe Banking System", $descr="Stores all of the core banking information about customers, accounts, transactions, etc.", $tags="Software System,Existing System", $link="")
                  System(EmailSystem, "E-mail System", $descr="The internal Microsoft Exchange e-mail system.", $tags="Software System,Existing System", $link="")
                  System(InternetBankingSystem, "Internet Banking System", $descr="Allows customers to view information about their bank accounts, and make payments.", $tags="Software System", $link="")
                }
                
                Person(PersonalBankingCustomer, "Personal Banking Customer", $descr="A customer of the bank, with personal bank accounts.", $tags="Person,Customer", $link="")
                
                Rel(PersonalBankingCustomer, InternetBankingSystem, "Views account balances, and makes payments using", $techn="", $tags="Relationship", $link="")
                Rel(InternetBankingSystem, MainframeBankingSystem, "Gets account information from, and makes payments using", $techn="", $tags="Relationship", $link="")
                Rel(InternetBankingSystem, EmailSystem, "Sends e-mail using", $techn="", $tags="Relationship", $link="")
                Rel(EmailSystem, PersonalBankingCustomer, "Sends e-mails to", $techn="", $tags="Relationship", $link="")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());

        diagram = diagrams.stream().filter(d -> d.getKey().equals("Containers")).findFirst().get();
        assertEquals("""
                @startuml
                title <size:24>Container View: Internet Banking System</size>\\n<size:24>The container diagram for the Internet Banking System.</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                !include <C4/C4_Container>
                
                AddElementTag("Software System,Existing System", $bgColor="#999999", $borderColor="#6b6b6b", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Container,Mobile App", $bgColor="#438dd5", $borderColor="#2e6295", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Container,Database", $bgColor="#438dd5", $borderColor="#2e6295", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Container,Web Browser", $bgColor="#438dd5", $borderColor="#2e6295", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Container", $bgColor="#438dd5", $borderColor="#2e6295", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Person,Customer", $bgColor="#08427b", $borderColor="#052e56", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                
                AddRelTag("Relationship", $textColor="#444444", $lineColor="#444444", $lineStyle = DashedLine())
                
                AddBoundaryTag("Software System", $bgColor="#ffffff", $borderColor="#0b4884", $fontColor="#0b4884", $shadowing="", $borderStyle="solid")
                
                Person(PersonalBankingCustomer, "Personal Banking Customer", $descr="A customer of the bank, with personal bank accounts.", $tags="Person,Customer", $link="")
                System(MainframeBankingSystem, "Mainframe Banking System", $descr="Stores all of the core banking information about customers, accounts, transactions, etc.", $tags="Software System,Existing System", $link="")
                System(EmailSystem, "E-mail System", $descr="The internal Microsoft Exchange e-mail system.", $tags="Software System,Existing System", $link="")
                
                System_Boundary("InternetBankingSystem_boundary", "Internet Banking System", $tags="Software System") {
                  Container(InternetBankingSystem.WebApplication, "Web Application", $techn="Java and Spring MVC", $descr="Delivers the static content and the Internet banking single page application.", $tags="Container", $link="")
                  Container(InternetBankingSystem.APIApplication, "API Application", $techn="Java and Spring MVC", $descr="Provides Internet banking functionality via a JSON/HTTPS API.", $tags="Container", $link="")
                  ContainerDb(InternetBankingSystem.Database, "Database", $techn="Oracle Database Schema", $descr="Stores user registration information, hashed authentication credentials, access logs, etc.", $tags="Container,Database", $link="")
                  Container(InternetBankingSystem.SinglePageApplication, "Single-Page Application", $techn="JavaScript and Angular", $descr="Provides all of the Internet banking functionality to customers via their web browser.", $tags="Container,Web Browser", $link="")
                  Container(InternetBankingSystem.MobileApp, "Mobile App", $techn="Xamarin", $descr="Provides a limited subset of the Internet banking functionality to customers via their mobile device.", $tags="Container,Mobile App", $link="")
                }
                
                Rel(EmailSystem, PersonalBankingCustomer, "Sends e-mails to", $techn="", $tags="Relationship", $link="")
                Rel(PersonalBankingCustomer, InternetBankingSystem.WebApplication, "Visits bigbank.com/ib using", $techn="HTTPS", $tags="Relationship", $link="")
                Rel(PersonalBankingCustomer, InternetBankingSystem.SinglePageApplication, "Views account balances, and makes payments using", $techn="", $tags="Relationship", $link="")
                Rel(PersonalBankingCustomer, InternetBankingSystem.MobileApp, "Views account balances, and makes payments using", $techn="", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.WebApplication, InternetBankingSystem.SinglePageApplication, "Delivers to the customer's web browser", $techn="", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.SinglePageApplication, InternetBankingSystem.APIApplication, "Makes API calls to", $techn="JSON/HTTPS", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.MobileApp, InternetBankingSystem.APIApplication, "Makes API calls to", $techn="JSON/HTTPS", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.APIApplication, InternetBankingSystem.Database, "Reads from and writes to", $techn="SQL/TCP", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.APIApplication, MainframeBankingSystem, "Makes API calls to", $techn="XML/HTTPS", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.APIApplication, EmailSystem, "Sends e-mail using", $techn="", $tags="Relationship", $link="")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());

        diagram = diagrams.stream().filter(d -> d.getKey().equals("Components")).findFirst().get();
        assertEquals("""
                @startuml
                title <size:24>Component View: Internet Banking System - API Application</size>\\n<size:24>The component diagram for the API Application.</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                !include <C4/C4_Container>
                !include <C4/C4_Component>
                
                AddElementTag("Software System,Existing System", $bgColor="#999999", $borderColor="#6b6b6b", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Component", $bgColor="#85bbf0", $borderColor="#5d82a8", $fontColor="#000000", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Container,Mobile App", $bgColor="#438dd5", $borderColor="#2e6295", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Container,Database", $bgColor="#438dd5", $borderColor="#2e6295", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Container,Web Browser", $bgColor="#438dd5", $borderColor="#2e6295", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                
                AddRelTag("Relationship", $textColor="#444444", $lineColor="#444444", $lineStyle = DashedLine())
                
                AddBoundaryTag("Container", $bgColor="#ffffff", $borderColor="#2e6295", $fontColor="#2e6295", $shadowing="", $borderStyle="solid")
                
                System(MainframeBankingSystem, "Mainframe Banking System", $descr="Stores all of the core banking information about customers, accounts, transactions, etc.", $tags="Software System,Existing System", $link="")
                System(EmailSystem, "E-mail System", $descr="The internal Microsoft Exchange e-mail system.", $tags="Software System,Existing System", $link="")
                
                System_Boundary("InternetBankingSystem_boundary", "Internet Banking System", $tags="Software System") {
                  Container_Boundary("InternetBankingSystem.APIApplication_boundary", "API Application", $tags="Container") {
                    Component(InternetBankingSystem.APIApplication.SignInController, "Sign In Controller", $techn="Spring MVC Rest Controller", $descr="Allows users to sign in to the Internet Banking System.", $tags="Component", $link="")
                    Component(InternetBankingSystem.APIApplication.AccountsSummaryController, "Accounts Summary Controller", $techn="Spring MVC Rest Controller", $descr="Provides customers with a summary of their bank accounts.", $tags="Component", $link="")
                    Component(InternetBankingSystem.APIApplication.ResetPasswordController, "Reset Password Controller", $techn="Spring MVC Rest Controller", $descr="Allows users to reset their passwords with a single use URL.", $tags="Component", $link="")
                    Component(InternetBankingSystem.APIApplication.SecurityComponent, "Security Component", $techn="Spring Bean", $descr="Provides functionality related to signing in, changing passwords, etc.", $tags="Component", $link="")
                    Component(InternetBankingSystem.APIApplication.MainframeBankingSystemFacade, "Mainframe Banking System Facade", $techn="Spring Bean", $descr="A facade onto the mainframe banking system.", $tags="Component", $link="")
                    Component(InternetBankingSystem.APIApplication.EmailComponent, "E-mail Component", $techn="Spring Bean", $descr="Sends e-mails to users.", $tags="Component", $link="")
                  }
                
                  ContainerDb(InternetBankingSystem.Database, "Database", $techn="Oracle Database Schema", $descr="Stores user registration information, hashed authentication credentials, access logs, etc.", $tags="Container,Database", $link="")
                  Container(InternetBankingSystem.SinglePageApplication, "Single-Page Application", $techn="JavaScript and Angular", $descr="Provides all of the Internet banking functionality to customers via their web browser.", $tags="Container,Web Browser", $link="")
                  Container(InternetBankingSystem.MobileApp, "Mobile App", $techn="Xamarin", $descr="Provides a limited subset of the Internet banking functionality to customers via their mobile device.", $tags="Container,Mobile App", $link="")
                }
                
                Rel(InternetBankingSystem.SinglePageApplication, InternetBankingSystem.APIApplication.SignInController, "Makes API calls to", $techn="JSON/HTTPS", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.SinglePageApplication, InternetBankingSystem.APIApplication.AccountsSummaryController, "Makes API calls to", $techn="JSON/HTTPS", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.SinglePageApplication, InternetBankingSystem.APIApplication.ResetPasswordController, "Makes API calls to", $techn="JSON/HTTPS", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.MobileApp, InternetBankingSystem.APIApplication.SignInController, "Makes API calls to", $techn="JSON/HTTPS", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.MobileApp, InternetBankingSystem.APIApplication.AccountsSummaryController, "Makes API calls to", $techn="JSON/HTTPS", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.MobileApp, InternetBankingSystem.APIApplication.ResetPasswordController, "Makes API calls to", $techn="JSON/HTTPS", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.APIApplication.SignInController, InternetBankingSystem.APIApplication.SecurityComponent, "Uses", $techn="", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.APIApplication.AccountsSummaryController, InternetBankingSystem.APIApplication.MainframeBankingSystemFacade, "Uses", $techn="", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.APIApplication.ResetPasswordController, InternetBankingSystem.APIApplication.SecurityComponent, "Uses", $techn="", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.APIApplication.ResetPasswordController, InternetBankingSystem.APIApplication.EmailComponent, "Uses", $techn="", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.APIApplication.SecurityComponent, InternetBankingSystem.Database, "Reads from and writes to", $techn="SQL/TCP", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.APIApplication.MainframeBankingSystemFacade, MainframeBankingSystem, "Makes API calls to", $techn="XML/HTTPS", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.APIApplication.EmailComponent, EmailSystem, "Sends e-mail using", $techn="", $tags="Relationship", $link="")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());

        diagram = diagrams.stream().filter(d -> d.getKey().equals("SignIn")).findFirst().get();
        assertEquals("""
                @startuml
                title <size:24>Dynamic View: Internet Banking System - API Application</size>\\n<size:24>Summarises how the sign in feature works in the single-page application.</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                !include <C4/C4_Container>
                !include <C4/C4_Component>
                
                AddElementTag("Component", $bgColor="#85bbf0", $borderColor="#5d82a8", $fontColor="#000000", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Container,Database", $bgColor="#438dd5", $borderColor="#2e6295", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Container,Web Browser", $bgColor="#438dd5", $borderColor="#2e6295", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                
                AddRelTag("Relationship", $textColor="#444444", $lineColor="#444444", $lineStyle = DashedLine())
                
                AddBoundaryTag("Container", $bgColor="#ffffff", $borderColor="#2e6295", $fontColor="#2e6295", $shadowing="", $borderStyle="solid")
                
                System_Boundary("InternetBankingSystem_boundary", "Internet Banking System", $tags="Software System") {
                  Container_Boundary("InternetBankingSystem.APIApplication_boundary", "API Application", $tags="Container") {
                    Component(InternetBankingSystem.APIApplication.SignInController, "Sign In Controller", $techn="Spring MVC Rest Controller", $descr="Allows users to sign in to the Internet Banking System.", $tags="Component", $link="")
                    Component(InternetBankingSystem.APIApplication.SecurityComponent, "Security Component", $techn="Spring Bean", $descr="Provides functionality related to signing in, changing passwords, etc.", $tags="Component", $link="")
                  }
                
                  ContainerDb(InternetBankingSystem.Database, "Database", $techn="Oracle Database Schema", $descr="Stores user registration information, hashed authentication credentials, access logs, etc.", $tags="Container,Database", $link="")
                  Container(InternetBankingSystem.SinglePageApplication, "Single-Page Application", $techn="JavaScript and Angular", $descr="Provides all of the Internet banking functionality to customers via their web browser.", $tags="Container,Web Browser", $link="")
                }
                
                Container(InternetBankingSystem.SinglePageApplication, "Single-Page Application", $techn="JavaScript and Angular", $descr="Provides all of the Internet banking functionality to customers via their web browser.", $tags="Container,Web Browser", $link="")
                ContainerDb(InternetBankingSystem.Database, "Database", $techn="Oracle Database Schema", $descr="Stores user registration information, hashed authentication credentials, access logs, etc.", $tags="Container,Database", $link="")
                
                Rel(InternetBankingSystem.SinglePageApplication, InternetBankingSystem.APIApplication.SignInController, "1: Submits credentials to", $techn="JSON/HTTPS", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.APIApplication.SignInController, InternetBankingSystem.APIApplication.SecurityComponent, "2: Validates credentials using", $techn="", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.APIApplication.SecurityComponent, InternetBankingSystem.Database, "3: select * from users where username = ?", $techn="SQL/TCP", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.Database, InternetBankingSystem.APIApplication.SecurityComponent, "4: Returns user data to", $techn="SQL/TCP", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.APIApplication.SecurityComponent, InternetBankingSystem.APIApplication.SignInController, "5: Returns true if the hashed password matches", $techn="", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.APIApplication.SignInController, InternetBankingSystem.SinglePageApplication, "6: Sends back an authentication token to", $techn="JSON/HTTPS", $tags="Relationship", $link="")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("DevelopmentDeployment")).findFirst().get();
        assertEquals("""
                @startuml
                title <size:24>Deployment View: Internet Banking System - Development</size>\\n<size:24>An example development deployment scenario for the Internet Banking System.</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                !include <C4/C4_Container>
                !include <C4/C4_Deployment>
                
                AddElementTag("Software System,Existing System", $bgColor="#999999", $borderColor="#6b6b6b", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Element", $bgColor="#ffffff", $borderColor="#444444", $fontColor="#444444", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Container,Database", $bgColor="#438dd5", $borderColor="#2e6295", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Container,Web Browser", $bgColor="#438dd5", $borderColor="#2e6295", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Container", $bgColor="#438dd5", $borderColor="#2e6295", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                
                AddRelTag("Relationship", $textColor="#444444", $lineColor="#444444", $lineStyle = DashedLine())
                
                Deployment_Node(Development.DeveloperLaptop, "Developer Laptop", $type="Microsoft Windows 10 or Apple macOS", $descr="", $tags="Element", $link="") {
                  Deployment_Node(Development.DeveloperLaptop.WebBrowser, "Web Browser", $type="Chrome, Firefox, Safari, or Edge", $descr="", $tags="Element", $link="") {
                    Container(Development.DeveloperLaptop.WebBrowser.SinglePageApplication_1, "Single-Page Application", $techn="JavaScript and Angular", $descr="Provides all of the Internet banking functionality to customers via their web browser.", $tags="Container,Web Browser", $link="")
                  }
                
                  Deployment_Node(Development.DeveloperLaptop.DockerContainerWebServer, "Docker Container - Web Server", $type="Docker", $descr="", $tags="Element", $link="") {
                    Deployment_Node(Development.DeveloperLaptop.DockerContainerWebServer.ApacheTomcat, "Apache Tomcat", $type="Apache Tomcat 8.x", $descr="", $tags="Element", $link="") {
                      Container(Development.DeveloperLaptop.DockerContainerWebServer.ApacheTomcat.WebApplication_1, "Web Application", $techn="Java and Spring MVC", $descr="Delivers the static content and the Internet banking single page application.", $tags="Container", $link="")
                      Container(Development.DeveloperLaptop.DockerContainerWebServer.ApacheTomcat.APIApplication_1, "API Application", $techn="Java and Spring MVC", $descr="Provides Internet banking functionality via a JSON/HTTPS API.", $tags="Container", $link="")
                    }
                
                  }
                
                  Deployment_Node(Development.DeveloperLaptop.DockerContainerDatabaseServer, "Docker Container - Database Server", $type="Docker", $descr="", $tags="Element", $link="") {
                    Deployment_Node(Development.DeveloperLaptop.DockerContainerDatabaseServer.DatabaseServer, "Database Server", $type="Oracle 12c", $descr="", $tags="Element", $link="") {
                      ContainerDb(Development.DeveloperLaptop.DockerContainerDatabaseServer.DatabaseServer.Database_1, "Database", $techn="Oracle Database Schema", $descr="Stores user registration information, hashed authentication credentials, access logs, etc.", $tags="Container,Database", $link="")
                    }
                
                  }
                
                }
                
                Deployment_Node(Development.BigBankplc, "Big Bank plc", $type="Big Bank plc data center", $descr="", $tags="Element", $link="") {
                  Deployment_Node(Development.BigBankplc.bigbankdev001, "bigbank-dev001", $type="", $descr="", $tags="Element", $link="") {
                    System(Development.BigBankplc.bigbankdev001.MainframeBankingSystem_1, "Mainframe Banking System", $descr="Stores all of the core banking information about customers, accounts, transactions, etc.", $tags="Software System,Existing System", $link="")
                  }
                
                }
                
                Rel(Development.DeveloperLaptop.DockerContainerWebServer.ApacheTomcat.WebApplication_1, Development.DeveloperLaptop.WebBrowser.SinglePageApplication_1, "Delivers to the customer's web browser", $techn="", $tags="Relationship", $link="")
                Rel(Development.DeveloperLaptop.WebBrowser.SinglePageApplication_1, Development.DeveloperLaptop.DockerContainerWebServer.ApacheTomcat.APIApplication_1, "Makes API calls to", $techn="JSON/HTTPS", $tags="Relationship", $link="")
                Rel(Development.DeveloperLaptop.DockerContainerWebServer.ApacheTomcat.APIApplication_1, Development.DeveloperLaptop.DockerContainerDatabaseServer.DatabaseServer.Database_1, "Reads from and writes to", $techn="SQL/TCP", $tags="Relationship", $link="")
                Rel(Development.DeveloperLaptop.DockerContainerWebServer.ApacheTomcat.APIApplication_1, Development.BigBankplc.bigbankdev001.MainframeBankingSystem_1, "Makes API calls to", $techn="XML/HTTPS", $tags="Relationship", $link="")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("LiveDeployment")).findFirst().get();
        assertEquals("""
                @startuml
                title <size:24>Deployment View: Internet Banking System - Live</size>\\n<size:24>An example live deployment scenario for the Internet Banking System.</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                !include <C4/C4_Container>
                !include <C4/C4_Deployment>
                
                AddElementTag("Failover", $bgColor="#ffffff", $borderColor="#444444", $fontColor="#444444", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Software System,Existing System", $bgColor="#999999", $borderColor="#6b6b6b", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Element", $bgColor="#ffffff", $borderColor="#444444", $fontColor="#444444", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Container,Mobile App", $bgColor="#438dd5", $borderColor="#2e6295", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Container,Database", $bgColor="#438dd5", $borderColor="#2e6295", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Container,Web Browser", $bgColor="#438dd5", $borderColor="#2e6295", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Container", $bgColor="#438dd5", $borderColor="#2e6295", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                
                AddRelTag("Relationship", $textColor="#444444", $lineColor="#444444", $lineStyle = DashedLine())
                
                Deployment_Node(Live.Customersmobiledevice, "Customer's mobile device", $type="Apple iOS or Android", $descr="", $tags="Element", $link="") {
                  Container(Live.Customersmobiledevice.MobileApp_1, "Mobile App", $techn="Xamarin", $descr="Provides a limited subset of the Internet banking functionality to customers via their mobile device.", $tags="Container,Mobile App", $link="")
                }
                
                Deployment_Node(Live.Customerscomputer, "Customer's computer", $type="Microsoft Windows or Apple macOS", $descr="", $tags="Element", $link="") {
                  Deployment_Node(Live.Customerscomputer.WebBrowser, "Web Browser", $type="Chrome, Firefox, Safari, or Edge", $descr="", $tags="Element", $link="") {
                    Container(Live.Customerscomputer.WebBrowser.SinglePageApplication_1, "Single-Page Application", $techn="JavaScript and Angular", $descr="Provides all of the Internet banking functionality to customers via their web browser.", $tags="Container,Web Browser", $link="")
                  }
                
                }
                
                Deployment_Node(Live.BigBankplc, "Big Bank plc", $type="Big Bank plc data center", $descr="", $tags="Element", $link="") {
                  Deployment_Node(Live.BigBankplc.bigbankweb, "bigbank-web*** (x4)", $type="Ubuntu 16.04 LTS", $descr="", $tags="Element", $link="") {
                    Deployment_Node(Live.BigBankplc.bigbankweb.ApacheTomcat, "Apache Tomcat", $type="Apache Tomcat 8.x", $descr="", $tags="Element", $link="") {
                      Container(Live.BigBankplc.bigbankweb.ApacheTomcat.WebApplication_1, "Web Application", $techn="Java and Spring MVC", $descr="Delivers the static content and the Internet banking single page application.", $tags="Container", $link="")
                    }
                
                  }
                
                  Deployment_Node(Live.BigBankplc.bigbankapi, "bigbank-api*** (x8)", $type="Ubuntu 16.04 LTS", $descr="", $tags="Element", $link="") {
                    Deployment_Node(Live.BigBankplc.bigbankapi.ApacheTomcat, "Apache Tomcat", $type="Apache Tomcat 8.x", $descr="", $tags="Element", $link="") {
                      Container(Live.BigBankplc.bigbankapi.ApacheTomcat.APIApplication_1, "API Application", $techn="Java and Spring MVC", $descr="Provides Internet banking functionality via a JSON/HTTPS API.", $tags="Container", $link="")
                    }
                
                  }
                
                  Deployment_Node(Live.BigBankplc.bigbankdb01, "bigbank-db01", $type="Ubuntu 16.04 LTS", $descr="", $tags="Element", $link="") {
                    Deployment_Node(Live.BigBankplc.bigbankdb01.OraclePrimary, "Oracle - Primary", $type="Oracle 12c", $descr="", $tags="Element", $link="") {
                      ContainerDb(Live.BigBankplc.bigbankdb01.OraclePrimary.Database_1, "Database", $techn="Oracle Database Schema", $descr="Stores user registration information, hashed authentication credentials, access logs, etc.", $tags="Container,Database", $link="")
                    }
                
                  }
                
                  Deployment_Node(Live.BigBankplc.bigbankdb02, "bigbank-db02", $type="Ubuntu 16.04 LTS", $descr="", $tags="Failover", $link="") {
                    Deployment_Node(Live.BigBankplc.bigbankdb02.OracleSecondary, "Oracle - Secondary", $type="Oracle 12c", $descr="", $tags="Failover", $link="") {
                      ContainerDb(Live.BigBankplc.bigbankdb02.OracleSecondary.Database_1, "Database", $techn="Oracle Database Schema", $descr="Stores user registration information, hashed authentication credentials, access logs, etc.", $tags="Container,Database", $link="")
                    }
                
                  }
                
                  Deployment_Node(Live.BigBankplc.bigbankprod001, "bigbank-prod001", $type="", $descr="", $tags="Element", $link="") {
                    System(Live.BigBankplc.bigbankprod001.MainframeBankingSystem_1, "Mainframe Banking System", $descr="Stores all of the core banking information about customers, accounts, transactions, etc.", $tags="Software System,Existing System", $link="")
                  }
                
                }
                
                Rel(Live.BigBankplc.bigbankweb.ApacheTomcat.WebApplication_1, Live.Customerscomputer.WebBrowser.SinglePageApplication_1, "Delivers to the customer's web browser", $techn="", $tags="Relationship", $link="")
                Rel(Live.Customersmobiledevice.MobileApp_1, Live.BigBankplc.bigbankapi.ApacheTomcat.APIApplication_1, "Makes API calls to", $techn="JSON/HTTPS", $tags="Relationship", $link="")
                Rel(Live.Customerscomputer.WebBrowser.SinglePageApplication_1, Live.BigBankplc.bigbankapi.ApacheTomcat.APIApplication_1, "Makes API calls to", $techn="JSON/HTTPS", $tags="Relationship", $link="")
                Rel(Live.BigBankplc.bigbankapi.ApacheTomcat.APIApplication_1, Live.BigBankplc.bigbankdb01.OraclePrimary.Database_1, "Reads from and writes to", $techn="SQL/TCP", $tags="Relationship", $link="")
                Rel(Live.BigBankplc.bigbankapi.ApacheTomcat.APIApplication_1, Live.BigBankplc.bigbankdb02.OracleSecondary.Database_1, "Reads from and writes to", $techn="SQL/TCP", $tags="Relationship", $link="")
                Rel(Live.BigBankplc.bigbankapi.ApacheTomcat.APIApplication_1, Live.BigBankplc.bigbankprod001.MainframeBankingSystem_1, "Makes API calls to", $techn="XML/HTTPS", $tags="Relationship", $link="")
                Rel(Live.BigBankplc.bigbankdb01.OraclePrimary, Live.BigBankplc.bigbankdb02.OracleSecondary, "Replicates data to", $techn="", $tags="Relationship", $link="")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());

        // and the sequence diagram version
        workspace.getViews().getConfiguration().addProperty(exporter.PLANTUML_SEQUENCE_DIAGRAM_PROPERTY, "true");
        diagrams = exporter.export(workspace);
        diagram = diagrams.stream().filter(d -> d.getKey().equals("SignIn")).findFirst().get();
        assertEquals("""
                @startuml
                title <size:24>Dynamic View: Internet Banking System - API Application</size>\\n<size:24>Summarises how the sign in feature works in the single-page application.</size>
                
                set separator none
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4_Sequence>
                
                AddElementTag("Component", $bgColor="#85bbf0", $borderColor="#5d82a8", $fontColor="#000000", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Container,Database", $bgColor="#438dd5", $borderColor="#2e6295", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Container,Web Browser", $bgColor="#438dd5", $borderColor="#2e6295", $fontColor="#ffffff", $sprite="", $shadowing="", $borderStyle="solid")
                
                AddRelTag("Relationship", $textColor="#444444", $lineColor="#444444", $lineStyle = DashedLine())
                
                Container(InternetBankingSystem.SinglePageApplication, "Single-Page Application", $techn="JavaScript and Angular", $descr="Provides all of the Internet banking functionality to customers via their web browser.", $tags="Container,Web Browser", $link="")
                Component(InternetBankingSystem.APIApplication.SignInController, "Sign In Controller", $techn="Spring MVC Rest Controller", $descr="Allows users to sign in to the Internet Banking System.", $tags="Component", $link="")
                Component(InternetBankingSystem.APIApplication.SecurityComponent, "Security Component", $techn="Spring Bean", $descr="Provides functionality related to signing in, changing passwords, etc.", $tags="Component", $link="")
                ContainerDb(InternetBankingSystem.Database, "Database", $techn="Oracle Database Schema", $descr="Stores user registration information, hashed authentication credentials, access logs, etc.", $tags="Container,Database", $link="")
                
                Rel(InternetBankingSystem.SinglePageApplication, InternetBankingSystem.APIApplication.SignInController, "1: Submits credentials to", $techn="JSON/HTTPS", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.APIApplication.SignInController, InternetBankingSystem.APIApplication.SecurityComponent, "2: Validates credentials using", $techn="", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.APIApplication.SecurityComponent, InternetBankingSystem.Database, "3: select * from users where username = ?", $techn="SQL/TCP", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.Database, InternetBankingSystem.APIApplication.SecurityComponent, "4: Returns user data to", $techn="SQL/TCP", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.APIApplication.SecurityComponent, InternetBankingSystem.APIApplication.SignInController, "5: Returns true if the hashed password matches", $techn="", $tags="Relationship", $link="")
                Rel(InternetBankingSystem.APIApplication.SignInController, InternetBankingSystem.SinglePageApplication, "6: Sends back an authentication token to", $techn="JSON/HTTPS", $tags="Relationship", $link="")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());
    }

    @Test
    @Tag("IntegrationTest")
    public void test_AmazonWebServicesExampleWithoutTags() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/amazon-web-services.json"));
        HttpClient httpClient = new HttpClient();
        httpClient.allow(".*");
        ThemeUtils.loadThemes(workspace, httpClient);

        workspace.getViews().getDeploymentViews().iterator().next().enableAutomaticLayout(AutomaticLayout.RankDirection.LeftRight, 300, 300);
        workspace.getViews().getViews().forEach(v -> v.addProperty(C4PlantUMLExporter.C4PLANTUML_TAGS_PROPERTY, "false"));

        C4PlantUMLExporter exporter = new C4PlantUMLExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(1, diagrams.size());

        Diagram diagram = diagrams.stream().findFirst().get();
        assertEquals("""
                @startuml
                title <size:24>Deployment View: X - Live</size>
                
                set separator none
                left to right direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                !include <C4/C4_Container>
                !include <C4/C4_Deployment>
                
                Deployment_Node(Live.AmazonWebServices, "Amazon Web Services", $type="", $descr="", $tags="", $link="") {
                  Deployment_Node(Live.AmazonWebServices.USEast1, "US-East-1", $type="", $descr="", $tags="", $link="") {
                    Deployment_Node(Live.AmazonWebServices.USEast1.Autoscalinggroup, "Autoscaling group", $type="", $descr="", $tags="", $link="") {
                      Deployment_Node(Live.AmazonWebServices.USEast1.Autoscalinggroup.AmazonEC2Ubuntuserver, "Amazon EC2 - Ubuntu server", $type="", $descr="", $tags="", $link="") {
                        Container(Live.AmazonWebServices.USEast1.Autoscalinggroup.AmazonEC2Ubuntuserver.WebApplication_1, "Web Application", $techn="Java and Spring Boot", $descr="", $tags="", $link="")
                      }
                
                    }
                
                    Deployment_Node(Live.AmazonWebServices.USEast1.AmazonRDS, "Amazon RDS", $type="", $descr="", $tags="", $link="") {
                      Deployment_Node(Live.AmazonWebServices.USEast1.AmazonRDS.MySQL, "MySQL", $type="", $descr="", $tags="", $link="") {
                        ContainerDb(Live.AmazonWebServices.USEast1.AmazonRDS.MySQL.DatabaseSchema_1, "Database Schema", $techn="", $descr="", $tags="", $link="")
                      }
                
                    }
                
                    Deployment_Node(Live.AmazonWebServices.USEast1.DNSrouter, "DNS router", $type="Route 53", $descr="Routes incoming requests based upon domain name.", $tags="", $link="")
                    Deployment_Node(Live.AmazonWebServices.USEast1.LoadBalancer, "Load Balancer", $type="Elastic Load Balancer", $descr="Automatically distributes incoming application traffic.", $tags="", $link="")
                  }
                
                }
                
                Rel(Live.AmazonWebServices.USEast1.LoadBalancer, Live.AmazonWebServices.USEast1.Autoscalinggroup.AmazonEC2Ubuntuserver.WebApplication_1, "Forwards requests to", $techn="HTTPS", $tags="", $link="")
                Rel(Live.AmazonWebServices.USEast1.Autoscalinggroup.AmazonEC2Ubuntuserver.WebApplication_1, Live.AmazonWebServices.USEast1.AmazonRDS.MySQL.DatabaseSchema_1, "Reads from and writes to", $techn="MySQL Protocol/SSL", $tags="", $link="")
                Rel(Live.AmazonWebServices.USEast1.DNSrouter, Live.AmazonWebServices.USEast1.LoadBalancer, "Forwards requests to", $techn="HTTPS", $tags="", $link="")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());
    }

    @Test
    @Tag("IntegrationTest")
    public void test_AmazonWebServicesExampleWithTags() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/amazon-web-services.json"));
        HttpClient httpClient = new HttpClient();
        httpClient.allow(".*");
        ThemeUtils.loadThemes(workspace, httpClient);

        workspace.getViews().getDeploymentViews().iterator().next().enableAutomaticLayout(AutomaticLayout.RankDirection.LeftRight, 300, 300);
        workspace.getViews().getConfiguration().addProperty(C4PlantUMLExporter.C4PLANTUML_TAGS_PROPERTY, "true");

        C4PlantUMLExporter exporter = new C4PlantUMLExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(1, diagrams.size());

        Diagram diagram = diagrams.stream().findFirst().get();
        assertEquals("""
                @startuml
                title <size:24>Deployment View: X - Live</size>
                
                set separator none
                left to right direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                !include <C4/C4_Container>
                !include <C4/C4_Deployment>
                
                AddElementTag("Amazon Web Services - RDS", $bgColor="#ffffff", $borderColor="#3b48cc", $fontColor="#3b48cc", $sprite="img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/amazon-rds.png{scale=0.1}", $shadowing="", $borderStyle="solid")
                AddElementTag("Amazon Web Services - Auto Scaling", $bgColor="#ffffff", $borderColor="#cc2264", $fontColor="#cc2264", $sprite="img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/aws-auto-scaling.png{scale=0.1}", $shadowing="", $borderStyle="solid")
                AddElementTag("Amazon Web Services - Route 53", $bgColor="#ffffff", $borderColor="#693cc5", $fontColor="#693cc5", $sprite="img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/amazon-route-53.png{scale=0.1}", $shadowing="", $borderStyle="solid")
                AddElementTag("Amazon Web Services - EC2", $bgColor="#ffffff", $borderColor="#d86613", $fontColor="#d86613", $sprite="img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/amazon-ec2.png{scale=0.1}", $shadowing="", $borderStyle="solid")
                AddElementTag("Amazon Web Services - Region", $bgColor="#ffffff", $borderColor="#147eba", $fontColor="#147eba", $sprite="img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/region.png{scale=0.21428571428571427}", $shadowing="", $borderStyle="solid")
                AddElementTag("Amazon Web Services - Elastic Load Balancing", $bgColor="#ffffff", $borderColor="#693cc5", $fontColor="#693cc5", $sprite="img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/elastic-load-balancing.png{scale=0.1}", $shadowing="", $borderStyle="solid")
                AddElementTag("Application", $bgColor="#ffffff", $borderColor="#444444", $fontColor="#444444", $sprite="", $shadowing="", $borderStyle="solid")
                AddElementTag("Amazon Web Services - RDS MySQL instance", $bgColor="#ffffff", $borderColor="#3b48cc", $fontColor="#3b48cc", $sprite="img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/amazon-rds-mysql-instance.png{scale=0.15}", $shadowing="", $borderStyle="solid")
                AddElementTag("Amazon Web Services - Cloud", $bgColor="#ffffff", $borderColor="#232f3e", $fontColor="#232f3e", $sprite="img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/aws-cloud.png{scale=0.21428571428571427}", $shadowing="", $borderStyle="solid")
                AddElementTag("Database", $bgColor="#ffffff", $borderColor="#444444", $fontColor="#444444", $sprite="", $shadowing="", $borderStyle="solid")
                
                AddRelTag("Relationship", $textColor="#444444", $lineColor="#444444", $lineStyle = DashedLine())
                
                Deployment_Node(Live.AmazonWebServices, "Amazon Web Services", $type="", $descr="", $tags="Amazon Web Services - Cloud", $link="") {
                  Deployment_Node(Live.AmazonWebServices.USEast1, "US-East-1", $type="", $descr="", $tags="Amazon Web Services - Region", $link="") {
                    Deployment_Node(Live.AmazonWebServices.USEast1.Autoscalinggroup, "Autoscaling group", $type="", $descr="", $tags="Amazon Web Services - Auto Scaling", $link="") {
                      Deployment_Node(Live.AmazonWebServices.USEast1.Autoscalinggroup.AmazonEC2Ubuntuserver, "Amazon EC2 - Ubuntu server", $type="", $descr="", $tags="Amazon Web Services - EC2", $link="") {
                        Container(Live.AmazonWebServices.USEast1.Autoscalinggroup.AmazonEC2Ubuntuserver.WebApplication_1, "Web Application", $techn="Java and Spring Boot", $descr="", $tags="Application", $link="")
                      }
                
                    }
                
                    Deployment_Node(Live.AmazonWebServices.USEast1.AmazonRDS, "Amazon RDS", $type="", $descr="", $tags="Amazon Web Services - RDS", $link="") {
                      Deployment_Node(Live.AmazonWebServices.USEast1.AmazonRDS.MySQL, "MySQL", $type="", $descr="", $tags="Amazon Web Services - RDS MySQL instance", $link="") {
                        ContainerDb(Live.AmazonWebServices.USEast1.AmazonRDS.MySQL.DatabaseSchema_1, "Database Schema", $techn="", $descr="", $tags="Database", $link="")
                      }
                
                    }
                
                    Deployment_Node(Live.AmazonWebServices.USEast1.DNSrouter, "DNS router", $type="Route 53", $descr="Routes incoming requests based upon domain name.", $tags="Amazon Web Services - Route 53", $link="")
                    Deployment_Node(Live.AmazonWebServices.USEast1.LoadBalancer, "Load Balancer", $type="Elastic Load Balancer", $descr="Automatically distributes incoming application traffic.", $tags="Amazon Web Services - Elastic Load Balancing", $link="")
                  }
                
                }
                
                Rel(Live.AmazonWebServices.USEast1.LoadBalancer, Live.AmazonWebServices.USEast1.Autoscalinggroup.AmazonEC2Ubuntuserver.WebApplication_1, "Forwards requests to", $techn="HTTPS", $tags="Relationship", $link="")
                Rel(Live.AmazonWebServices.USEast1.Autoscalinggroup.AmazonEC2Ubuntuserver.WebApplication_1, Live.AmazonWebServices.USEast1.AmazonRDS.MySQL.DatabaseSchema_1, "Reads from and writes to", $techn="MySQL Protocol/SSL", $tags="Relationship", $link="")
                Rel(Live.AmazonWebServices.USEast1.DNSrouter, Live.AmazonWebServices.USEast1.LoadBalancer, "Forwards requests to", $techn="HTTPS", $tags="Relationship", $link="")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void test_GroupsExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/groups.json"));

        C4PlantUMLExporter exporter = new C4PlantUMLExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(3, diagrams.size());

        Diagram diagram = diagrams.stream().filter(md -> md.getKey().equals("SystemLandscape")).findFirst().get();
        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                
                AddBoundaryTag("Group 1", $borderColor="#cccccc", $fontColor="#cccccc", $borderStyle="dashed")
                Boundary(group_1, "Group 1", $tags="Group 1") {
                  System(B, "B", $descr="", $tags="", $link="")
                }
                
                AddBoundaryTag("Group 2", $borderColor="#cccccc", $fontColor="#cccccc", $borderStyle="dashed")
                Boundary(group_2, "Group 2", $tags="Group 2") {
                  System(C, "C", $descr="", $tags="", $link="")
                    AddBoundaryTag("Group 2/Group 3", $borderColor="#cccccc", $fontColor="#cccccc", $borderStyle="dashed")
                    Boundary(group_3, "Group 3", $tags="Group 2/Group 3") {
                      System(D, "D", $descr="", $tags="", $link="")
                    }
                
                }
                
                System(A, "A", $descr="", $tags="", $link="")
                
                Rel(B, C, "", $techn="", $tags="", $link="")
                Rel(C, D, "", $techn="", $tags="", $link="")
                Rel(A, B, "", $techn="", $tags="", $link="")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Containers")).findFirst().get();
        assertEquals("""
                @startuml
                title <size:24>Container View: D</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                !include <C4/C4_Container>
                
                System(C, "C", $descr="", $tags="", $link="")
                
                System_Boundary("D_boundary", "D", $tags="") {
                  AddBoundaryTag("Group 4", $borderColor="#cccccc", $fontColor="#cccccc", $borderStyle="dashed")
                  Boundary(group_1, "Group 4", $tags="Group 4") {
                    Container(D.F, "F", $techn="", $descr="", $tags="", $link="")
                  }
                
                  Container(D.E, "E", $techn="", $descr="", $tags="", $link="")
                }
                
                Rel(C, D.E, "", $techn="", $tags="", $link="")
                Rel(C, D.F, "", $techn="", $tags="", $link="")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Components")).findFirst().get();
        assertEquals("""
                @startuml
                title <size:24>Component View: D - F</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                !include <C4/C4_Component>
                
                System(C, "C", $descr="", $tags="", $link="")
                
                System_Boundary("D_boundary", "D", $tags="") {
                  Container_Boundary("D.F_boundary", "F", $tags="") {
                    AddBoundaryTag("Group 5", $borderColor="#cccccc", $fontColor="#cccccc", $borderStyle="dashed")
                    Boundary(group_1, "Group 5", $tags="Group 5") {
                      Component(D.F.H, "H", $techn="", $descr="", $tags="", $link="")
                    }
                
                    Component(D.F.G, "G", $techn="", $descr="", $tags="", $link="")
                  }
                
                }
                
                Rel(C, D.F.G, "", $techn="", $tags="", $link="")
                Rel(C, D.F.H, "", $techn="", $tags="", $link="")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void test_NestedGroupsExample() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addProperty("structurizr.groupSeparator", "/");

        SoftwareSystem a = workspace.getModel().addSoftwareSystem("Team 1");
        a.setGroup("Organisation 1/Department 1/Team 1");

        SoftwareSystem b = workspace.getModel().addSoftwareSystem("Team 2");
        b.setGroup("Organisation 1/Department 1/Team 2");

        SoftwareSystem c = workspace.getModel().addSoftwareSystem("Organisation 1");
        c.setGroup("Organisation 1");

        SoftwareSystem d = workspace.getModel().addSoftwareSystem("Organisation 2");
        d.setGroup("Organisation 2");

        SoftwareSystem e = workspace.getModel().addSoftwareSystem("Department 1");
        e.setGroup("Organisation 1/Department 1");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("SystemLandscape", "Description");
        view.addAllElements();

        C4PlantUMLExporter exporter = new C4PlantUMLExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);

        Diagram diagram = diagrams.stream().filter(md -> md.getKey().equals("SystemLandscape")).findFirst().get();
        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>\\n<size:24>Description</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                
                AddBoundaryTag("Organisation 1", $borderColor="#cccccc", $fontColor="#cccccc", $borderStyle="dashed")
                Boundary(group_1, "Organisation 1", $tags="Organisation 1") {
                  System(Organisation1, "Organisation 1", $descr="", $tags="", $link="")
                    AddBoundaryTag("Organisation 1/Department 1", $borderColor="#cccccc", $fontColor="#cccccc", $borderStyle="dashed")
                    Boundary(group_2, "Department 1", $tags="Organisation 1/Department 1") {
                      System(Department1, "Department 1", $descr="", $tags="", $link="")
                        AddBoundaryTag("Organisation 1/Department 1/Team 1", $borderColor="#cccccc", $fontColor="#cccccc", $borderStyle="dashed")
                        Boundary(group_3, "Team 1", $tags="Organisation 1/Department 1/Team 1") {
                          System(Team1, "Team 1", $descr="", $tags="", $link="")
                        }
                
                        AddBoundaryTag("Organisation 1/Department 1/Team 2", $borderColor="#cccccc", $fontColor="#cccccc", $borderStyle="dashed")
                        Boundary(group_4, "Team 2", $tags="Organisation 1/Department 1/Team 2") {
                          System(Team2, "Team 2", $descr="", $tags="", $link="")
                        }
                
                    }
                
                }
                
                AddBoundaryTag("Organisation 2", $borderColor="#cccccc", $fontColor="#cccccc", $borderStyle="dashed")
                Boundary(group_5, "Organisation 2", $tags="Organisation 2") {
                  System(Organisation2, "Organisation 2", $descr="", $tags="", $link="")
                }
                
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void test_NestedGroupsExample_WithDotAsGroupSeparator() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addProperty("structurizr.groupSeparator", ".");

        SoftwareSystem a = workspace.getModel().addSoftwareSystem("Team 1");
        a.setGroup("Organisation 1.Department 1.Team 1");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("SystemLandscape", "Description");
        view.addAllElements();

        C4PlantUMLExporter exporter = new C4PlantUMLExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);

        Diagram diagram = diagrams.stream().filter(md -> md.getKey().equals("SystemLandscape")).findFirst().get();
        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>\\n<size:24>Description</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                
                AddBoundaryTag("Organisation 1", $borderColor="#cccccc", $fontColor="#cccccc", $borderStyle="dashed")
                Boundary(group_1, "Organisation 1", $tags="Organisation 1") {
                    AddBoundaryTag("Organisation 1.Department 1", $borderColor="#cccccc", $fontColor="#cccccc", $borderStyle="dashed")
                    Boundary(group_2, "Department 1", $tags="Organisation 1.Department 1") {
                        AddBoundaryTag("Organisation 1.Department 1.Team 1", $borderColor="#cccccc", $fontColor="#cccccc", $borderStyle="dashed")
                        Boundary(group_3, "Team 1", $tags="Organisation 1.Department 1.Team 1") {
                          System(Team1, "Team 1", $descr="", $tags="", $link="")
                        }
                
                    }
                
                }
                
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void test_renderGroupStyles() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addPerson("User 1").setGroup("Group 1");
        workspace.getModel().addPerson("User 2").setGroup("Group 2");
        workspace.getModel().addPerson("User 3").setGroup("Group 3");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "");
        view.addDefaultElements();

        workspace.getViews().getConfiguration().getStyles().addElementStyle("Group:Group 1").color("#111111").icon("https://example.com/icon1.png");
        workspace.getViews().getConfiguration().getStyles().addElementStyle("Group:Group 2").color("#222222").icon("https://example.com/icon2.png");

        C4PlantUMLExporter exporter = new C4PlantUMLExporter() {
            @Override
            protected double calculateIconScale(String iconUrl, int maxIconSize) {
                return 1.0;
            }
        };

        Diagram diagram = exporter.export(view);
        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                
                AddBoundaryTag("Group 1", $borderColor="#111111", $fontColor="#111111", $borderStyle="dashed")
                Boundary(group_1, "Group 1", $tags="Group 1") {
                  Person(User1, "User 1", $descr="", $tags="", $link="")
                }
                
                AddBoundaryTag("Group 2", $borderColor="#222222", $fontColor="#222222", $borderStyle="dashed")
                Boundary(group_2, "Group 2", $tags="Group 2") {
                  Person(User2, "User 2", $descr="", $tags="", $link="")
                }
                
                AddBoundaryTag("Group 3", $borderColor="#cccccc", $fontColor="#cccccc", $borderStyle="dashed")
                Boundary(group_3, "Group 3", $tags="Group 3") {
                  Person(User3, "User 3", $descr="", $tags="", $link="")
                }
                
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());

        workspace.getViews().getConfiguration().getStyles().addElementStyle("Group").color("#aabbcc");

        diagram = exporter.export(view);
        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                
                AddBoundaryTag("Group 1", $borderColor="#111111", $fontColor="#111111", $borderStyle="dashed")
                Boundary(group_1, "Group 1", $tags="Group 1") {
                  Person(User1, "User 1", $descr="", $tags="", $link="")
                }
                
                AddBoundaryTag("Group 2", $borderColor="#222222", $fontColor="#222222", $borderStyle="dashed")
                Boundary(group_2, "Group 2", $tags="Group 2") {
                  Person(User2, "User 2", $descr="", $tags="", $link="")
                }
                
                AddBoundaryTag("Group 3", $borderColor="#aabbcc", $fontColor="#aabbcc", $borderStyle="dashed")
                Boundary(group_3, "Group 3", $tags="Group 3") {
                  Person(User3, "User 3", $descr="", $tags="", $link="")
                }
                
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void test_renderContainerDiagramWithExternalContainers() {
        Workspace workspace = new Workspace("Name", "Description");

        SoftwareSystem softwareSystem1 = workspace.getModel().addSoftwareSystem("Software System 1");
        Container container1 = softwareSystem1.addContainer("Container 1");
        SoftwareSystem softwareSystem2 = workspace.getModel().addSoftwareSystem("Software System 2");
        Container container2 = softwareSystem2.addContainer("Container 2");

        container1.uses(container2, "Uses");

        ContainerView containerView = workspace.getViews().createContainerView(softwareSystem1, "Containers", "");
        containerView.add(container1);
        containerView.add(container2);

        Diagram diagram = new C4PlantUMLExporter().export(containerView);
        assertEquals("""
                @startuml
                title <size:24>Container View: Software System 1</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                !include <C4/C4_Container>
                
                System_Boundary("SoftwareSystem1_boundary", "Software System 1", $tags="") {
                  Container(SoftwareSystem1.Container1, "Container 1", $techn="", $descr="", $tags="", $link="")
                }
                
                System_Boundary("SoftwareSystem2_boundary", "Software System 2", $tags="") {
                  Container(SoftwareSystem2.Container2, "Container 2", $techn="", $descr="", $tags="", $link="")
                }
                
                Rel(SoftwareSystem1.Container1, SoftwareSystem2.Container2, "Uses", $techn="", $tags="", $link="")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void test_renderComponentDiagramWithExternalComponents() {
        Workspace workspace = new Workspace("Name", "Description");

        SoftwareSystem softwareSystem1 = workspace.getModel().addSoftwareSystem("Software System 1");
        Container container1 = softwareSystem1.addContainer("Container 1");
        Component component1 = container1.addComponent("Component 1");
        SoftwareSystem softwareSystem2 = workspace.getModel().addSoftwareSystem("Software System 2");
        Container container2 = softwareSystem2.addContainer("Container 2");
        Component component2 = container2.addComponent("Component 2");

        component1.uses(component2, "Uses");

        ComponentView componentView = workspace.getViews().createComponentView(container1, "Components", "");
        componentView.add(component1);
        componentView.add(component2);

        Diagram diagram = new C4PlantUMLExporter().export(componentView);
        assertEquals("""
                @startuml
                title <size:24>Component View: Software System 1 - Container 1</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                !include <C4/C4_Component>
                
                System_Boundary("SoftwareSystem1_boundary", "Software System 1", $tags="") {
                  Container_Boundary("SoftwareSystem1.Container1_boundary", "Container 1", $tags="") {
                    Component(SoftwareSystem1.Container1.Component1, "Component 1", $techn="", $descr="", $tags="", $link="")
                  }
                
                }
                
                System_Boundary("SoftwareSystem2_boundary", "Software System 2", $tags="") {
                  Container_Boundary("SoftwareSystem2.Container2_boundary", "Container 2", $tags="") {
                    Component(SoftwareSystem2.Container2.Component2, "Component 2", $techn="", $descr="", $tags="", $link="")
                  }
                
                }
                
                Rel(SoftwareSystem1.Container1.Component1, SoftwareSystem2.Container2.Component2, "Uses", $techn="", $tags="", $link="")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void test_renderDiagramWithElementUrls() {
        Workspace workspace = new Workspace("Name", "Description");

        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        softwareSystem.setUrl("https://structurizr.com");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.addDefaultElements();

        Diagram diagram = new C4PlantUMLExporter().export(view);
        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>\\n<size:24>Description</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                
                System(SoftwareSystem, "Software System", $descr="", $tags="", $link="https://structurizr.com")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void test_renderDiagramWithIncludes() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addSoftwareSystem("Software System");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.addDefaultElements();

        view.addProperty(C4PlantUMLExporter.PLANTUML_INCLUDES_PROPERTY, "styles.puml");

        Diagram diagram = new C4PlantUMLExporter().export(view);
        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>\\n<size:24>Description</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                
                !include styles.puml
                
                System(SoftwareSystem, "Software System", $descr="", $tags="", $link="")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void test_renderDiagramWithNewLineCharacterInElementName() {
        Workspace workspace = new Workspace("Name", "Description");

        workspace.getModel().addSoftwareSystem("Software\nSystem");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.addDefaultElements();

        Diagram diagram = new C4PlantUMLExporter().export(view);
        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>\\n<size:24>Description</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                
                System(SoftwareSystem, "Software\\nSystem", $descr="", $tags="", $link="")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void test_renderInfrastructureNodeWithTechnology() {
        Workspace workspace = new Workspace("Name", "Description");
        DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Deployment node");
        deploymentNode.addInfrastructureNode("Infrastructure node", "description", "technology");

        DeploymentView view = workspace.getViews().createDeploymentView("key", "view description");
        view.addDefaultElements();

        Diagram diagram = new C4PlantUMLExporter().export(view);
        assertEquals("""
                @startuml
                title <size:24>Deployment View: Default</size>\\n<size:24>view description</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                !include <C4/C4_Deployment>
                
                Deployment_Node(Default.Deploymentnode, "Deployment node", $type="", $descr="", $tags="", $link="") {
                  Deployment_Node(Default.Deploymentnode.Infrastructurenode, "Infrastructure node", $type="technology", $descr="description", $tags="", $link="")
                }
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void test_printProperties() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("SoftwareSystem");
        Container container1 = softwareSystem.addContainer("Container 1");
        container1.addProperty("structurizr.dsl.identifier", "container1");
        container1.addProperty("IP", "127.0.0.1");
        container1.addProperty("Region", "East");
        Container container2 = softwareSystem.addContainer("Container 2");
        container1.addProperty("structurizr.dsl.identifier", "container2");
        container2.addProperty("Region", "West");
        container2.addProperty("IP", "127.0.0.2");
        Relationship relationship = container1.uses(container2, "");
        relationship.addProperty("Prop1", "Value1");
        relationship.addProperty("Prop2", "Value2");

        workspace.getViews().getConfiguration().addProperty(C4PlantUMLExporter.C4PLANTUML_ELEMENT_PROPERTIES_PROPERTY, Boolean.TRUE.toString());
        workspace.getViews().getConfiguration().addProperty(C4PlantUMLExporter.C4PLANTUML_RELATIONSHIP_PROPERTIES_PROPERTY, Boolean.TRUE.toString());
        ContainerView view = workspace.getViews().createContainerView(softwareSystem, "containerView", "");
        view.addDefaultElements();

        Diagram diagram = new C4PlantUMLExporter().export(view);
        assertEquals("""
                @startuml
                title <size:24>Container View: SoftwareSystem</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                !include <C4/C4_Container>
                
                System_Boundary("SoftwareSystem_boundary", "SoftwareSystem", $tags="") {
                  WithoutPropertyHeader()
                  AddProperty("IP","127.0.0.1")
                  AddProperty("Region","East")
                  Container(SoftwareSystem.Container1, "Container 1", $techn="", $descr="", $tags="", $link="")
                  WithoutPropertyHeader()
                  AddProperty("IP","127.0.0.2")
                  AddProperty("Region","West")
                  Container(SoftwareSystem.Container2, "Container 2", $techn="", $descr="", $tags="", $link="")
                }
                
                WithoutPropertyHeader()
                AddProperty("Prop1","Value1")
                AddProperty("Prop2","Value2")
                Rel(SoftwareSystem.Container1, SoftwareSystem.Container2, "", $techn="", $tags="", $link="")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void test_deploymentViewPrintProperties() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");

        DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Deployment node");
        deploymentNode.addProperty("Prop1", "Value1");

        InfrastructureNode infraNode = deploymentNode.addInfrastructureNode("Infrastructure node", "description", "technology");
        infraNode.addProperty("Prop2", "Value2");

        workspace.getViews().getConfiguration().addProperty(C4PlantUMLExporter.C4PLANTUML_ELEMENT_PROPERTIES_PROPERTY, Boolean.TRUE.toString());
        DeploymentView deploymentView = workspace.getViews().createDeploymentView("deploymentView", "");
        deploymentView.addDefaultElements();

        Diagram diagram = new C4PlantUMLExporter().export(deploymentView);
        assertEquals("""
                @startuml
                title <size:24>Deployment View: Default</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                !include <C4/C4_Deployment>
                
                WithoutPropertyHeader()
                AddProperty("Prop1","Value1")
                Deployment_Node(Default.Deploymentnode, "Deployment node", $type="", $descr="", $tags="", $link="") {
                  WithoutPropertyHeader()
                  AddProperty("Prop2","Value2")
                  Deployment_Node(Default.Deploymentnode.Infrastructurenode, "Infrastructure node", $type="technology", $descr="description", $tags="", $link="")
                }
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void test_legendAndStereotypes() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addSoftwareSystem("Name");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.addDefaultElements();

        // legend (true) and stereotypes (false)
        view.addProperty(C4PlantUMLExporter.C4PLANTUML_LEGEND_PROPERTY, "true");
        view.addProperty(C4PlantUMLExporter.C4PLANTUML_STEREOTYPES_PROPERTY, "false");
        Diagram diagram = new C4PlantUMLExporter().export(view);
        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>\\n<size:24>Description</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                
                System(Name, "Name", $descr="", $tags="", $link="")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());

        // legend (true) and stereotypes (true)
        view.addProperty(C4PlantUMLExporter.C4PLANTUML_LEGEND_PROPERTY, "true");
        view.addProperty(C4PlantUMLExporter.C4PLANTUML_STEREOTYPES_PROPERTY, "true");
        diagram = new C4PlantUMLExporter().export(view);
        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>\\n<size:24>Description</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                
                System(Name, "Name", $descr="", $tags="", $link="")
                
                SHOW_LEGEND(true)
                show stereotypes
                @enduml""", diagram.getDefinition());

        // legend (false) and stereotypes (false)
        view.addProperty(C4PlantUMLExporter.C4PLANTUML_LEGEND_PROPERTY, "false");
        view.addProperty(C4PlantUMLExporter.C4PLANTUML_STEREOTYPES_PROPERTY, "false");
        diagram = new C4PlantUMLExporter().export(view);
        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>\\n<size:24>Description</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                
                System(Name, "Name", $descr="", $tags="", $link="")
                
                SHOW_LEGEND(false)
                hide stereotypes
                @enduml""", diagram.getDefinition());

        // legend (false) and stereotypes (true)
        view.addProperty(C4PlantUMLExporter.C4PLANTUML_LEGEND_PROPERTY, "false");
        view.addProperty(C4PlantUMLExporter.C4PLANTUML_STEREOTYPES_PROPERTY, "true");
        diagram = new C4PlantUMLExporter().export(view);
        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>\\n<size:24>Description</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                
                System(Name, "Name", $descr="", $tags="", $link="")
                
                SHOW_LEGEND(false)
                show stereotypes
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void test_renderContainerShapes() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");

        Container container1 = softwareSystem.addContainer("Default Container");
        Container container2 = softwareSystem.addContainer("Cylinder Container");
        Container container3 = softwareSystem.addContainer("Pipe Container");
        Container container4 = softwareSystem.addContainer("Robot Container");
        container2.addTags("Cylinder");
        container3.addTags("Pipe");
        container4.addTags("Robot"); // Just an example of a shape that has no C4-PlantUML mapping.

        ContainerView containerView = workspace.getViews().createContainerView(softwareSystem, "Containers", "");
        containerView.add(container1);
        containerView.add(container2);
        containerView.add(container3);
        containerView.add(container4);

        workspace.getViews().getConfiguration().getStyles().addElementStyle("Cylinder").shape(Shape.Cylinder);
        workspace.getViews().getConfiguration().getStyles().addElementStyle("Pipe").shape(Shape.Pipe);
        workspace.getViews().getConfiguration().getStyles().addElementStyle("Robot").shape(Shape.Robot);

        Diagram diagram = new C4PlantUMLExporter().export(containerView);
        assertEquals("""
                @startuml
                title <size:24>Container View: Software System</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                !include <C4/C4_Container>
                
                System_Boundary("SoftwareSystem_boundary", "Software System", $tags="") {
                  Container(SoftwareSystem.DefaultContainer, "Default Container", $techn="", $descr="", $tags="", $link="")
                  ContainerDb(SoftwareSystem.CylinderContainer, "Cylinder Container", $techn="", $descr="", $tags="", $link="")
                  ContainerQueue(SoftwareSystem.PipeContainer, "Pipe Container", $techn="", $descr="", $tags="", $link="")
                  Container(SoftwareSystem.RobotContainer, "Robot Container", $techn="", $descr="", $tags="", $link="")
                }
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void test_renderComponentShapes() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");

        Component component1 = container.addComponent("Default Component");
        Component component2 = container.addComponent("Cylinder Component");
        Component component3 = container.addComponent("Pipe Component");
        Component component4 = container.addComponent("Robot Component");

        component2.addTags("Cylinder");
        component3.addTags("Pipe");
        component4.addTags("Robot"); // Just an example of a shape that has no C4-PlantUML mapping.

        ContainerView containerView = workspace.getViews().createContainerView(softwareSystem, "Containers", "");
        ComponentView componentView = workspace.getViews().createComponentView(container, "Components", "");
        componentView.add(component1);
        componentView.add(component2);
        componentView.add(component3);
        componentView.add(component4);

        workspace.getViews().getConfiguration().getStyles().addElementStyle("Cylinder").shape(Shape.Cylinder);
        workspace.getViews().getConfiguration().getStyles().addElementStyle("Pipe").shape(Shape.Pipe);
        workspace.getViews().getConfiguration().getStyles().addElementStyle("Robot").shape(Shape.Robot);

        Diagram diagram = new C4PlantUMLExporter().export(componentView);
        assertEquals("""
                @startuml
                title <size:24>Component View: Software System - Container</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                !include <C4/C4_Component>
                
                System_Boundary("SoftwareSystem_boundary", "Software System", $tags="") {
                  Container_Boundary("SoftwareSystem.Container_boundary", "Container", $tags="") {
                    Component(SoftwareSystem.Container.DefaultComponent, "Default Component", $techn="", $descr="", $tags="", $link="")
                    ComponentDb(SoftwareSystem.Container.CylinderComponent, "Cylinder Component", $techn="", $descr="", $tags="", $link="")
                    ComponentQueue(SoftwareSystem.Container.PipeComponent, "Pipe Component", $techn="", $descr="", $tags="", $link="")
                    Component(SoftwareSystem.Container.RobotComponent, "Robot Component", $techn="", $descr="", $tags="", $link="")
                  }
                
                }
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void testFont() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addPerson("User");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.addAllElements();
        workspace.getViews().getConfiguration().getBranding().setFont(new Font("Courier"));

        Diagram diagram = new C4PlantUMLExporter().export(view);
        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>\\n<size:24>Description</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                    FontName: Courier
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                
                Person(User, "User", $descr="", $tags="", $link="")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());

    }

    @Test
    public void stdlib_false() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addPerson("User");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.addAllElements();
        view.addProperty(C4PlantUMLExporter.C4PLANTUML_STANDARD_LIBRARY_PROPERTY, "false");

        Diagram diagram = new C4PlantUMLExporter().export(view);
        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>\\n<size:24>Description</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4.puml
                !include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Context.puml
                
                Person(User, "User", $descr="", $tags="", $link="")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition().toString());

    }

    @Test
    public void componentWithoutTechnology() {
        Workspace workspace = new Workspace("Name", "Description");
        Container container = workspace.getModel().addSoftwareSystem("Name").addContainer("Name");
        container.addComponent("Name").setDescription("Description");

        ComponentView view = workspace.getViews().createComponentView(container, "key", "Description");
        view.addAllElements();

        Diagram diagram = new C4PlantUMLExporter().export(view);
        assertEquals("""
                @startuml
                title <size:24>Component View: Name - Name</size>\\n<size:24>Description</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                !include <C4/C4_Component>
                
                System_Boundary("Name_boundary", "Name", $tags="") {
                  Container_Boundary("Name.Name_boundary", "Name", $tags="") {
                    Component(Name.Name.Name, "Name", $techn="", $descr="Description", $tags="", $link="")
                  }
                
                }
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void borderStyling() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addSoftwareSystem("Name");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.addAllElements();
        workspace.getViews().getConfiguration().addProperty(C4PlantUMLExporter.C4PLANTUML_TAGS_PROPERTY, "true");
        workspace.getViews().getConfiguration().getStyles().addElementStyle(Tags.ELEMENT).stroke("green").border(Border.Dashed).strokeWidth(2);

        Diagram diagram = new C4PlantUMLExporter().export(view);
        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>\\n<size:24>Description</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                
                AddElementTag("Element", $bgColor="#ffffff", $borderColor="#008000", $fontColor="#444444", $sprite="", $shadowing="", $borderStyle="dashed", $borderThickness="2")
                
                System(Name, "Name", $descr="", $tags="Element", $link="")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void elementWithUrl() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addSoftwareSystem("Name").setUrl("https://example.com");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.addAllElements();

        Diagram diagram = new C4PlantUMLExporter().export(view);
        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>\\n<size:24>Description</size>
                
                set separator none
                top to bottom direction
                
                <style>
                  root {
                    BackgroundColor: #ffffff
                    FontColor: #444444
                  }
                </style>
                
                !include <C4/C4>
                !include <C4/C4_Context>
                
                System(Name, "Name", $descr="", $tags="", $link="https://example.com")
                
                SHOW_LEGEND(true)
                hide stereotypes
                @enduml""", diagram.getDefinition());
    }

}