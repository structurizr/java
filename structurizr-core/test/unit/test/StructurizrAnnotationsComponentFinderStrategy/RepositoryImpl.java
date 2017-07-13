package test.StructurizrAnnotationsComponentFinderStrategy;

import com.structurizr.annotation.UsesContainer;
import com.structurizr.annotation.UsesSoftwareSystem;

@UsesContainer(name = "Database", description = "Reads from and writes to", technology = "JDBC")
class RepositoryImpl implements Repository {

    @Override
    public void getData() {
    }

}