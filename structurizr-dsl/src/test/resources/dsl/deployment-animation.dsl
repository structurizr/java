workspace {

    model {
        ss = softwaresystem "Software System" {
            webapp = container "Web Application" {
                tag "UI"
            }
            db = container "Database Schema" {
                tag "DB"
            }
        }

        webapp -> db

        live = deploymentEnvironment "Live" {
            dn = deploymentNode "Deployment Node" {
                webappInstance = containerInstance webapp
                dbInstance = containerInstance db
            }
        }
    }
         
    views {
        deployment ss "Live" {
            include *

            // add animation steps via container instance identifiers
            animation {
                webappInstance
                dbInstance
            }
        }

        deployment ss "Live" {
            include *

            // add animation steps via container identifiers
            animation {
                webapp
                db
            }
        }

        deployment ss "Live" {
            include *

            // add animation steps via element expressions
            animation {
                webapp
                webapp->
            }
        }

        deployment ss "Live" {
            include *

            // add animation steps via element expressions
            animation {
                webappInstance
                webappInstance->
            }
        }

        deployment ss "Live" {
            include *

            // add animation steps via element expressions
            animation {
                element.tag==UI
                element.tag==DB
            }
        }
    }

}