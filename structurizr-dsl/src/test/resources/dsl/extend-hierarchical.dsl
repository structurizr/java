workspace {

    !identifiers hierarchical

    model {
         a = softwareSystem "A" {
            b = container "B" {
                c = component "C"

                !extend c {
                    properties {
                        "Name1" "Value1"
                    }
                }
            }

            !extend b.c {
                properties {
                    "Name2" "Value2"
                }
            }
        }

        !extend a.b.c {
            properties {
                "Name3" "Value3"
            }
        }
    }

}