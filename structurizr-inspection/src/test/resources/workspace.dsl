workspace {

    model {
        user = person "User"
        softwareSystem1 = softwareSystem "Software System 1" {
            container1 = container "Container 1" {
                component1 = component "Component 1"
            }
        }

        user -> component1

        deploymentEnvironment "dev" {
            deploymentNode "Server 1" {
                infrastructureNode "Load Balancer"
                containerInstance container1
            }
        }
    }

    configuration {
        scope softwaresystem
    }

}