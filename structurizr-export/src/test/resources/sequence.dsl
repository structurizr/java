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
        properties {
            "structurizr.softwareSystemBoundaries" "true"
            "plantuml.sequenceDiagram" "true"
            "plantuml.teoz" "true"
            "mermaid.sequenceDiagram" "true"
            "mermaid.title" "true"
        }
            
        dynamic e "Sequence-Container" {
            f -> h
            c -> f
            f -> g
            autoLayout
        }
    }

}
