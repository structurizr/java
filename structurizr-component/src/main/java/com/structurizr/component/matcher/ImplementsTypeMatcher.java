package com.structurizr.component.matcher;

import com.structurizr.component.Type;
import com.structurizr.util.StringUtils;
import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Set;

/**
 * Matches types where the type implements the specified interface.
 */
public class ImplementsTypeMatcher implements TypeMatcher {

    private static final Log log = LogFactory.getLog(ImplementsTypeMatcher.class);

    private final String interfaceName;

    public ImplementsTypeMatcher(String interfaceName) {
        if (StringUtils.isNullOrEmpty(interfaceName)) {
            throw new IllegalArgumentException("A fully qualified interface name must be supplied");
        }

        this.interfaceName = interfaceName;
    }

    @Override
    public boolean matches(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("A type must be specified");
        }

        if (type.getJavaClass() == null) {
            return false;
        }

        JavaClass javaClass = type.getJavaClass();
        Set<String> interfaceNames = Set.of(javaClass.getInterfaceNames());
        return interfaceNames.contains(interfaceName);
    }

    @Override
    public String toString() {
        return "ImplementsTypeMatcher{" +
                "interfaceName='" + interfaceName + '\'' +
                '}';
    }

}