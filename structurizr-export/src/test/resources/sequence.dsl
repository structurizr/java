workspace {

    model {
        properties {
            structurizr.groupSeparator /
        }

        a = softwareSystem "A" {
            b = container "B" {
                c = component "C"
            }
        }
        d = softwareSystem "d" {
            group1 = group "group1" {
                e = container "e" {
                    group2 = group "group2" {
                        f = component "f"
                        g = component "g"
                        h = component "h"
                    }
                }
            }
        }

        b -> e
        f -> h
        c -> f
        f -> g

    }

    views {
        styles {
            element "Software System" {
                background "LightBlue"
            }
            element "Container" {
                background "Beige"
            }
        }
        properties {
            "plantuml.sequenceDiagram" "true"
            "plantuml.teoz" "true"
            "mermaid.sequenceDiagram" "true"
            "mermaid.title" "true"
            "structurizr.softwareSystemBoundaries" "true"
        }
        
        dynamic a "Sequence-SoftwareSystem" {
            b -> e
            autoLayout
        }
            
        dynamic e "Sequence-Container" {
            f -> h
            c -> f
            f -> g
            autoLayout
        }
    }

}
