workspace extends workspace.dsl {

    model {
        !element softwareSystem1 {
            webapp = container "Web Application"
        }

        user -> softwareSystem1 "Uses"
        softwareSystem3.webapp -> softwareSystem3.db
    }

}