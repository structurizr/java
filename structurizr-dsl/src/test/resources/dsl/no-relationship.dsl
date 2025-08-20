workspace {

    !identifiers hierarchical

    model {
        ss = softwareSystem "Software System" {
            ui = container "UI" "Description" "JavaScript and React"
            backend = container "Backend" "Description" "Spring Boot"

            ui -> backend "Makes API requests to" "JSON/HTTPS"
        }

        // ui -> backend
        one = deploymentEnvironment "One" {
            deploymentNode "Developer's Computer" {
                deploymentNode "Web Browser" {
                    instanceOf ss.ui
                }
                instanceOf ss.backend
            }
        }

        // ui -> loadbalancer -> backend
        // configured via container identifiers
        two = deploymentEnvironment "Two" {
            deploymentNode "User's Computer" {
                deploymentNode "Web Browser" {
                    instanceOf ss.ui
                }
            }
            dc = deploymentNode "Data Center" {
                loadBalancer = infrastructureNode "Load Balancer"
                deploymentNode "Server" {
                    instanceOf ss.backend
                }
            }

            ss.ui -/> ss.backend {
                ss.ui -> dc.loadBalancer
                dc.loadBalancer -> ss.backend "Forwards API requests to" ""
            }
        }

        // ui -> loadbalancer -> backend
        // configured via container instance identifiers
        three = deploymentEnvironment "Three" {
            computer = deploymentNode "User's Computer" {
                webbrowser = deploymentNode "Web Browser" {
                    ui = instanceOf ss.ui
                }
            }
            datacenter = deploymentNode "Data Center" {
                loadbalancer = infrastructureNode "Load Balancer"
                server = deploymentNode "Server" {
                    backend = instanceOf ss.backend
                }
            }

            computer.webbrowser.ui -/> datacenter.server.backend {
                computer.webbrowser.ui -> datacenter.loadbalancer
                datacenter.loadbalancer -> datacenter.server.backend "Forwards API requests to" ""
            }
        }
    }

    views {
        container ss {
            include *
            autolayout lr
        }

        deployment ss one {
            include *
            autolayout lr
        }

        deployment ss two {
            include *
            autolayout lr
        }

        deployment ss three {
            include *
            autolayout lr
        }
    }

}