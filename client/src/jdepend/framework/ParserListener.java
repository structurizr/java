package jdepend.framework;

/**
 * The <code>ParserListener</code> interface defines a listener 
 * notified upon the completion of parsing events.
 * <p>
 * Implementers of this interface register for notification using 
 * the <code>JDepend.addParseListener()</code> method.
 * 
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public interface ParserListener {

    /**
     * Called whenever a Java class file is parsed into the specified
     * <code>JavaClass</code> instance.
     * 
     * @param parsedClass Parsed Java class.
     */
    public void onParsedJavaClass(JavaClass parsedClass);

}