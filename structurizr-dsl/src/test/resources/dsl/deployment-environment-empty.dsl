workspace {
    model {
        de = deploymentEnvironment "DeploymentEnvironment"

        !element de {
            dn = deploymentNode "DeploymentNode"
        }
    }
}