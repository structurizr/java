workspace {

    model {
        user = person "User"

        group = group "Group" {
            softwareSystem "A"
            softwareSystem "B"
            softwareSystem "C"
        }

        !elements group {
            user -> this "Uses"
        }
    }

}