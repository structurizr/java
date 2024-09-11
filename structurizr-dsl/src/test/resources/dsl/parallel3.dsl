workspace {

    model {
        a = softwareSystem "A"
        b = softwareSystem "B"
        c = softwareSystem "C"
        d = softwareSystem "D"
        e = softwareSystem "E"

        a -> b
        b -> c
        b -> d
        b -> e
    }

    views {

        dynamic * {
            1: a -> b "Makes a request to"
            2: b -> c "Gets data from"
            2: b -> d "Gets data from"
            3: b -> e "Sends data to"

            autoLayout
        }
    }

}