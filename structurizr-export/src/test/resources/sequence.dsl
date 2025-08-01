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
            e = container "e" {
                f = component "f"
                g = component "g"
                h = component "h"
            }
        }

        b -> e
        f -> h
        c -> f
        f -> g

    }

    views {
    
        dynamic a "Sequence-SoftwareSystem" {
            b -> e
            properties {
                "plantuml.sequenceDiagram" "true"
                "plantuml.teoz" "true"
                "mermaid.sequenceDiagram" "true"
                "mermaid.title" "true"
                "structurizr.softwareSystemBoundaries" "true"
            }
            autoLayout
        }
            
        dynamic e "Sequence-Container" {
            f -> h
            c -> f
            f -> g
            properties {
                "plantuml.sequenceDiagram" "true"
                "plantuml.teoz" "true"
                "mermaid.sequenceDiagram" "true"
                "mermaid.title" "true"
                "structurizr.softwareSystemBoundaries" "true"
            }
            autoLayout
        }
    }

}
