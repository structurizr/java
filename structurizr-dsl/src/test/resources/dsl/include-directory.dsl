workspace {

    model {
        !var SOFTWARE_SYSTEM_NAME "Software System 1"
        !include include/model/software-system/model.dsl

        !var SOFTWARE_SYSTEM_NAME "Software System 2"
        !include include/model/software-system

        !var SOFTWARE_SYSTEM_NAME "Software System 3"
        !include include/model
    }

}