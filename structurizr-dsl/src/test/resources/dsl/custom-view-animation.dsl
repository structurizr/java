workspace {

    model {
        a = element "A"
        b = element "B"

        a -> b
    }
         
    views {
        custom {
            include *

            // add animation steps via element identifiers
            animation {
                a
                b
            }
        }

        custom {
            include *

            // add animation steps via element expressions
            animation {
                a
                a->
            }
        }
    }

}