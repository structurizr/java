workspace {

    model {
        archetypes {
            softwaresystem {
                description "Default Description"
                tag "Default Tag"
            }

            -> {
                description "Default Description"
                technology "Default Technology"
                tag "Default Tag"
            }
        }

        a = softwareSystem "A"
        b = softwareSystem "B"
        a -> b
    }

}