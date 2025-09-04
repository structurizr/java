workspace {

    views {
        properties {
            "plantuml.url" "http://localhost:7777"
            "mermaid.url" "http://localhost:8888"
            "mermaid.compress" "false"
            "kroki.url" "http://localhost:9999"
        }

        image * "plantuml" {
            plantuml """
                @startuml
                Bob -> Alice : hello
                @enduml
            """
        }

        image * "mermaid" {
            mermaid """
                flowchart TD
                    Start --> Stop
            """
        }

        image * "kroki" {
            kroki graphviz """
                digraph G {Hello->World}

            """
        }
    }

}