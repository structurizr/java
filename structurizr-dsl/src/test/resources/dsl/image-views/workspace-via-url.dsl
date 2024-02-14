workspace {

    views {
        properties {
            "plantuml.url" "http://localhost:7777"
            "plantuml.format" "svg"
            "mermaid.url" "http://localhost:8888"
            "mermaid.format" "svg"
            "kroki.url" "http://localhost:9999"
            "kroki.format" "svg"
        }

        image * "plantuml" {
            plantuml https://raw.githubusercontent.com/structurizr/java/master/structurizr-dsl/src/test/resources/dsl/image-views/diagram.puml
        }

        image * "mermaid" {
            mermaid https://raw.githubusercontent.com/structurizr/java/master/structurizr-dsl/src/test/resources/dsl/image-views/diagram.mmd
        }

        image * "kroki" {
            kroki graphviz https://raw.githubusercontent.com/structurizr/java/master/structurizr-dsl/src/test/resources/dsl/image-views/diagram.dot
        }

        image * "png" {
            image https://raw.githubusercontent.com/structurizr/java/master/structurizr-dsl/src/test/resources/dsl/image-views/image.png
        }

        image * "svg" {
            image https://raw.githubusercontent.com/structurizr/java/master/structurizr-dsl/src/test/resources/dsl/image-views/image.svg
        }
    }

}