package test.StructurizrAnnotationsComponentFinderStrategy;

import com.structurizr.annotation.Component;

@Component(description = "Manages some data.")
public interface Repository {

    void getData();

}