workspace {
    model {
        archetypes {
            https = -> {
                technology "HTTPS"
                tag "HTTPS"
            }
        }

        a = softwareSystem "A"
        b = softwareSystem "B" {
            --https-> a "Makes API calls to"
        }
    }
}