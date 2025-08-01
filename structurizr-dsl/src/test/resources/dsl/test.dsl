!const ORGANISATION_NAME "Organisation"
!const GROUP_NAME "Group"

!var name abc
!var name ABC

workspace "Name" "Description" {

    /*
        multi-line comment
    */

    /**
        multi-line comment
    */

    /* multi-line comment on single line */

    /* multi-line comment
        on two lines */

    # single line comment
    // single line comment

    model {
        !impliedRelationships false
        !impliedRelationships "com.structurizr.model.CreateImpliedRelationshipsUnlessSameRelationshipExistsStrategy"
        !impliedRelationships true

        // single line comment with long line split character \
        properties {
            "Name" "Value"
        }

        box1 = element "Box 1" "Metadata" "Description" "Tag"
        box2 = element "Box 2" "Metadata" "Description" "Tag"
        box1 -> box2

        user = person "User" "Description" "Tag" {
            url "https://structurizr.com"
            properties {
                "Name" "Value"
            }
            perspectives {
                "Security" "A description..."
                "Technical Debt" "Tech debt is high due to delivering feature X rapidly." "High"
            }
        }

        group "${ORGANISATION_NAME} - ${GROUP_NAME}" {
            softwareSystem = softwareSystem "Software System" "Description"  "Tag" {
                webApplication = container "Web Application" "Description" "Technology" "Tag" {
                    homePageController = component "HomePageController" "Description" "Spring MVC Controller" "Tag" {
                        url "https://structurizr.com"
                        properties {
                            "Name" "Value"
                        }
                        perspectives {
                            "Security" "A description..."
                        }
                    }

                    url "https://structurizr.com"
                    properties {
                        "Name" "Value"
                    }
                    perspectives {
                        "Security" "A description..."
                    }

                    !elements "element.parent==webApplication && element.technology==Spring MVC Controller" {
                        tag "Tag 1"
                        tags "Tag 2, Tag 3"
                        url "https://example.com"
                        properties {
                            "type" "Spring MVC Controller"
                        }
                        perspectives {
                            "Owner" "Team A"
                        }
                    }
                }

                url "https://structurizr.com"
                properties {
                    "Name" "Value"
                }
                perspectives {
                    "Security" "A description..."
                }
            }

            softwareSystem "E-mail System" "Description" "Tag"
        }

        user -> HomePageController "Visits" "HTTPS" "Tag"

        developmentEnvironment = deploymentEnvironment "Development" {
            deploymentNode "Amazon Web Services" "Description" "Technology" "Tag" {
                softwareSystemInstance softwareSystem {
                    url "https://structurizr.com"
                    properties {
                        "Name" "Value"
                    }
                    perspectives {
                        "Security" "A description..."
                    }
                    healthCheck "Check 1" "https://example.com/health"
                    healthCheck "Check 2" "https://example.com/health" 60
                    healthCheck "Check 2" "https://example.com/health" 120 1000
                }
                instanceOf softwareSystem {
                    url "https://structurizr.com"
                    properties {
                        "Name" "Value"
                    }
                    perspectives {
                        "Security" "A description..."
                    }
                    healthCheck "Check 1" "https://example.com/health"
                    healthCheck "Check 2" "https://example.com/health" 60
                    healthCheck "Check 2" "https://example.com/health" 120 1000
                }
            }
        }

        deploymentEnvironment "Live" {
            deploymentNode "Amazon Web Services" "Description" "Technology" "Tag" {

                infrastructureNode "Elastic Load Balancer" "Description" "Technology" "Tag" {
                    url "https://structurizr.com"
                    properties {
                        "Name" "Value"
                    }
                    perspectives {
                        "Security" "A description..."
                    }
                }

                deploymentNode "Amazon Web Services - EC2" "Description" "Technology" "Tag" {
                    containerInstance webApplication {
                        url "https://structurizr.com"
                        properties {
                            "Name" "Value"
                        }
                        perspectives {
                            "Security" "A description..."
                        }
                        healthCheck "Check 1" "https://example.com/health"
                        healthCheck "Check 2" "https://example.com/health" 60
                        healthCheck "Check 2" "https://example.com/health" 120 1000
                    }
                    instanceOf webApplication {
                        url "https://structurizr.com"
                        properties {
                            "Name" "Value"
                        }
                        perspectives {
                            "Security" "A description..."
                        }
                        healthCheck "Check 1" "https://example.com/health"
                        healthCheck "Check 2" "https://example.com/health" 60
                        healthCheck "Check 2" "https://example.com/health" 120 1000
                    }
                }

                url "https://structurizr.com"
                properties {
                    "Name" "Value"
                }
                perspectives {
                    "Security" "A description..."
                }

            }
        }

        !relationships "*->*" {
            tag "Tag 1"
            tags "Tag 2, Tag 3"
            url "https://example.com"
            properties {
                name value
            }
            perspectives {
                name value
            }
        }
    }
         
    views {

        custom "CustomDiagram" "Title" "Description" {
            title "Title"
            description "Description"

            include box1 box2

            animation {
                box1
                box2
            }

            autolayout

            properties {
                "Name" "Value"
            }

            default
        }

        systemLandscape "SystemLandscape" "Description" {
            title "Title"
            description "Description"

            include *
            autoLayout

            properties {
                "Name" "Value"
            }

            default
        }

        systemContext softwareSystem "SystemContext" "Description" {
            title "Title"
            description "Description"

            include *
            autoLayout

            properties {
                "Name" "Value"
            }

            default
        }

        container softwareSystem "Containers" "Description" {
            title "Title"
            description "Description"

            include *
            autoLayout

            properties {
                "Name" "Value"
            }

            default
        }

        component webApplication "Components" "Description" {
            title "Title"
            description "Description"

            include *
            autoLayout

            properties {
                "Name" "Value"
            }

            default
        }

        filtered "SystemLandscape" include "Element,Relationship" "Filtered1"

        filtered "SystemLandscape" include "Element,Relationship" "Filtered2" {
            title "Filtered view"
            description "Description"

            properties {
                "Name" "Value"
            }

            default
        }

        dynamic webApplication "Dynamic" "Description" {
            title "Title"
            description "Description"

            user -> homePageController "Requests via web browser"
            homePageController -> user {
                url "https://structurizr.com"
                properties {
                    "Name" "Value"
                }
            }

            autoLayout

            properties {
                "Name" "Value"
            }

            default
        }

        deployment * developmentEnvironment "Deployment-Development" "Description" {
            title "Title"
            description "Description"

            include *
            autoLayout

            properties {
                "Name" "Value"
            }

            default
        }

        deployment * "Live" "Deployment-Live" "Description" {
            title "Title"
            description "Description"

            include *
            autoLayout

            properties {
                "Name" "Value"
            }

            default
        }

        styles {
            element "Element" {
                shape roundedbox
                icon logo.png
                iconPosition left
                width 450
                height 300
                background #ffffff
                color #000000
                colour #000000
                stroke #777777
                fontSize 24
                border solid
                opacity 50
                metadata false
                description false
                properties {
                    "Name" "Value"
                }
            }

            relationship "Relationship" {
                thickness 2
                color #777777
                colour #777777
                dashed true
                routing curved
                fontSize 24
                width 400
                position 50
                opacity 50
                properties {
                    "Name" "Value"
                }
            }

            light {
                element "Element" {
                    background #ffffff
                }

                relationship "Relationship" {
                    color #777777
                }
            }

            dark {
                element "Element" {
                    background #000000
                }

                relationship "Relationship" {
                    color #777777
                }
            }

            theme https://example.com/theme1
            themes https://example.com/theme2 https://example.com/theme3
        }

        theme https://example.com/theme1
        themes https://example.com/theme2 https://example.com/theme3

        branding {
            logo logo.png
            font "Example" https://example/com/font
        }

        terminology {
            person "Person"
            softwareSystem "Software System"
            container "Container"
            component "Component"
            deploymentNode "Deployment Node"
            infrastructureNode "Infrastructure Node"
            relationship "Relationship"
            metadata angle
        }

        properties {
            "Name" "Value"
        }
    }

    configuration {
        users {
            user1@example.com read
            user2@example.com write
        }

        visibility public
        scope softwaresystem
    }

}