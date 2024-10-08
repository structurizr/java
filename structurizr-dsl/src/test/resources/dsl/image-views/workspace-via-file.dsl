workspace {

    views {
        properties {
            "plantuml.url" "http://localhost:7777"
            "mermaid.url" "http://localhost:8888"
            "mermaid.compress" "false"
            "kroki.url" "http://localhost:9999"
        }

        image * "plantuml" {
            plantuml diagram.puml
        }

        image * "mermaid" {
            mermaid diagram.mmd
        }

        image * "kroki" {
            kroki graphviz diagram.dot
        }

        image * "png" {
            image image.png
        }

        image * "svg" {
            image image.svg
        }
    }

}