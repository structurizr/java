package test.StructurizrAnnotationsComponentFinderStrategy;

import com.structurizr.annotation.*;

@Component(description = "Does something.")
@UsedByPerson(name = "Anonymous User", description = "Uses to do something")
@UsedByPerson(name = "Authenticated User", description = "Uses to do something too")
@UsedBySoftwareSystem(name = "External 1", description = "Uses to do something")
@UsedBySoftwareSystem(name = "External 2", description = "Uses to do something too")
@UsedByContainer(name = "Software System/Web Browser", description = "Makes calls to") // an example of using a canonical name
@UsedByContainer(name = "API Client", description = "Makes API calls to") // an example of not using a canonical name
@UsesSoftwareSystem(name = "External 1", description = "Sends information to")
public class Controller {

    @UsesComponent(description = "Reads from and writes to")
    protected Repository repository;

}
