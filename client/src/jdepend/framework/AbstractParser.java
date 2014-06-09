package jdepend.framework;

import java.io.*;
import java.util.*;

/**
 * The <code>AbstractParser</code> class is the base class 
 * for classes capable of parsing files to create a 
 * <code>JavaClass</code> instance.
 * 
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public abstract class AbstractParser {

    private ArrayList parseListeners;
    private PackageFilter filter;
    public static boolean DEBUG = false;


    public AbstractParser() {
        this(new PackageFilter());
    }

    public AbstractParser(PackageFilter filter) {
        setFilter(filter);
        parseListeners = new ArrayList();
    }

    public void addParseListener(ParserListener listener) {
        parseListeners.add(listener);
    }

    /**
     * Registered parser listeners are informed that the resulting
     * <code>JavaClass</code> was parsed.
     */
    public abstract JavaClass parse(InputStream is) throws IOException;

    /**
     * Informs registered parser listeners that the specified
     * <code>JavaClass</code> was parsed.
     * 
     * @param jClass Parsed Java class.
     */
    protected void onParsedJavaClass(JavaClass jClass) {
        for (Iterator i = parseListeners.iterator(); i.hasNext();) {
            ((ParserListener) i.next()).onParsedJavaClass(jClass);
        }
    }

    protected PackageFilter getFilter() {
        if (filter == null) {
            setFilter(new PackageFilter());
        }
        return filter;
    }

    protected void setFilter(PackageFilter filter) {
        this.filter = filter;
    }

    protected void debug(String message) {
        if (DEBUG) {
            System.err.println(message);
        }
    }
}