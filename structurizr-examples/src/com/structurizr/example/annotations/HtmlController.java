package com.structurizr.example.annotations;

import com.structurizr.annotation.Component;
import com.structurizr.annotation.UsedByPerson;
import com.structurizr.annotation.UsesComponent;

@Component(description = "Serves HTML pages to users.", technology = "Java")
@UsedByPerson(name = "User", description = "Uses", technology = "HTTPS")
class HtmlController {

    @UsesComponent(description = "Gets data using")
    private Repository repository = new JdbcRepository();

}