workspace {

    model {
        archetypes {
            hardwareSystem = element {
                metadata "Hardware System"
                tag "Hardware System"
            }
        }

        a = softwareSystem "A"
        b = hardwareSystem "B"

        a -> b "Gets data from"
    }

}