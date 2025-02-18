workspace {

    model {
        archetypes {
            softwaresystem {
                tag "Default Tag"
            }

            externalSoftwareSystem = softwareSystem {
                tag "External Software System"
            }

            -> {
                technology "Default Technology"
                tag "Default Tag"
            }

            https = -> {
                technology "HTTPS"
                tag "HTTPS"
            }
        }

        a = softwareSystem "A" "Description of A."
        b = externalSoftwareSystem "B" "Description of B."
        a --https-> b "Makes API calls to"
    }

}