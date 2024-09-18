workspace {

    !identifiers hierarchical

    model {
         a = softwareSystem "A" {
            b = container "B" {
                c = component "C"

                !element c {
                    properties {
                        "Name1" "Value1"
                    }
                }
            }

            !element b.c {
                properties {
                    "Name2" "Value2"
                }
            }
        }

        !element a.b.c {
            properties {
                "Name3" "Value3"
            }
        }
    }

}