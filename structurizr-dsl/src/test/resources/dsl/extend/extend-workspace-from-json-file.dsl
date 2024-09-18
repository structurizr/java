workspace extends workspace.json {

    model {
        // !element with DSL identifier
        !element softwareSystem1 {
            webapp1 = container "Web Application 1"
        }

        // !element with canonical name
        !element "SoftwareSystem://Software System 1" {
            webapp2 = container "Web Application 2"
        }

        user -> softwareSystem1 "Uses"
        softwareSystem3.webapp -> softwareSystem3.db
    }

}