workspace {

    views {
        properties {
            "plantuml.url" "https://plantuml.com/plantuml"
        }

        !const SOURCE """
            class MyClass
            """

        !var STYLES """
            <style>
            root {
                BackgroundColor: #ffffff;
            }
            </style>
        """

        image * "image" {
            plantuml """
                 @startuml

                 ${STYLES}

                 ${SOURCE}
                 @enduml
             """
        }
    }

}