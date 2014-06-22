package com.structurizr;

import com.structurizr.model.Container;
import org.scannotation.AnnotationDB;

import java.io.File;
import java.util.Collection;
import java.util.Set;

public class ScannotationComponentFinder implements ComponentFinder {

    public void findComponents(Container container, Collection<String> paths) throws Exception {
        AnnotationDB db = new AnnotationDB();
        for (String path : paths) {
            db.scanArchives(new File(path).toURI().toURL());
        }
        Set<String> classesWithComponentAnnotations = db.getAnnotationIndex().get(com.structurizr.element.Component.class.getName());

        for (String classWithComponentAnnotation : classesWithComponentAnnotations) {
            container.createComponentWithClass(classWithComponentAnnotation,
                    trim(db.getAnnotationValue(classWithComponentAnnotation, "com.structurizr.element.Component", "description")));
        }
    }

    private String trim(String string) {
        if (string != null) {
            return string.replaceAll("\\\"", "");
        }

        return "";
    }

}
