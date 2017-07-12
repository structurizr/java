package test.StructurizrAnnotationsComponentFinderStrategy;

import com.structurizr.annotation.UsesContainer;
import com.structurizr.annotation.UsesSoftwareSystem;

@UsesContainer(name = "Database", description = "Reads from and writes to")
@UsesSoftwareSystem(name = "External 1", description = "Sends information to")
class RepositoryImpl implements Repository {

    @Override
    public void getData() {
    }

}