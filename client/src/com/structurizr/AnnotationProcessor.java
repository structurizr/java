package com.structurizr;

import com.structurizr.element.Component;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("com.structurizr.element.Component")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class AnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        JavacProcessingEnvironment javacProcessingEnv = (JavacProcessingEnvironment)processingEnv;
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Component.class);
        for (Element e : elements) {

            PackageElement packageElement = getPackage(e);
            log("Found component called " + e.getSimpleName() + " in package " + packageElement.getQualifiedName());
            log(" - " + processingEnv.getElementUtils().getDocComment(e));

            checkAccessModifiers(packageElement);
            checkComponentImplementationIsPrivate(e);
        }
        return false;
    }

    private PackageElement getPackage(Element e) {
        Element parent = e.getEnclosingElement();
        while (parent != null && parent.getKind() != ElementKind.PACKAGE) {
            parent = parent.getEnclosingElement();
        }

        if (parent != null) {
            return (PackageElement)parent;
        } else {
            return null;
        }
    }

    private void checkComponentImplementationIsPrivate(Element componentInterfaceElement) {
        PackageElement packageElement = getPackage(componentInterfaceElement);
        List<? extends Element> elements = packageElement.getEnclosedElements();
        boolean foundComponentImplementation = false;

        for (Element element : elements) {
            if (element.getKind() == ElementKind.CLASS) {
                List<? extends TypeMirror> typeMirrors = ((TypeElement)element).getInterfaces();
                for (TypeMirror typeMirror : typeMirrors) {
                    DeclaredType declaredType = (DeclaredType)typeMirror;
                    if (declaredType.asElement() == componentInterfaceElement) {
                        if (foundComponentImplementation) {
                            log("Warning: " + componentInterfaceElement.getSimpleName() + " has multiple implementations");
                        } else {
                            foundComponentImplementation = true;
                            if (element.getModifiers().contains(Modifier.PUBLIC)) {
                                log("Warning: " + componentInterfaceElement.getSimpleName() + " has a public implementation");
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkAccessModifiers(PackageElement packageElement) {
        List<? extends Element> elements = packageElement.getEnclosedElements();

        for (Element element : elements) {
            if (element.getKind() == ElementKind.CLASS) {
                if (element.getModifiers().contains(Modifier.PUBLIC)) {
                    log("Warning: " + element.getSimpleName().toString() + " is public");
                }
            }
        }
    }

    private void log(String message) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
    }

}