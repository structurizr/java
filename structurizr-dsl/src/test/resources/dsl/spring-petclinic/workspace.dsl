workspace "Spring PetClinic" "A C4 model of the Spring PetClinic sample app (https://github.com/spring-projects/spring-petclinic/)" {

    // this example requires an environment variable as follows:
    // - Name: SPRING_PETCLINIC_HOME
    // - Value: the full path to the location of the spring-petclinic example (e.g. /Users/simon/spring-petclinic)

    !identifiers hierarchical

    model {
        clinicEmployee = person "Clinic Employee" "An employee of the clinic."
        springPetClinic = softwareSystem "Spring PetClinic" "Allows employees to view and manage information regarding the veterinarians, the clients, and their pets." {
            relationalDatabaseSchema = container "Relational Database Schema" {
                description "Stores information regarding the veterinarians, the clients, and their pets."
                technology "Relational Database Schema"
                url "https://github.com/spring-projects/spring-petclinic/tree/main/src/main/resources/db"
                tag "Relational Database Schema"
            }

            webApplication = container "Web Application" {
                description "Allows employees to view and manage information regarding the veterinarians, the clients, and their pets."
                technology "Java and Spring"

                !components {
                    classes "${SPRING_PETCLINIC_HOME}/target/spring-petclinic-3.3.0-SNAPSHOT.jar"
                    source "${SPRING_PETCLINIC_HOME}/src/main/java"
                    filter include fqn-regex "org.springframework.samples.petclinic..*"
                    strategy {
                        technology "Spring MVC Controller"
                        matcher annotation "org.springframework.stereotype.Controller"
                        filter exclude fqn-regex ".*.CrashController"
                        url prefix-src "https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java"
                        forEach {
                            clinicEmployee -> this "Uses"
                            tag "Spring MVC Controller"
                        }
                    }
                    strategy {
                        technology "Spring Data Repository"
                        matcher implements "org.springframework.data.repository.Repository"
                        description first-sentence
                        url prefix-src "https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java"
                        forEach {
                            -> relationalDatabaseSchema "Reads from and writes to"
                            tag "Spring Data Repository"
                        }
                    }
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