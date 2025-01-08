workspace {

    model {
        archetypes {
            application = container {
                tag "Application"
            }
            datastore = container {
                tag "Datastore"
            }

            microservice = group

            springBootApplication = application {
                technology "Spring Boot"
                tags "Spring Boot"
            }

            restController = component {
                technology "Spring MVC REST Controller"
                tag "Spring MVC REST Controller"
            }
            repository = component {
                technology "Spring Data Repository"
                tag "Spring Data Repository"
            }
        }

        x = softwareSystem "X" {
            customerService = microservice "Customer Service" {
                db = datastore "Customer database"
                api = springBootApplication "Customer API" {
                    customerController = restController "Customer Controller"
                    customerRepository = repository "Customer Repository" {
                        customerController -> this
                        this -> db
                    }
                }
            }
        }
    }

}