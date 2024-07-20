workspace {

    model {
        a = softwareSystem "A"
        b = softwareSystem "B"
        c = softwareSystem "C"

        a -> b
        b -> c
    }

    views {
        dynamic * {
            2: a -> b
            3: b -> c
        }
    }

}