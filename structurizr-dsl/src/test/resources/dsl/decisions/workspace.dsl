workspace {

    !adrs adrtools com.structurizr.example.ExampleDecisionImporter

    model {
        softwareSystem = softwareSystem "Software System" {
            !decisions adrtools

            container "Container" {
                !decisions adrtools adrtools

                component "Component" {
                    !decisions log4brains log4brains
                }
            }
        }
    }

}