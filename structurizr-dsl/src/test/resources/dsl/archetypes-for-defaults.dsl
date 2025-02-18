workspace {

    model {
        archetypes {
            softwaresystem {
                description "Default Description"
                tag "Default Tag"

                properties {
                    "Default Property Name" "Default Property Value"
                }
            }

            -> {
                description "Default Description"
                technology "Default Technology"
                tag "Default Tag"

                properties {
                    "Default Property Name" "Default Property Value"
                }
            }
        }

        a = softwareSystem "A"
        b = softwareSystem "B"
        a -> b
    }

}