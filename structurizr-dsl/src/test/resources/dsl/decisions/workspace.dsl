workspace {

    !adrs adrtools com.structurizr.example.ExampleDecisionImporter

    model {
        softwareSystem = softwareSystem "Software System" {
            !decisions adrtools

            container "Container" {
                !decisions madr madr

                component "Component" {
                    !decisions log4brains log4brains
                }
            }
        }
    }

}