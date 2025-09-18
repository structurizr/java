package com.structurizr.export.dot;

import com.structurizr.Workspace;
import com.structurizr.export.AbstractExporterTests;
import com.structurizr.export.Diagram;
import com.structurizr.model.*;
import com.structurizr.util.WorkspaceUtils;
import com.structurizr.view.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DOTDiagramExporterTests extends AbstractExporterTests {

    @Test
    public void test_BigBankPlcExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/big-bank-plc.json"));
        DOTExporter dotWriter = new DOTExporter();

        Collection<Diagram> diagrams = dotWriter.export(workspace);
        assertEquals(7, diagrams.size());

        Diagram diagram = diagrams.stream().filter(md -> md.getKey().equals("SystemLandscape")).findFirst().get();
        assertEquals("""
                digraph {
                  compound=true
                  graph [fontname="Arial", rankdir=TB, ranksep=1.0, nodesep=1.0]
                  node [fontname="Arial", shape=box, margin="0.4,0.3"]
                  edge [fontname="Arial"]
                  label=<<br /><font point-size="34">System Landscape View</font>>
                
                  subgraph "cluster_group_Big Bank plc" {
                    margin=25
                    label=<<font point-size="24"><br />Big Bank plc</font>>
                    labelloc=b
                    color="#cccccc"
                    fontcolor="#cccccc"
                    fillcolor="#ffffff"
                    style="dashed"
                
                    2 [id=2,shape=rect, label=<<font point-size="32">Customer Service Staff</font><br /><font point-size="17">[Person]</font><br /><br /><font point-size="22">Customer service staff within the<br />bank.</font>>, style=filled, color="#6b6b6b", fillcolor="#999999", fontcolor="#ffffff"]
                    3 [id=3,shape=rect, label=<<font point-size="32">Back Office Staff</font><br /><font point-size="17">[Person]</font><br /><br /><font point-size="22">Administration and support staff<br />within the bank.</font>>, style=filled, color="#6b6b6b", fillcolor="#999999", fontcolor="#ffffff"]
                    4 [id=4,shape=rect, label=<<font point-size="34">Mainframe Banking<br />System</font><br /><font point-size="19">[Software System]</font><br /><br /><font point-size="24">Stores all of the core banking<br />information about customers,<br />accounts, transactions, etc.</font>>, style=filled, color="#6b6b6b", fillcolor="#999999", fontcolor="#ffffff"]
                    5 [id=5,shape=rect, label=<<font point-size="34">E-mail System</font><br /><font point-size="19">[Software System]</font><br /><br /><font point-size="24">The internal Microsoft<br />Exchange e-mail system.</font>>, style=filled, color="#6b6b6b", fillcolor="#999999", fontcolor="#ffffff"]
                    6 [id=6,shape=rect, label=<<font point-size="34">ATM</font><br /><font point-size="19">[Software System]</font><br /><br /><font point-size="24">Allows customers to withdraw<br />cash.</font>>, style=filled, color="#6b6b6b", fillcolor="#999999", fontcolor="#ffffff"]
                    7 [id=7,shape=rect, label=<<font point-size="34">Internet Banking<br />System</font><br /><font point-size="19">[Software System]</font><br /><br /><font point-size="24">Allows customers to view<br />information about their bank<br />accounts, and make payments.</font>>, style=filled, color="#0b4884", fillcolor="#1168bd", fontcolor="#ffffff"]
                  }
                
                  1 [id=1,shape=rect, label=<<font point-size="32">Personal Banking<br />Customer</font><br /><font point-size="17">[Person]</font><br /><br /><font point-size="22">A customer of the bank, with<br />personal bank accounts.</font>>, style=filled, color="#052e56", fillcolor="#08427b", fontcolor="#ffffff"]
                
                  1 -> 7 [id=19, label=<<font point-size="24">Views account balances,<br />and makes payments using</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  7 -> 4 [id=20, label=<<font point-size="24">Gets account information<br />from, and makes payments<br />using</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  7 -> 5 [id=21, label=<<font point-size="24">Sends e-mail using</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  5 -> 1 [id=22, label=<<font point-size="24">Sends e-mails to</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  1 -> 2 [id=23, label=<<font point-size="24">Asks questions to</font><br /><font point-size="19">[Telephone]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  2 -> 4 [id=24, label=<<font point-size="24">Uses</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  1 -> 6 [id=25, label=<<font point-size="24">Withdraws cash using</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  6 -> 4 [id=26, label=<<font point-size="24">Uses</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  3 -> 4 [id=27, label=<<font point-size="24">Uses</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                
                }""", diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("SystemContext")).findFirst().get();
        assertEquals("""
                digraph {
                  compound=true
                  graph [fontname="Arial", rankdir=TB, ranksep=1.0, nodesep=1.0]
                  node [fontname="Arial", shape=box, margin="0.4,0.3"]
                  edge [fontname="Arial"]
                  label=<<br /><font point-size="34">System Context View: Internet Banking System</font><br /><font point-size="24">The system context diagram for the Internet Banking System.</font>>
                
                  subgraph "cluster_group_Big Bank plc" {
                    margin=25
                    label=<<font point-size="24"><br />Big Bank plc</font>>
                    labelloc=b
                    color="#cccccc"
                    fontcolor="#cccccc"
                    fillcolor="#ffffff"
                    style="dashed"
                
                    4 [id=4,shape=rect, label=<<font point-size="34">Mainframe Banking<br />System</font><br /><font point-size="19">[Software System]</font><br /><br /><font point-size="24">Stores all of the core banking<br />information about customers,<br />accounts, transactions, etc.</font>>, style=filled, color="#6b6b6b", fillcolor="#999999", fontcolor="#ffffff"]
                    5 [id=5,shape=rect, label=<<font point-size="34">E-mail System</font><br /><font point-size="19">[Software System]</font><br /><br /><font point-size="24">The internal Microsoft<br />Exchange e-mail system.</font>>, style=filled, color="#6b6b6b", fillcolor="#999999", fontcolor="#ffffff"]
                    7 [id=7,shape=rect, label=<<font point-size="34">Internet Banking<br />System</font><br /><font point-size="19">[Software System]</font><br /><br /><font point-size="24">Allows customers to view<br />information about their bank<br />accounts, and make payments.</font>>, style=filled, color="#0b4884", fillcolor="#1168bd", fontcolor="#ffffff"]
                  }
                
                  1 [id=1,shape=rect, label=<<font point-size="32">Personal Banking<br />Customer</font><br /><font point-size="17">[Person]</font><br /><br /><font point-size="22">A customer of the bank, with<br />personal bank accounts.</font>>, style=filled, color="#052e56", fillcolor="#08427b", fontcolor="#ffffff"]
                
                  1 -> 7 [id=19, label=<<font point-size="24">Views account balances,<br />and makes payments using</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  7 -> 4 [id=20, label=<<font point-size="24">Gets account information<br />from, and makes payments<br />using</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  7 -> 5 [id=21, label=<<font point-size="24">Sends e-mail using</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  5 -> 1 [id=22, label=<<font point-size="24">Sends e-mails to</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                
                }""", diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Containers")).findFirst().get();
        assertEquals("""
                digraph {
                  compound=true
                  graph [fontname="Arial", rankdir=TB, ranksep=1.0, nodesep=1.0]
                  node [fontname="Arial", shape=box, margin="0.4,0.3"]
                  edge [fontname="Arial"]
                  label=<<br /><font point-size="34">Container View: Internet Banking System</font><br /><font point-size="24">The container diagram for the Internet Banking System.</font>>
                
                  1 [id=1,shape=rect, label=<<font point-size="32">Personal Banking<br />Customer</font><br /><font point-size="17">[Person]</font><br /><br /><font point-size="22">A customer of the bank, with<br />personal bank accounts.</font>>, style=filled, color="#052e56", fillcolor="#08427b", fontcolor="#ffffff"]
                  4 [id=4,shape=rect, label=<<font point-size="34">Mainframe Banking<br />System</font><br /><font point-size="19">[Software System]</font><br /><br /><font point-size="24">Stores all of the core banking<br />information about customers,<br />accounts, transactions, etc.</font>>, style=filled, color="#6b6b6b", fillcolor="#999999", fontcolor="#ffffff"]
                  5 [id=5,shape=rect, label=<<font point-size="34">E-mail System</font><br /><font point-size="19">[Software System]</font><br /><br /><font point-size="24">The internal Microsoft<br />Exchange e-mail system.</font>>, style=filled, color="#6b6b6b", fillcolor="#999999", fontcolor="#ffffff"]
                
                  subgraph cluster_7 {
                    margin=25
                    label=<<font point-size="24"><br />Internet Banking System</font><br /><font point-size="19">[Software System]</font>>
                    labelloc=b
                    color="#444444"
                    fontcolor="#444444"
                    fillcolor="#444444"
                
                    10 [id=10,shape=rect, label=<<font point-size="34">Web Application</font><br /><font point-size="19">[Container: Java and Spring MVC]</font><br /><br /><font point-size="24">Delivers the static content<br />and the Internet banking<br />single page application.</font>>, style=filled, color="#2e6295", fillcolor="#438dd5", fontcolor="#ffffff"]
                    11 [id=11,shape=rect, label=<<font point-size="34">API Application</font><br /><font point-size="19">[Container: Java and Spring MVC]</font><br /><br /><font point-size="24">Provides Internet banking<br />functionality via a JSON/HTTPS<br />API.</font>>, style=filled, color="#2e6295", fillcolor="#438dd5", fontcolor="#ffffff"]
                    18 [id=18,shape=cylinder, label=<<font point-size="34">Database</font><br /><font point-size="19">[Container: Oracle Database Schema]</font><br /><br /><font point-size="24">Stores user registration<br />information, hashed<br />authentication credentials,<br />access logs, etc.</font>>, style=filled, color="#2e6295", fillcolor="#438dd5", fontcolor="#ffffff"]
                    8 [id=8,shape=rect, label=<<font point-size="34">Single-Page<br />Application</font><br /><font point-size="19">[Container: JavaScript and Angular]</font><br /><br /><font point-size="24">Provides all of the Internet<br />banking functionality to<br />customers via their web<br />browser.</font>>, style=filled, color="#2e6295", fillcolor="#438dd5", fontcolor="#ffffff"]
                    9 [id=9,shape=rect, label=<<font point-size="34">Mobile App</font><br /><font point-size="19">[Container: Xamarin]</font><br /><br /><font point-size="24">Provides a limited subset of<br />the Internet banking<br />functionality to customers via<br />their mobile device.</font>>, style=filled, color="#2e6295", fillcolor="#438dd5", fontcolor="#ffffff"]
                  }
                
                  5 -> 1 [id=22, label=<<font point-size="24">Sends e-mails to</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  1 -> 10 [id=28, label=<<font point-size="24">Visits bigbank.com/ib<br />using</font><br /><font point-size="19">[HTTPS]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  1 -> 8 [id=29, label=<<font point-size="24">Views account balances,<br />and makes payments using</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  1 -> 9 [id=30, label=<<font point-size="24">Views account balances,<br />and makes payments using</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  10 -> 8 [id=31, label=<<font point-size="24">Delivers to the customer's<br />web browser</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  8 -> 11 [id=33, label=<<font point-size="24">Makes API calls to</font><br /><font point-size="19">[JSON/HTTPS]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  9 -> 11 [id=37, label=<<font point-size="24">Makes API calls to</font><br /><font point-size="19">[JSON/HTTPS]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  11 -> 18 [id=45, label=<<font point-size="24">Reads from and writes to</font><br /><font point-size="19">[SQL/TCP]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  11 -> 4 [id=47, label=<<font point-size="24">Makes API calls to</font><br /><font point-size="19">[XML/HTTPS]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  11 -> 5 [id=49, label=<<font point-size="24">Sends e-mail using</font>>, style="dashed", color="#444444", fontcolor="#444444"]

                }""", diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Components")).findFirst().get();
        assertEquals("""
                digraph {
                  compound=true
                  graph [fontname="Arial", rankdir=TB, ranksep=1.0, nodesep=1.0]
                  node [fontname="Arial", shape=box, margin="0.4,0.3"]
                  edge [fontname="Arial"]
                  label=<<br /><font point-size="34">Component View: Internet Banking System - API Application</font><br /><font point-size="24">The component diagram for the API Application.</font>>
                
                  4 [id=4,shape=rect, label=<<font point-size="34">Mainframe Banking<br />System</font><br /><font point-size="19">[Software System]</font><br /><br /><font point-size="24">Stores all of the core banking<br />information about customers,<br />accounts, transactions, etc.</font>>, style=filled, color="#6b6b6b", fillcolor="#999999", fontcolor="#ffffff"]
                  5 [id=5,shape=rect, label=<<font point-size="34">E-mail System</font><br /><font point-size="19">[Software System]</font><br /><br /><font point-size="24">The internal Microsoft<br />Exchange e-mail system.</font>>, style=filled, color="#6b6b6b", fillcolor="#999999", fontcolor="#ffffff"]
                  8 [id=8,shape=rect, label=<<font point-size="34">Single-Page<br />Application</font><br /><font point-size="19">[Container: JavaScript and Angular]</font><br /><br /><font point-size="24">Provides all of the Internet<br />banking functionality to<br />customers via their web<br />browser.</font>>, style=filled, color="#2e6295", fillcolor="#438dd5", fontcolor="#ffffff"]
                  9 [id=9,shape=rect, label=<<font point-size="34">Mobile App</font><br /><font point-size="19">[Container: Xamarin]</font><br /><br /><font point-size="24">Provides a limited subset of<br />the Internet banking<br />functionality to customers via<br />their mobile device.</font>>, style=filled, color="#2e6295", fillcolor="#438dd5", fontcolor="#ffffff"]
                  18 [id=18,shape=cylinder, label=<<font point-size="34">Database</font><br /><font point-size="19">[Container: Oracle Database Schema]</font><br /><br /><font point-size="24">Stores user registration<br />information, hashed<br />authentication credentials,<br />access logs, etc.</font>>, style=filled, color="#2e6295", fillcolor="#438dd5", fontcolor="#ffffff"]
                
                  subgraph cluster_11 {
                    margin=25
                    label=<<font point-size="24"><br />API Application</font><br /><font point-size="19">[Container: Java and Spring MVC]</font>>
                    labelloc=b
                    color="#444444"
                    fontcolor="#444444"
                    fillcolor="#444444"
                
                    12 [id=12,shape=rect, label=<<font point-size="34">Sign In Controller</font><br /><font point-size="19">[Component: Spring MVC Rest Controller]</font><br /><br /><font point-size="24">Allows users to sign in to the<br />Internet Banking System.</font>>, style=filled, color="#5d82a8", fillcolor="#85bbf0", fontcolor="#000000"]
                    13 [id=13,shape=rect, label=<<font point-size="34">Accounts Summary<br />Controller</font><br /><font point-size="19">[Component: Spring MVC Rest Controller]</font><br /><br /><font point-size="24">Provides customers with a<br />summary of their bank<br />accounts.</font>>, style=filled, color="#5d82a8", fillcolor="#85bbf0", fontcolor="#000000"]
                    14 [id=14,shape=rect, label=<<font point-size="34">Reset Password<br />Controller</font><br /><font point-size="19">[Component: Spring MVC Rest Controller]</font><br /><br /><font point-size="24">Allows users to reset their<br />passwords with a single use<br />URL.</font>>, style=filled, color="#5d82a8", fillcolor="#85bbf0", fontcolor="#000000"]
                    15 [id=15,shape=rect, label=<<font point-size="34">Security Component</font><br /><font point-size="19">[Component: Spring Bean]</font><br /><br /><font point-size="24">Provides functionality related<br />to signing in, changing<br />passwords, etc.</font>>, style=filled, color="#5d82a8", fillcolor="#85bbf0", fontcolor="#000000"]
                    16 [id=16,shape=rect, label=<<font point-size="34">Mainframe Banking<br />System Facade</font><br /><font point-size="19">[Component: Spring Bean]</font><br /><br /><font point-size="24">A facade onto the mainframe<br />banking system.</font>>, style=filled, color="#5d82a8", fillcolor="#85bbf0", fontcolor="#000000"]
                    17 [id=17,shape=rect, label=<<font point-size="34">E-mail Component</font><br /><font point-size="19">[Component: Spring Bean]</font><br /><br /><font point-size="24">Sends e-mails to users.</font>>, style=filled, color="#5d82a8", fillcolor="#85bbf0", fontcolor="#000000"]
                  }
                
                  8 -> 12 [id=32, label=<<font point-size="24">Makes API calls to</font><br /><font point-size="19">[JSON/HTTPS]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  8 -> 13 [id=34, label=<<font point-size="24">Makes API calls to</font><br /><font point-size="19">[JSON/HTTPS]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  8 -> 14 [id=35, label=<<font point-size="24">Makes API calls to</font><br /><font point-size="19">[JSON/HTTPS]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  9 -> 12 [id=36, label=<<font point-size="24">Makes API calls to</font><br /><font point-size="19">[JSON/HTTPS]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  9 -> 13 [id=38, label=<<font point-size="24">Makes API calls to</font><br /><font point-size="19">[JSON/HTTPS]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  9 -> 14 [id=39, label=<<font point-size="24">Makes API calls to</font><br /><font point-size="19">[JSON/HTTPS]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  12 -> 15 [id=40, label=<<font point-size="24">Uses</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  13 -> 16 [id=41, label=<<font point-size="24">Uses</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  14 -> 15 [id=42, label=<<font point-size="24">Uses</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  14 -> 17 [id=43, label=<<font point-size="24">Uses</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  15 -> 18 [id=44, label=<<font point-size="24">Reads from and writes to</font><br /><font point-size="19">[SQL/TCP]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  16 -> 4 [id=46, label=<<font point-size="24">Makes API calls to</font><br /><font point-size="19">[XML/HTTPS]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  17 -> 5 [id=48, label=<<font point-size="24">Sends e-mail using</font>>, style="dashed", color="#444444", fontcolor="#444444"]

                }""", diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("SignIn")).findFirst().get();
        assertEquals("""
                digraph {
                  compound=true
                  graph [fontname="Arial", rankdir=TB, ranksep=1.0, nodesep=1.0]
                  node [fontname="Arial", shape=box, margin="0.4,0.3"]
                  edge [fontname="Arial"]
                  label=<<br /><font point-size="34">Dynamic View: Internet Banking System - API Application</font><br /><font point-size="24">Summarises how the sign in feature works in the single-page application.</font>>
                
                  subgraph cluster_11 {
                    margin=25
                    label=<<font point-size="24"><br />API Application</font><br /><font point-size="19">[Container: Java and Spring MVC]</font>>
                    labelloc=b
                    color="#444444"
                    fontcolor="#444444"
                    fillcolor="#444444"
                
                    12 [id=12,shape=rect, label=<<font point-size="34">Sign In Controller</font><br /><font point-size="19">[Component: Spring MVC Rest Controller]</font><br /><br /><font point-size="24">Allows users to sign in to the<br />Internet Banking System.</font>>, style=filled, color="#5d82a8", fillcolor="#85bbf0", fontcolor="#000000"]
                    15 [id=15,shape=rect, label=<<font point-size="34">Security Component</font><br /><font point-size="19">[Component: Spring Bean]</font><br /><br /><font point-size="24">Provides functionality related<br />to signing in, changing<br />passwords, etc.</font>>, style=filled, color="#5d82a8", fillcolor="#85bbf0", fontcolor="#000000"]
                  }
                
                  8 [id=8,shape=rect, label=<<font point-size="34">Single-Page<br />Application</font><br /><font point-size="19">[Container: JavaScript and Angular]</font><br /><br /><font point-size="24">Provides all of the Internet<br />banking functionality to<br />customers via their web<br />browser.</font>>, style=filled, color="#2e6295", fillcolor="#438dd5", fontcolor="#ffffff"]
                  18 [id=18,shape=cylinder, label=<<font point-size="34">Database</font><br /><font point-size="19">[Container: Oracle Database Schema]</font><br /><br /><font point-size="24">Stores user registration<br />information, hashed<br />authentication credentials,<br />access logs, etc.</font>>, style=filled, color="#2e6295", fillcolor="#438dd5", fontcolor="#ffffff"]
                
                  8 -> 12 [id=32, label=<<font point-size="24">1. Submits credentials to</font><br /><font point-size="19">[JSON/HTTPS]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  12 -> 15 [id=40, label=<<font point-size="24">2. Validates credentials<br />using</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  15 -> 18 [id=44, label=<<font point-size="24">3. select * from users<br />where username = ?</font><br /><font point-size="19">[SQL/TCP]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  18 -> 15 [id=44, label=<<font point-size="24">4. Returns user data to</font><br /><font point-size="19">[SQL/TCP]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  15 -> 12 [id=40, label=<<font point-size="24">5. Returns true if the<br />hashed password matches</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  12 -> 8 [id=32, label=<<font point-size="24">6. Sends back an<br />authentication token to</font><br /><font point-size="19">[JSON/HTTPS]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                
                }""", diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("DevelopmentDeployment")).findFirst().get();
        assertEquals("""
                digraph {
                  compound=true
                  graph [fontname="Arial", rankdir=TB, ranksep=1.0, nodesep=1.0]
                  node [fontname="Arial", shape=box, margin="0.4,0.3"]
                  edge [fontname="Arial"]
                  label=<<br /><font point-size="34">Deployment View: Internet Banking System - Development</font><br /><font point-size="24">An example development deployment scenario for the Internet Banking System.</font>>
                
                  subgraph cluster_50 {
                    margin=25
                    label=<<font point-size="24">Developer Laptop</font><br /><font point-size="19">[Deployment Node: Microsoft Windows 10 or Apple macOS]</font>>
                    labelloc=b
                    color="#444444"
                    fontcolor="#444444"
                    fillcolor="#ffffff"
                
                    subgraph cluster_51 {
                      margin=25
                      label=<<font point-size="24">Web Browser</font><br /><font point-size="19">[Deployment Node: Chrome, Firefox, Safari, or Edge]</font>>
                      labelloc=b
                      color="#444444"
                      fontcolor="#444444"
                      fillcolor="#ffffff"
                
                      52 [id=52,shape=rect, label=<<font point-size="34">Single-Page<br />Application</font><br /><font point-size="19">[Container: JavaScript and Angular]</font><br /><br /><font point-size="24">Provides all of the Internet<br />banking functionality to<br />customers via their web<br />browser.</font>>, style=filled, color="#2e6295", fillcolor="#438dd5", fontcolor="#ffffff"]
                    }
                
                    subgraph cluster_53 {
                      margin=25
                      label=<<font point-size="24">Docker Container - Web Server</font><br /><font point-size="19">[Deployment Node: Docker]</font>>
                      labelloc=b
                      color="#444444"
                      fontcolor="#444444"
                      fillcolor="#ffffff"
                
                      subgraph cluster_54 {
                        margin=25
                        label=<<font point-size="24">Apache Tomcat</font><br /><font point-size="19">[Deployment Node: Apache Tomcat 8.x]</font>>
                        labelloc=b
                        color="#444444"
                        fontcolor="#444444"
                        fillcolor="#ffffff"
                
                        55 [id=55,shape=rect, label=<<font point-size="34">Web Application</font><br /><font point-size="19">[Container: Java and Spring MVC]</font><br /><br /><font point-size="24">Delivers the static content<br />and the Internet banking<br />single page application.</font>>, style=filled, color="#2e6295", fillcolor="#438dd5", fontcolor="#ffffff"]
                        57 [id=57,shape=rect, label=<<font point-size="34">API Application</font><br /><font point-size="19">[Container: Java and Spring MVC]</font><br /><br /><font point-size="24">Provides Internet banking<br />functionality via a JSON/HTTPS<br />API.</font>>, style=filled, color="#2e6295", fillcolor="#438dd5", fontcolor="#ffffff"]
                      }
                
                    }
                
                    subgraph cluster_59 {
                      margin=25
                      label=<<font point-size="24">Docker Container - Database Server</font><br /><font point-size="19">[Deployment Node: Docker]</font>>
                      labelloc=b
                      color="#444444"
                      fontcolor="#444444"
                      fillcolor="#ffffff"
                
                      subgraph cluster_60 {
                        margin=25
                        label=<<font point-size="24">Database Server</font><br /><font point-size="19">[Deployment Node: Oracle 12c]</font>>
                        labelloc=b
                        color="#444444"
                        fontcolor="#444444"
                        fillcolor="#ffffff"
                
                        61 [id=61,shape=cylinder, label=<<font point-size="34">Database</font><br /><font point-size="19">[Container: Oracle Database Schema]</font><br /><br /><font point-size="24">Stores user registration<br />information, hashed<br />authentication credentials,<br />access logs, etc.</font>>, style=filled, color="#2e6295", fillcolor="#438dd5", fontcolor="#ffffff"]
                      }
                
                    }
                
                  }
                
                  subgraph cluster_63 {
                    margin=25
                    label=<<font point-size="24">Big Bank plc</font><br /><font point-size="19">[Deployment Node: Big Bank plc data center]</font>>
                    labelloc=b
                    color="#444444"
                    fontcolor="#444444"
                    fillcolor="#ffffff"
                
                    subgraph cluster_64 {
                      margin=25
                      label=<<font point-size="24">bigbank-dev001</font><br /><font point-size="19">[Deployment Node]</font>>
                      labelloc=b
                      color="#444444"
                      fontcolor="#444444"
                      fillcolor="#ffffff"
                
                      65 [id=65,shape=rect, label=<<font point-size="34">Mainframe Banking<br />System</font><br /><font point-size="19">[Software System]</font><br /><br /><font point-size="24">Stores all of the core banking<br />information about customers,<br />accounts, transactions, etc.</font>>, style=filled, color="#6b6b6b", fillcolor="#999999", fontcolor="#ffffff"]
                    }
                
                  }
                
                  55 -> 52 [id=56, label=<<font point-size="24">Delivers to the customer's<br />web browser</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  52 -> 57 [id=58, label=<<font point-size="24">Makes API calls to</font><br /><font point-size="19">[JSON/HTTPS]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  57 -> 61 [id=62, label=<<font point-size="24">Reads from and writes to</font><br /><font point-size="19">[SQL/TCP]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  57 -> 65 [id=66, label=<<font point-size="24">Makes API calls to</font><br /><font point-size="19">[XML/HTTPS]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                
                }""", diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("LiveDeployment")).findFirst().get();
        assertEquals("""
                digraph {
                  compound=true
                  graph [fontname="Arial", rankdir=TB, ranksep=1.0, nodesep=1.0]
                  node [fontname="Arial", shape=box, margin="0.4,0.3"]
                  edge [fontname="Arial"]
                  label=<<br /><font point-size="34">Deployment View: Internet Banking System - Live</font><br /><font point-size="24">An example live deployment scenario for the Internet Banking System.</font>>
                
                  subgraph cluster_67 {
                    margin=25
                    label=<<font point-size="24">Customer's mobile device</font><br /><font point-size="19">[Deployment Node: Apple iOS or Android]</font>>
                    labelloc=b
                    color="#444444"
                    fontcolor="#444444"
                    fillcolor="#ffffff"
                
                    68 [id=68,shape=rect, label=<<font point-size="34">Mobile App</font><br /><font point-size="19">[Container: Xamarin]</font><br /><br /><font point-size="24">Provides a limited subset of<br />the Internet banking<br />functionality to customers via<br />their mobile device.</font>>, style=filled, color="#2e6295", fillcolor="#438dd5", fontcolor="#ffffff"]
                  }
                
                  subgraph cluster_69 {
                    margin=25
                    label=<<font point-size="24">Customer's computer</font><br /><font point-size="19">[Deployment Node: Microsoft Windows or Apple macOS]</font>>
                    labelloc=b
                    color="#444444"
                    fontcolor="#444444"
                    fillcolor="#ffffff"
                
                    subgraph cluster_70 {
                      margin=25
                      label=<<font point-size="24">Web Browser</font><br /><font point-size="19">[Deployment Node: Chrome, Firefox, Safari, or Edge]</font>>
                      labelloc=b
                      color="#444444"
                      fontcolor="#444444"
                      fillcolor="#ffffff"
                
                      71 [id=71,shape=rect, label=<<font point-size="34">Single-Page<br />Application</font><br /><font point-size="19">[Container: JavaScript and Angular]</font><br /><br /><font point-size="24">Provides all of the Internet<br />banking functionality to<br />customers via their web<br />browser.</font>>, style=filled, color="#2e6295", fillcolor="#438dd5", fontcolor="#ffffff"]
                    }
                
                  }
                
                  subgraph cluster_72 {
                    margin=25
                    label=<<font point-size="24">Big Bank plc</font><br /><font point-size="19">[Deployment Node: Big Bank plc data center]</font>>
                    labelloc=b
                    color="#444444"
                    fontcolor="#444444"
                    fillcolor="#ffffff"
                
                    subgraph cluster_73 {
                      margin=25
                      label=<<font point-size="24">bigbank-web***</font><br /><font point-size="19">[Deployment Node: Ubuntu 16.04 LTS]</font>>
                      labelloc=b
                      color="#444444"
                      fontcolor="#444444"
                      fillcolor="#ffffff"
                
                      subgraph cluster_74 {
                        margin=25
                        label=<<font point-size="24">Apache Tomcat</font><br /><font point-size="19">[Deployment Node: Apache Tomcat 8.x]</font>>
                        labelloc=b
                        color="#444444"
                        fontcolor="#444444"
                        fillcolor="#ffffff"
                
                        75 [id=75,shape=rect, label=<<font point-size="34">Web Application</font><br /><font point-size="19">[Container: Java and Spring MVC]</font><br /><br /><font point-size="24">Delivers the static content<br />and the Internet banking<br />single page application.</font>>, style=filled, color="#2e6295", fillcolor="#438dd5", fontcolor="#ffffff"]
                      }
                
                    }
                
                    subgraph cluster_77 {
                      margin=25
                      label=<<font point-size="24">bigbank-api***</font><br /><font point-size="19">[Deployment Node: Ubuntu 16.04 LTS]</font>>
                      labelloc=b
                      color="#444444"
                      fontcolor="#444444"
                      fillcolor="#ffffff"
                
                      subgraph cluster_78 {
                        margin=25
                        label=<<font point-size="24">Apache Tomcat</font><br /><font point-size="19">[Deployment Node: Apache Tomcat 8.x]</font>>
                        labelloc=b
                        color="#444444"
                        fontcolor="#444444"
                        fillcolor="#ffffff"
                
                        79 [id=79,shape=rect, label=<<font point-size="34">API Application</font><br /><font point-size="19">[Container: Java and Spring MVC]</font><br /><br /><font point-size="24">Provides Internet banking<br />functionality via a JSON/HTTPS<br />API.</font>>, style=filled, color="#2e6295", fillcolor="#438dd5", fontcolor="#ffffff"]
                      }
                
                    }
                
                    subgraph cluster_82 {
                      margin=25
                      label=<<font point-size="24">bigbank-db01</font><br /><font point-size="19">[Deployment Node: Ubuntu 16.04 LTS]</font>>
                      labelloc=b
                      color="#444444"
                      fontcolor="#444444"
                      fillcolor="#ffffff"
                
                      subgraph cluster_83 {
                        margin=25
                        label=<<font point-size="24">Oracle - Primary</font><br /><font point-size="19">[Deployment Node: Oracle 12c]</font>>
                        labelloc=b
                        color="#444444"
                        fontcolor="#444444"
                        fillcolor="#ffffff"
                
                        84 [id=84,shape=cylinder, label=<<font point-size="34">Database</font><br /><font point-size="19">[Container: Oracle Database Schema]</font><br /><br /><font point-size="24">Stores user registration<br />information, hashed<br />authentication credentials,<br />access logs, etc.</font>>, style=filled, color="#2e6295", fillcolor="#438dd5", fontcolor="#ffffff"]
                      }
                
                    }
                
                    subgraph cluster_86 {
                      margin=25
                      label=<<font point-size="24">bigbank-db02</font><br /><font point-size="19">[Deployment Node: Ubuntu 16.04 LTS]</font>>
                      labelloc=b
                      color="#444444"
                      fontcolor="#444444"
                      fillcolor="#ffffff"
                
                      subgraph cluster_87 {
                        margin=25
                        label=<<font point-size="24">Oracle - Secondary</font><br /><font point-size="19">[Deployment Node: Oracle 12c]</font>>
                        labelloc=b
                        color="#444444"
                        fontcolor="#444444"
                        fillcolor="#ffffff"
                
                        88 [id=88,shape=cylinder, label=<<font point-size="34">Database</font><br /><font point-size="19">[Container: Oracle Database Schema]</font><br /><br /><font point-size="24">Stores user registration<br />information, hashed<br />authentication credentials,<br />access logs, etc.</font>>, style=filled, color="#2e6295", fillcolor="#438dd5", fontcolor="#ffffff"]
                      }
                
                    }
                
                    subgraph cluster_90 {
                      margin=25
                      label=<<font point-size="24">bigbank-prod001</font><br /><font point-size="19">[Deployment Node]</font>>
                      labelloc=b
                      color="#444444"
                      fontcolor="#444444"
                      fillcolor="#ffffff"
                
                      91 [id=91,shape=rect, label=<<font point-size="34">Mainframe Banking<br />System</font><br /><font point-size="19">[Software System]</font><br /><br /><font point-size="24">Stores all of the core banking<br />information about customers,<br />accounts, transactions, etc.</font>>, style=filled, color="#6b6b6b", fillcolor="#999999", fontcolor="#ffffff"]
                    }
                
                  }
                
                  75 -> 71 [id=76, label=<<font point-size="24">Delivers to the customer's<br />web browser</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  68 -> 79 [id=80, label=<<font point-size="24">Makes API calls to</font><br /><font point-size="19">[JSON/HTTPS]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  71 -> 79 [id=81, label=<<font point-size="24">Makes API calls to</font><br /><font point-size="19">[JSON/HTTPS]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  79 -> 84 [id=85, label=<<font point-size="24">Reads from and writes to</font><br /><font point-size="19">[SQL/TCP]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  79 -> 88 [id=89, label=<<font point-size="24">Reads from and writes to</font><br /><font point-size="19">[SQL/TCP]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  79 -> 91 [id=92, label=<<font point-size="24">Makes API calls to</font><br /><font point-size="19">[XML/HTTPS]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  84 -> 88 [id=93, label=<<font point-size="24">Replicates data to</font>>, style="dashed", color="#444444", fontcolor="#444444",ltail=cluster_83,lhead=cluster_87]
                
                }""", diagram.getDefinition());
    }

    @Test
    @Tag("IntegrationTest")
    public void test_AmazonWebServicesExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/amazon-web-services.json"));
        ThemeUtils.loadThemes(workspace);
        workspace.getViews().getDeploymentViews().iterator().next().enableAutomaticLayout(AutomaticLayout.RankDirection.LeftRight, 300, 300);

        DOTExporter exporter = new DOTExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(1, diagrams.size());

        Diagram diagram = diagrams.stream().findFirst().get();
        assertEquals("""
                digraph {
                  compound=true
                  graph [fontname="Arial", rankdir=LR, ranksep=1.0, nodesep=1.0]
                  node [fontname="Arial", shape=box, margin="0.4,0.3"]
                  edge [fontname="Arial"]
                  label=<<br /><font point-size="34">Deployment View: X - Live</font>>
                
                  subgraph cluster_5 {
                    margin=25
                    label=<<font point-size="24">Amazon Web Services</font><br /><font point-size="19">[Deployment Node]</font>>
                    labelloc=b
                    color="#232f3e"
                    fontcolor="#232f3e"
                    fillcolor="#ffffff"
                
                    subgraph cluster_6 {
                      margin=25
                      label=<<font point-size="24">US-East-1</font><br /><font point-size="19">[Deployment Node]</font>>
                      labelloc=b
                      color="#147eba"
                      fontcolor="#147eba"
                      fillcolor="#ffffff"
                
                      subgraph cluster_10 {
                        margin=25
                        label=<<font point-size="24">Autoscaling group</font><br /><font point-size="19">[Deployment Node]</font>>
                        labelloc=b
                        color="#cc2264"
                        fontcolor="#cc2264"
                        fillcolor="#ffffff"
                
                        subgraph cluster_11 {
                          margin=25
                          label=<<font point-size="24">Amazon EC2 - Ubuntu server</font><br /><font point-size="19">[Deployment Node]</font>>
                          labelloc=b
                          color="#d86613"
                          fontcolor="#d86613"
                          fillcolor="#ffffff"
                
                          12 [id=12,shape=rect, label=<<font point-size="34">Web Application</font><br /><font point-size="19">[Container: Java and Spring Boot]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                        }
                
                      }
                
                      subgraph cluster_14 {
                        margin=25
                        label=<<font point-size="24">Amazon RDS</font><br /><font point-size="19">[Deployment Node]</font>>
                        labelloc=b
                        color="#3b48cc"
                        fontcolor="#3b48cc"
                        fillcolor="#ffffff"
                
                        subgraph cluster_15 {
                          margin=25
                          label=<<font point-size="24">MySQL</font><br /><font point-size="19">[Deployment Node]</font>>
                          labelloc=b
                          color="#3b48cc"
                          fontcolor="#3b48cc"
                          fillcolor="#ffffff"
                
                          16 [id=16,shape=cylinder, label=<<font point-size="34">Database Schema</font><br /><font point-size="19">[Container]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                        }
                
                      }
                
                      7 [id=7,shape=rect, label=<<font point-size="34">DNS router</font><br /><font point-size="19">[Infrastructure Node: Route 53]</font><br /><br /><font point-size="24">Routes incoming requests based<br />upon domain name.</font>>, style=filled, color="#693cc5", fillcolor="#ffffff", fontcolor="#693cc5"]
                      8 [id=8,shape=rect, label=<<font point-size="34">Load Balancer</font><br /><font point-size="19">[Infrastructure Node: Elastic Load Balancer]</font><br /><br /><font point-size="24">Automatically distributes<br />incoming application traffic.</font>>, style=filled, color="#693cc5", fillcolor="#ffffff", fontcolor="#693cc5"]
                    }
                
                  }
                
                  8 -> 12 [id=13, label=<<font point-size="24">Forwards requests to</font><br /><font point-size="19">[HTTPS]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  12 -> 16 [id=17, label=<<font point-size="24">Reads from and writes to</font><br /><font point-size="19">[MySQL Protocol/SSL]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  7 -> 8 [id=9, label=<<font point-size="24">Forwards requests to</font><br /><font point-size="19">[HTTPS]</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                
                }""", diagram.getDefinition());
    }

    @Test
    public void test_GroupsExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/groups.json"));
        ThemeUtils.loadThemes(workspace);

        DOTExporter exporter = new DOTExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(3, diagrams.size());

        Diagram diagram = diagrams.stream().filter(md -> md.getKey().equals("SystemLandscape")).findFirst().get();
        assertEquals("""
                digraph {
                  compound=true
                  graph [fontname="Arial", rankdir=TB, ranksep=1.0, nodesep=1.0]
                  node [fontname="Arial", shape=box, margin="0.4,0.3"]
                  edge [fontname="Arial"]
                  label=<<br /><font point-size="34">System Landscape View</font>>
                
                  subgraph "cluster_group_Group 1" {
                    margin=25
                    label=<<font point-size="24"><br />Group 1</font>>
                    labelloc=b
                    color="#cccccc"
                    fontcolor="#cccccc"
                    fillcolor="#ffffff"
                    style="dashed"
                
                    2 [id=2,shape=rect, label=<<font point-size="34">B</font><br /><font point-size="19">[Software System]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                  }
                
                  subgraph "cluster_group_Group 2" {
                    margin=25
                    label=<<font point-size="24"><br />Group 2</font>>
                    labelloc=b
                    color="#cccccc"
                    fontcolor="#cccccc"
                    fillcolor="#ffffff"
                    style="dashed"
                
                    3 [id=3,shape=rect, label=<<font point-size="34">C</font><br /><font point-size="19">[Software System]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                      subgraph "cluster_group_Group 3" {
                        margin=25
                        label=<<font point-size="24"><br />Group 3</font>>
                        labelloc=b
                        color="#cccccc"
                        fontcolor="#cccccc"
                        fillcolor="#ffffff"
                        style="dashed"
                
                        4 [id=4,shape=rect, label=<<font point-size="34">D</font><br /><font point-size="19">[Software System]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                      }
                
                  }
                
                  1 [id=1,shape=rect, label=<<font point-size="34">A</font><br /><font point-size="19">[Software System]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                
                  2 -> 3 [id=10, label=<>, style="dashed", color="#444444", fontcolor="#444444"]
                  3 -> 4 [id=12, label=<>, style="dashed", color="#444444", fontcolor="#444444"]
                  1 -> 2 [id=9, label=<>, style="dashed", color="#444444", fontcolor="#444444"]

                }""", diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Containers")).findFirst().get();
        assertEquals("""
                digraph {
                  compound=true
                  graph [fontname="Arial", rankdir=TB, ranksep=1.0, nodesep=1.0]
                  node [fontname="Arial", shape=box, margin="0.4,0.3"]
                  edge [fontname="Arial"]
                  label=<<br /><font point-size="34">Container View: D</font>>
                
                  3 [id=3,shape=rect, label=<<font point-size="34">C</font><br /><font point-size="19">[Software System]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                
                  subgraph cluster_4 {
                    margin=25
                    label=<<font point-size="24"><br />D</font><br /><font point-size="19">[Software System]</font>>
                    labelloc=b
                    color="#444444"
                    fontcolor="#444444"
                    fillcolor="#444444"
                
                    subgraph "cluster_group_Group 4" {
                      margin=25
                      label=<<font point-size="24"><br />Group 4</font>>
                      labelloc=b
                      color="#cccccc"
                      fontcolor="#cccccc"
                      fillcolor="#ffffff"
                      style="dashed"
                
                      6 [id=6,shape=rect, label=<<font point-size="34">F</font><br /><font point-size="19">[Container]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                    }
                
                    5 [id=5,shape=rect, label=<<font point-size="34">E</font><br /><font point-size="19">[Container]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                  }
                
                  3 -> 5 [id=11, label=<>, style="dashed", color="#444444", fontcolor="#444444"]
                  3 -> 6 [id=14, label=<>, style="dashed", color="#444444", fontcolor="#444444"]

                }""", diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Components")).findFirst().get();
        assertEquals("""
                digraph {
                  compound=true
                  graph [fontname="Arial", rankdir=TB, ranksep=1.0, nodesep=1.0]
                  node [fontname="Arial", shape=box, margin="0.4,0.3"]
                  edge [fontname="Arial"]
                  label=<<br /><font point-size="34">Component View: D - F</font>>
                
                  3 [id=3,shape=rect, label=<<font point-size="34">C</font><br /><font point-size="19">[Software System]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                
                  subgraph cluster_6 {
                    margin=25
                    label=<<font point-size="24"><br />F</font><br /><font point-size="19">[Container]</font>>
                    labelloc=b
                    color="#444444"
                    fontcolor="#444444"
                    fillcolor="#444444"
                
                    subgraph "cluster_group_Group 5" {
                      margin=25
                      label=<<font point-size="24"><br />Group 5</font>>
                      labelloc=b
                      color="#cccccc"
                      fontcolor="#cccccc"
                      fillcolor="#ffffff"
                      style="dashed"
                
                      8 [id=8,shape=rect, label=<<font point-size="34">H</font><br /><font point-size="19">[Component]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                    }
                
                    7 [id=7,shape=rect, label=<<font point-size="34">G</font><br /><font point-size="19">[Component]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                  }
                
                  3 -> 7 [id=13, label=<>, style="dashed", color="#444444", fontcolor="#444444"]
                  3 -> 8 [id=15, label=<>, style="dashed", color="#444444", fontcolor="#444444"]
                
                }""", diagram.getDefinition());
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

        DOTExporter exporter = new DOTExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);

        Diagram diagram = diagrams.stream().filter(md -> md.getKey().equals("SystemLandscape")).findFirst().get();
        assertEquals("""
                digraph {
                  compound=true
                  graph [fontname="Arial", rankdir=TB, ranksep=1.0, nodesep=1.0]
                  node [fontname="Arial", shape=box, margin="0.4,0.3"]
                  edge [fontname="Arial"]
                  label=<<br /><font point-size="34">System Landscape View</font><br /><font point-size="24">Description</font>>
                
                  subgraph "cluster_group_Organisation 1" {
                    margin=25
                    label=<<font point-size="24"><br />Organisation 1</font>>
                    labelloc=b
                    color="#cccccc"
                    fontcolor="#cccccc"
                    fillcolor="#ffffff"
                    style="dashed"
                
                    3 [id=3,shape=rect, label=<<font point-size="34">Organisation 1</font><br /><font point-size="19">[Software System]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                      subgraph "cluster_group_Department 1" {
                        margin=25
                        label=<<font point-size="24"><br />Department 1</font>>
                        labelloc=b
                        color="#cccccc"
                        fontcolor="#cccccc"
                        fillcolor="#ffffff"
                        style="dashed"
                
                        5 [id=5,shape=rect, label=<<font point-size="34">Department 1</font><br /><font point-size="19">[Software System]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                          subgraph "cluster_group_Team 1" {
                            margin=25
                            label=<<font point-size="24"><br />Team 1</font>>
                            labelloc=b
                            color="#cccccc"
                            fontcolor="#cccccc"
                            fillcolor="#ffffff"
                            style="dashed"
                
                            1 [id=1,shape=rect, label=<<font point-size="34">Team 1</font><br /><font point-size="19">[Software System]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                          }
                
                          subgraph "cluster_group_Team 2" {
                            margin=25
                            label=<<font point-size="24"><br />Team 2</font>>
                            labelloc=b
                            color="#cccccc"
                            fontcolor="#cccccc"
                            fillcolor="#ffffff"
                            style="dashed"
                
                            2 [id=2,shape=rect, label=<<font point-size="34">Team 2</font><br /><font point-size="19">[Software System]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                          }
                
                      }
                
                  }
                
                  subgraph "cluster_group_Organisation 2" {
                    margin=25
                    label=<<font point-size="24"><br />Organisation 2</font>>
                    labelloc=b
                    color="#cccccc"
                    fontcolor="#cccccc"
                    fillcolor="#ffffff"
                    style="dashed"
                
                    4 [id=4,shape=rect, label=<<font point-size="34">Organisation 2</font><br /><font point-size="19">[Software System]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                  }
                
                
                }""", diagram.getDefinition());
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

        Diagram diagram = new DOTExporter().export(containerView);
        assertEquals("""
                digraph {
                  compound=true
                  graph [fontname="Arial", rankdir=TB, ranksep=1.0, nodesep=1.0]
                  node [fontname="Arial", shape=box, margin="0.4,0.3"]
                  edge [fontname="Arial"]
                  label=<<br /><font point-size="34">Container View: Software System 1</font>>
                
                  subgraph cluster_1 {
                    margin=25
                    label=<<font point-size="24"><br />Software System 1</font><br /><font point-size="19">[Software System]</font>>
                    labelloc=b
                    color="#444444"
                    fontcolor="#444444"
                    fillcolor="#444444"
                
                    2 [id=2,shape=rect, label=<<font point-size="34">Container 1</font><br /><font point-size="19">[Container]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                  }
                
                  subgraph cluster_3 {
                    margin=25
                    label=<<font point-size="24"><br />Software System 2</font><br /><font point-size="19">[Software System]</font>>
                    labelloc=b
                    color="#cccccc"
                    fontcolor="#cccccc"
                    fillcolor="#cccccc"
                
                    4 [id=4,shape=rect, label=<<font point-size="34">Container 2</font><br /><font point-size="19">[Container]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                  }
                
                  2 -> 4 [id=5, label=<<font point-size="24">Uses</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                
                }""", diagram.getDefinition());
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

        Diagram diagram = new DOTExporter().export(componentView);
        assertEquals("""
                digraph {
                  compound=true
                  graph [fontname="Arial", rankdir=TB, ranksep=1.0, nodesep=1.0]
                  node [fontname="Arial", shape=box, margin="0.4,0.3"]
                  edge [fontname="Arial"]
                  label=<<br /><font point-size="34">Component View: Software System 1 - Container 1</font>>
                
                  subgraph cluster_2 {
                    margin=25
                    label=<<font point-size="24"><br />Container 1</font><br /><font point-size="19">[Container]</font>>
                    labelloc=b
                    color="#444444"
                    fontcolor="#444444"
                    fillcolor="#444444"
                
                    3 [id=3,shape=rect, label=<<font point-size="34">Component 1</font><br /><font point-size="19">[Component]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                  }
                
                  subgraph cluster_5 {
                    margin=25
                    label=<<font point-size="24"><br />Container 2</font><br /><font point-size="19">[Container]</font>>
                    labelloc=b
                    color="#cccccc"
                    fontcolor="#cccccc"
                    fillcolor="#cccccc"
                
                    6 [id=6,shape=rect, label=<<font point-size="34">Component 2</font><br /><font point-size="19">[Component]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                  }
                
                  3 -> 6 [id=7, label=<<font point-size="24">Uses</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                
                }""", diagram.getDefinition());
    }

    @Test
    public void test_renderGroupStyles() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addPerson("User 1").setGroup("Group 1");
        workspace.getModel().addPerson("User 2").setGroup("Group 2");
        workspace.getModel().addPerson("User 3").setGroup("Group 3");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "");
        view.addDefaultElements();

        workspace.getViews().getConfiguration().getStyles().addElementStyle("Group:Group 1").color("#111111");
        workspace.getViews().getConfiguration().getStyles().addElementStyle("Group:Group 2").color("#222222");

        DOTExporter exporter = new DOTExporter();
        Diagram diagram = exporter.export(view);
        assertEquals("""
                digraph {
                  compound=true
                  graph [fontname="Arial", rankdir=TB, ranksep=1.0, nodesep=1.0]
                  node [fontname="Arial", shape=box, margin="0.4,0.3"]
                  edge [fontname="Arial"]
                  label=<<br /><font point-size="34">System Landscape View</font>>
                
                  subgraph "cluster_group_Group 1" {
                    margin=25
                    label=<<font point-size="24"><br />Group 1</font>>
                    labelloc=b
                    color="#111111"
                    fontcolor="#111111"
                    fillcolor="#ffffff"
                    style="dashed"
                
                    1 [id=1,shape=rect, label=<<font point-size="34">User 1</font><br /><font point-size="19">[Person]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                  }
                
                  subgraph "cluster_group_Group 2" {
                    margin=25
                    label=<<font point-size="24"><br />Group 2</font>>
                    labelloc=b
                    color="#222222"
                    fontcolor="#222222"
                    fillcolor="#ffffff"
                    style="dashed"
                
                    2 [id=2,shape=rect, label=<<font point-size="34">User 2</font><br /><font point-size="19">[Person]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                  }
                
                  subgraph "cluster_group_Group 3" {
                    margin=25
                    label=<<font point-size="24"><br />Group 3</font>>
                    labelloc=b
                    color="#cccccc"
                    fontcolor="#cccccc"
                    fillcolor="#ffffff"
                    style="dashed"
                
                    3 [id=3,shape=rect, label=<<font point-size="34">User 3</font><br /><font point-size="19">[Person]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                  }

                
                }""", diagram.getDefinition());

        workspace.getViews().getConfiguration().getStyles().addElementStyle("Group").color("#aabbcc");

        diagram = exporter.export(view);
        assertEquals("""
                digraph {
                  compound=true
                  graph [fontname="Arial", rankdir=TB, ranksep=1.0, nodesep=1.0]
                  node [fontname="Arial", shape=box, margin="0.4,0.3"]
                  edge [fontname="Arial"]
                  label=<<br /><font point-size="34">System Landscape View</font>>
                
                  subgraph "cluster_group_Group 1" {
                    margin=25
                    label=<<font point-size="24"><br />Group 1</font>>
                    labelloc=b
                    color="#111111"
                    fontcolor="#111111"
                    fillcolor="#ffffff"
                    style="dashed"
                
                    1 [id=1,shape=rect, label=<<font point-size="34">User 1</font><br /><font point-size="19">[Person]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                  }
                
                  subgraph "cluster_group_Group 2" {
                    margin=25
                    label=<<font point-size="24"><br />Group 2</font>>
                    labelloc=b
                    color="#222222"
                    fontcolor="#222222"
                    fillcolor="#ffffff"
                    style="dashed"
                
                    2 [id=2,shape=rect, label=<<font point-size="34">User 2</font><br /><font point-size="19">[Person]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                  }
                
                  subgraph "cluster_group_Group 3" {
                    margin=25
                    label=<<font point-size="24"><br />Group 3</font>>
                    labelloc=b
                    color="#aabbcc"
                    fontcolor="#aabbcc"
                    fillcolor="#ffffff"
                    style="dashed"
                
                    3 [id=3,shape=rect, label=<<font point-size="34">User 3</font><br /><font point-size="19">[Person]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                  }

                
                }""", diagram.getDefinition());
    }

    @Test
    public void test_renderCustomView() {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();

        CustomElement a = model.addCustomElement("A");
        CustomElement b = model.addCustomElement("B", "Custom", "Description");
        a.uses(b, "Uses");

        CustomView view = workspace.getViews().createCustomView("key", "Title", "Description");
        view.addDefaultElements();

        Diagram diagram = new DOTExporter().export(view);
        assertEquals("""
                digraph {
                  compound=true
                  graph [fontname="Arial", rankdir=TB, ranksep=1.0, nodesep=1.0]
                  node [fontname="Arial", shape=box, margin="0.4,0.3"]
                  edge [fontname="Arial"]
                  label=<<br /><font point-size="34">Title</font><br /><font point-size="24">Description</font>>
                
                  1 [id=1,shape=rect, label=<<font point-size="34">A</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                  2 [id=2,shape=rect, label=<<font point-size="34">B</font><br /><font point-size="19">[Custom]</font><br /><br /><font point-size="24">Description</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                
                  1 -> 2 [id=3, label=<<font point-size="24">Uses</font>>, style="dashed", color="#444444", fontcolor="#444444"]
                  
                }""", diagram.getDefinition());
    }

    @Test
    public void test_writeContainerViewWithGroupedElements_WithAndWithoutAGroupSeparator() {
        Workspace workspace = new Workspace("Name", "");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "");
        Container container1 = softwareSystem.addContainer("Container 1");
        container1.setGroup("Group 1");
        Container container2 = softwareSystem.addContainer("Container 2");
        container2.setGroup("Group 2");

        ContainerView view = workspace.getViews().createContainerView(softwareSystem, "Containers", "");
        view.addAllElements();

        DOTExporter exporter = new DOTExporter();
        Diagram diagram = exporter.export(view);
        assertEquals("""
                digraph {
                  compound=true
                  graph [fontname="Arial", rankdir=TB, ranksep=1.0, nodesep=1.0]
                  node [fontname="Arial", shape=box, margin="0.4,0.3"]
                  edge [fontname="Arial"]
                  label=<<br /><font point-size="34">Container View: Software System</font>>
                
                  subgraph cluster_1 {
                    margin=25
                    label=<<font point-size="24"><br />Software System</font><br /><font point-size="19">[Software System]</font>>
                    labelloc=b
                    color="#444444"
                    fontcolor="#444444"
                    fillcolor="#444444"
                
                    subgraph "cluster_group_Group 1" {
                      margin=25
                      label=<<font point-size="24"><br />Group 1</font>>
                      labelloc=b
                      color="#cccccc"
                      fontcolor="#cccccc"
                      fillcolor="#ffffff"
                      style="dashed"
                
                      2 [id=2,shape=rect, label=<<font point-size="34">Container 1</font><br /><font point-size="19">[Container]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                    }
                
                    subgraph "cluster_group_Group 2" {
                      margin=25
                      label=<<font point-size="24"><br />Group 2</font>>
                      labelloc=b
                      color="#cccccc"
                      fontcolor="#cccccc"
                      fillcolor="#ffffff"
                      style="dashed"
                
                      3 [id=3,shape=rect, label=<<font point-size="34">Container 2</font><br /><font point-size="19">[Container]</font>>, style=filled, color="#444444", fillcolor="#ffffff", fontcolor="#444444"]
                    }
                
                  }
                
                }""", diagram.getDefinition());
    }

}