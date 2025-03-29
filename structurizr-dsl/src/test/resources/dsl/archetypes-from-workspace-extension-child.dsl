workspace extends archetypes-from-workspace-extension-parent.dsl {

    model {
        a = softwareSystem "A"
        b = softwareSystem "B"
        a -> b
    }

}