workspace "Amazon Web Services Example" "An example AWS deployment architecture." {

    !identifiers hierarchical

    model {
        x = softwaresystem "X" {
            wa = container "Web Application" {
                technology "Java and Spring Boot"
                tags "Application"
            }
            db = container "Database Schema" {
                tags "Database"
            }

            wa -> db "Reads from and writes to" "MySQL Protocol/SSL"
        }

        live = deploymentEnvironment "Live" {
            deploymentNode "Amazon Web Services" {
                tags "Amazon Web Services - Cloud"

                region = deploymentNode "US-East-1" {
                    tags "Amazon Web Services - Region"

                    dns = infrastructureNode "DNS router" {
                        technology "Route 53"
                        description "Routes incoming requests based upon domain name."
                        tags "Amazon Web Services - Route 53"
                    }

                    lb = infrastructureNode "Load Balancer" {
                        technology "Elastic Load Balancer"
                        description "Automatically distributes incoming application traffic."
                        tags "Amazon Web Services - Elastic Load Balancing"
                        dns -> this "Forwards requests to" "HTTPS"
                    }

                    deploymentNode "Autoscaling group" {
                        tags "Amazon Web Services - Auto Scaling"

                        deploymentNode "Amazon EC2 - Ubuntu server" {
                            tags "Amazon Web Services - EC2"

                            webApplicationInstance = containerInstance x.wa {
                                lb -> this "Forwards requests to" "HTTPS"
                            }
                        }
                    }

                    deploymentNode "Amazon RDS" {
                        tags "Amazon Web Services - RDS"

                        deploymentNode "MySQL" {
                            tags "Amazon Web Services - RDS MySQL instance"

                            databaseInstance = containerInstance x.db
                        }
                    }

                }
            }
        }
    }

    views {
        deployment x live "AmazonWebServicesDeployment" {
            include *
            autolayout lr
        }

        styles {
            element "Application" {
                shape roundedbox
            }
            element "Database" {
                shape cylinder
            }
        }

        themes https://static.structurizr.com/themes/amazon-web-services-2020.04.30/theme.json
    }

}