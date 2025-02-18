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

            https = -> {
                technology "HTTPS"
            }
        }

        a = softwareSystem "A"

        x = softwareSystem "X" {
            customerService = microservice "Customer Service" {
                db = datastore "Customer database"
                api = springBootApplication "Customer API" {
                    customerController = restController "Customer Controller" {
                        a --https-> this "Makes API calls using"
                    }
                    customerRepository = repository "Customer Repository" {
                        customerController -> this
                        this -> db
                    }
                }
            }
        }
    }

}