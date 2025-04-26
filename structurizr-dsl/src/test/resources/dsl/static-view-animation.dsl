workspace {

    model {
        a = softwareSystem "A"
        b = softwareSystem "B"

        a -> b
    }
         
    views {
        systemLandscape {
            include *

            // add animation steps via element identifiers
            animation {
                a
                b
            }
        }

        systemLandscape {
            include *

            // add animation steps via element expressions
            animation {
                a
                a->
            }
        }
    }

}