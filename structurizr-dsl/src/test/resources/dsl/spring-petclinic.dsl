 workspace "Spring PetClinic" "A C4 model of the Spring PetClinic sample app (https://github.com/spring-projects/spring-petclinic/)" {

    !identifiers hierarchical

    model {
        clinicEmployee = person "Clinic Employee" "An employee of the clinic."
        springPetClinic = softwareSystem "Spring PetClinic" "Allows employees to view and manage information regarding the veterinarians, the clients, and their pets." {
            relationalDatabaseSchema = container "Relational Database Schema" {
                description "Stores information regarding the veterinarians, the clients, and their pets."
                technology "Relational Database Schema"
                tag "Relational Database Schema"
            }

            webApplication = container "Web Application" {
                description "Allows employees to view and manage information regarding the veterinarians, the clients, and their pets."
                technology "Java and Spring"

                !components {
                    classes "${SPRING_PETCLINIC_DIR}/target/spring-petclinic-3.3.0-SNAPSHOT.jar"
                    source "${SPRING_PETCLINIC_DIR}/src/main/java"
                    strategy {
                        matcher annotation "org.springframework.stereotype.Controller" "Spring MVC Controller"
                        filter excludeRegex ".*.CrashController"
                    }
                    strategy {
                        matcher implements "org.springframework.data.repository.Repository" "Spring Data Repository"
                    }
                }

                !script groovy {
                    element.components.each { it.url = it.properties["component.src"].replace(context.dslParser.getConstant("SPRING_PETCLINIC_DIR") + "/src/main/java", "https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java") }
                }

                !elements "element.parent==webApplication && element.technology==Spring MVC Controller" {
                    clinicEmployee -> this "Uses"
                    tag "Spring MVC Controller"
                }

                !elements "element.parent==webApplication && element.technology==Spring Data Repository" {
                    -> relationalDatabaseSchema "Reads from and writes to"
                    tag "Spring Data Repository"
                }
            }
        }
    }

    views {
        systemContext springPetClinic "SystemContext" {
            include *
            autolayout
        }

        container springPetClinic "Containers" {
            include *
            autolayout
        }

        component springPetClinic.webApplication "Components" {
            include *
            autolayout
        }

        styles {
            element "Person" {
                shape person
                background #519823
                color #FFFFFF
            }

            element "Software System" {
                background #6CB33E
                color #FFFFFF
            }

            element "Container" {
                background #91D366
                color #FFFFFF
            }

            element "Relational Database Schema" {
                shape cylinder
            }

            element "Spring MVC Controller" {
                background #D4F3C0
                color #000000
            }

            element "Spring Data Repository" {
                background #95D46C
                color #000000
            }
        }
    }

}