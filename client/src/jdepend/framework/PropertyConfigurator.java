package jdepend.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * The <code>PropertyConfigurator</code> class contains configuration
 * information contained in the <code>jdepend.properties</code> file, 
 * if such a file exists either in the user's home directory or somewhere 
 * in the classpath.
 * 
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class PropertyConfigurator {

    private Properties properties;

    public static final String DEFAULT_PROPERTY_FILE = "jdepend.properties";

    /**
     * Constructs a <code>PropertyConfigurator</code> instance 
     * containing the properties specified in the file
     * <code>jdepend.properties</code>, if it exists.
     */
    public PropertyConfigurator() {
        this(getDefaultPropertyFile());
    }

    /**
     * Constructs a <code>PropertyConfigurator</code> instance 
     * with the specified property set.
     * 
     * @param p Property set.
     */
    public PropertyConfigurator(Properties p) {
        this.properties = p;
    }

    /**
     * Constructs a <code>PropertyConfigurator</code> instance 
     * with the specified property file.
     * 
     * @param f Property file.
     */
    public PropertyConfigurator(File f) {
        this(loadProperties(f));
    }

    public Collection getFilteredPackages() {

        Collection packages = new ArrayList();

        Enumeration e = properties.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            if (key.startsWith("ignore")) {
                String path = properties.getProperty(key);
                StringTokenizer st = new StringTokenizer(path, ",");
                while (st.hasMoreTokens()) {
                    String name = (String) st.nextToken();
                    name = name.trim();
                    packages.add(name);
                }
            }
        }

        return packages;
    }

    public Collection getConfiguredPackages() {

        Collection packages = new ArrayList();

        Enumeration e = properties.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String)e.nextElement();
            if (!key.startsWith("ignore")
                    && (!key.equals("analyzeInnerClasses"))) {
                String v = properties.getProperty(key);
                packages.add(new JavaPackage(key, new Integer(v).intValue()));
            }
        }

        return packages;
    }

    public boolean getAnalyzeInnerClasses() {

        String key = "analyzeInnerClasses";
        if (properties.containsKey(key)) {
            String value = properties.getProperty(key);
            return new Boolean(value).booleanValue();
        }

        return true;
    }

    public static File getDefaultPropertyFile() {
        String home = System.getProperty("user.home");
        return new File(home, DEFAULT_PROPERTY_FILE);
    }

    public static Properties loadProperties(File file) {

        Properties p = new Properties();

        InputStream is = null;

        try {

            is = new FileInputStream(file);

        } catch (Exception e) {
            is = PropertyConfigurator.class.getResourceAsStream("/"
                    + DEFAULT_PROPERTY_FILE);
        }

        try {
            if (is != null) {
                p.load(is);
            }
        } catch (IOException ignore) {
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ignore) {
            }
        }

        return p;
    }
}