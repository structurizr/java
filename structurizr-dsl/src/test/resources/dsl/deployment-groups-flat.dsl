workspace {

    model {
        softwareSystem = softwareSystem "Software System" {
            database = container "DB"
            api = container "API" {
                -> database "Uses"
            }
        }

        deploymentEnvironment "WithoutDeploymentGroups" {
            deploymentNode "Server 1" {
                containerInstance api
                containerInstance database
            }
            deploymentNode "Server 2" {
                containerInstance api
                containerInstance database
            }
        }

        deploymentEnvironment "WithDeploymentGroups" {
            serviceInstance1 = deploymentGroup "Service Instance 1"
            serviceInstance2 = deploymentGroup "Service Instance 2"
            deploymentNode "Server 1" {
                containerInstance api serviceInstance1
                containerInstance database serviceInstance1
            }
            deploymentNode "Server 2" {
                containerInstance api serviceInstance2
                containerInstance database serviceInstance2
            }
        }
    }

}