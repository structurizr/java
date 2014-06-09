package jdepend.framework;

import java.io.*;
import java.util.*;

/**
 * The <code>ClassFileParser</code> class is responsible for 
 * parsing a Java class file to create a <code>JavaClass</code> 
 * instance.
 * 
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class ClassFileParser extends AbstractParser {

    public static final int JAVA_MAGIC = 0xCAFEBABE;
    public static final int CONSTANT_UTF8 = 1;
    public static final int CONSTANT_UNICODE = 2;
    public static final int CONSTANT_INTEGER = 3;
    public static final int CONSTANT_FLOAT = 4;
    public static final int CONSTANT_LONG = 5;
    public static final int CONSTANT_DOUBLE = 6;
    public static final int CONSTANT_CLASS = 7;
    public static final int CONSTANT_STRING = 8;
    public static final int CONSTANT_FIELD = 9;
    public static final int CONSTANT_METHOD = 10;
    public static final int CONSTANT_INTERFACEMETHOD = 11;
    public static final int CONSTANT_NAMEANDTYPE = 12;
    public static final char CLASS_DESCRIPTOR = 'L';
    public static final int ACC_INTERFACE = 0x200;
    public static final int ACC_ABSTRACT = 0x400;
    
    private String fileName;
    private String className;
    private String superClassName;
    private String interfaceNames[];
    private boolean isAbstract;
    private JavaClass jClass;
    private Constant[] constantPool;
    private FieldOrMethodInfo[] fields;
    private FieldOrMethodInfo[] methods;
    private AttributeInfo[] attributes;
    private DataInputStream in;

    
    public ClassFileParser() {
        this(new PackageFilter());
    }

    public ClassFileParser(PackageFilter filter) {
        super(filter);
        reset();
    }

    private void reset() {
        className = null;
        superClassName = null;
        interfaceNames = new String[0];
        isAbstract = false;

        jClass = null;
        constantPool = new Constant[1];
        fields = new FieldOrMethodInfo[0];
        methods = new FieldOrMethodInfo[0];
        attributes = new AttributeInfo[0];
    }

    /**
     * Registered parser listeners are informed that the resulting
     * <code>JavaClass</code> was parsed.
     */
    public JavaClass parse(File classFile) throws IOException {

        this.fileName = classFile.getCanonicalPath();

        debug("\nParsing " + fileName + "...");

        FileInputStream in = null;

        try {

            in = new FileInputStream(classFile);

            return parse(in);

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    public JavaClass parse(InputStream is) throws IOException {

        reset();

        jClass = new JavaClass("Unknown");

        in = new DataInputStream(is);

        int magic = parseMagic();

        int minorVersion = parseMinorVersion();
        int majorVersion = parseMajorVersion();

        constantPool = parseConstantPool();

        parseAccessFlags();

        className = parseClassName();

        superClassName = parseSuperClassName();

        interfaceNames = parseInterfaces();

        fields = parseFields();

        methods = parseMethods();

        parseAttributes();

        addClassConstantReferences();

        onParsedJavaClass(jClass);

        return jClass;
    }

    private int parseMagic() throws IOException {
        int magic = in.readInt();
        if (magic != JAVA_MAGIC) {
            throw new IOException("Invalid class file: " + fileName);
        }

        return magic;
    }

    private int parseMinorVersion() throws IOException {
        return in.readUnsignedShort();
    }

    private int parseMajorVersion() throws IOException {
        return in.readUnsignedShort();
    }

    private Constant[] parseConstantPool() throws IOException {
        int constantPoolSize = in.readUnsignedShort();

        Constant[] pool = new Constant[constantPoolSize];

        for (int i = 1; i < constantPoolSize; i++) {

            Constant constant = parseNextConstant();

            pool[i] = constant;

            //
            // 8-byte constants use two constant pool entries
            //
            if (constant.getTag() == CONSTANT_DOUBLE
                    || constant.getTag() == CONSTANT_LONG) {
                i++;
            }
        }

        return pool;
    }

    private void parseAccessFlags() throws IOException {
        int accessFlags = in.readUnsignedShort();

        boolean isAbstract = ((accessFlags & ACC_ABSTRACT) != 0);
        boolean isInterface = ((accessFlags & ACC_INTERFACE) != 0);

        this.isAbstract = isAbstract || isInterface;
        jClass.isAbstract(this.isAbstract);

        debug("Parser: abstract = " + this.isAbstract);
    }

    private String parseClassName() throws IOException {
        int entryIndex = in.readUnsignedShort();
        String className = getClassConstantName(entryIndex);
        jClass.setName(className);
        jClass.setPackageName(getPackageName(className));

        debug("Parser: class name = " + className);
        debug("Parser: package name = " + getPackageName(className));
        
        return className;
    }

    private String parseSuperClassName() throws IOException {
        int entryIndex = in.readUnsignedShort();
        String superClassName = getClassConstantName(entryIndex);
        jClass.setSuperClassName(superClassName);
        addImport(getPackageName(superClassName));

        debug("Parser: super class name = " + superClassName);
        
        return superClassName;
    }

    private String[] parseInterfaces() throws IOException {
        int interfacesCount = in.readUnsignedShort();
        String[] interfaceNames = new String[interfacesCount];
        for (int i = 0; i < interfacesCount; i++) {
            int entryIndex = in.readUnsignedShort();
            interfaceNames[i] = getClassConstantName(entryIndex);
            addImport(getPackageName(interfaceNames[i]));

            debug("Parser: interface = " + interfaceNames[i]);
        }

        return interfaceNames;
    }

    private FieldOrMethodInfo[] parseFields() throws IOException {
        int fieldsCount = in.readUnsignedShort();
        FieldOrMethodInfo[] fields = new FieldOrMethodInfo[fieldsCount];
        for (int i = 0; i < fieldsCount; i++) {
            fields[i] = parseFieldOrMethodInfo();
            String descriptor = toUTF8(fields[i].getDescriptorIndex());
            debug("Parser: field descriptor = " + descriptor);
            String[] types = descriptorToTypes(descriptor);
            for (int t = 0; t < types.length; t++) {
                addImport(getPackageName(types[t]));
                debug("Parser: field type = " + types[t]);
            }
        }

        return fields;
    }

    private FieldOrMethodInfo[] parseMethods() throws IOException {
        int methodsCount = in.readUnsignedShort();
        FieldOrMethodInfo[] methods = new FieldOrMethodInfo[methodsCount];
        for (int i = 0; i < methodsCount; i++) {
            methods[i] = parseFieldOrMethodInfo();
            String descriptor = toUTF8(methods[i].getDescriptorIndex());
            debug("Parser: method descriptor = " + descriptor);
            String[] types = descriptorToTypes(descriptor);
            for (int t = 0; t < types.length; t++) {
                if (types[t].length() > 0) {
                    addImport(getPackageName(types[t]));
                    debug("Parser: method type = " + types[t]);
                }
            }
        }

        return methods;
    }

    private Constant parseNextConstant() throws IOException {

        Constant result;

        byte tag = in.readByte();

        switch (tag) {

        case (ClassFileParser.CONSTANT_CLASS):
        case (ClassFileParser.CONSTANT_STRING):
            result = new Constant(tag, in.readUnsignedShort());
            break;
        case (ClassFileParser.CONSTANT_FIELD):
        case (ClassFileParser.CONSTANT_METHOD):
        case (ClassFileParser.CONSTANT_INTERFACEMETHOD):
        case (ClassFileParser.CONSTANT_NAMEANDTYPE):
            result = new Constant(tag, in.readUnsignedShort(), in
                    .readUnsignedShort());
            break;
        case (ClassFileParser.CONSTANT_INTEGER):
            result = new Constant(tag, new Integer(in.readInt()));
            break;
        case (ClassFileParser.CONSTANT_FLOAT):
            result = new Constant(tag, new Float(in.readFloat()));
            break;
        case (ClassFileParser.CONSTANT_LONG):
            result = new Constant(tag, new Long(in.readLong()));
            break;
        case (ClassFileParser.CONSTANT_DOUBLE):
            result = new Constant(tag, new Double(in.readDouble()));
            break;
        case (ClassFileParser.CONSTANT_UTF8):
            result = new Constant(tag, in.readUTF());
            break;
        default:
            throw new IOException("Unknown constant: " + tag);
        }

        return result;
    }

    private FieldOrMethodInfo parseFieldOrMethodInfo() throws IOException {

        FieldOrMethodInfo result = new FieldOrMethodInfo(
                in.readUnsignedShort(), in.readUnsignedShort(), in
                        .readUnsignedShort());

        int attributesCount = in.readUnsignedShort();
        for (int a = 0; a < attributesCount; a++) {
            parseAttribute();
        }

        return result;
    }

    private void parseAttributes() throws IOException {
        int attributesCount = in.readUnsignedShort();
        attributes = new AttributeInfo[attributesCount];

        for (int i = 0; i < attributesCount; i++) {
            attributes[i] = parseAttribute();

            // Section 4.7.7 of VM Spec - Class File Format
            if (attributes[i].getName() != null) {
                if (attributes[i].getName().equals("SourceFile")) {
                    byte[] b = attributes[i].getValue();
                    int b0 = b[0] < 0 ? b[0] + 256 : b[0];
                    int b1 = b[1] < 0 ? b[1] + 256 : b[1];
                    int pe = b0 * 256 + b1;

                    String descriptor = toUTF8(pe);
                    jClass.setSourceFile(descriptor);
                }
            }
        }
    }

    private AttributeInfo parseAttribute() throws IOException {
        AttributeInfo result = new AttributeInfo();

        int nameIndex = in.readUnsignedShort();
        if (nameIndex != -1) {
            result.setName(toUTF8(nameIndex));
        }

        int attributeLength = in.readInt();
        byte[] value = new byte[attributeLength];
        for (int b = 0; b < attributeLength; b++) {
            value[b] = in.readByte();
        }

        result.setValue(value);
        return result;
    }

    private Constant getConstantPoolEntry(int entryIndex) throws IOException {

        if (entryIndex < 0 || entryIndex >= constantPool.length) {
            throw new IOException("Illegal constant pool index : " + entryIndex);
        }

        return constantPool[entryIndex];
    }

    private void addClassConstantReferences() throws IOException {
        for (int j = 1; j < constantPool.length; j++) {
            if (constantPool[j].getTag() == CONSTANT_CLASS) {
                String name = toUTF8(constantPool[j].getNameIndex());
                addImport(getPackageName(name));

                debug("Parser: class type = " + slashesToDots(name));
            }

            if (constantPool[j].getTag() == CONSTANT_DOUBLE
                    || constantPool[j].getTag() == CONSTANT_LONG) {
                j++;
            }
        }
    }

    private String getClassConstantName(int entryIndex) throws IOException {

        Constant entry = getConstantPoolEntry(entryIndex);
        if (entry == null) {
            return "";
        }
        return slashesToDots(toUTF8(entry.getNameIndex()));
    }

    private String toUTF8(int entryIndex) throws IOException {
        Constant entry = getConstantPoolEntry(entryIndex);
        if (entry.getTag() == CONSTANT_UTF8) {
            return (String) entry.getValue();
        }

        throw new IOException("Constant pool entry is not a UTF8 type: "
                + entryIndex);
    }

    private void addImport(String importPackage) {
        if ((importPackage != null) && (getFilter().accept(importPackage))) {
            jClass.addImportedPackage(new JavaPackage(importPackage));
        }
    }

    private String slashesToDots(String s) {
        return s.replace('/', '.');
    }

    private String getPackageName(String s) {
        if ((s.length() > 0) && (s.charAt(0) == '[')) {
            String types[] = descriptorToTypes(s);
            if (types.length == 0) {
                return null; // primitives
            }

            s = types[0];
        }

        s = slashesToDots(s);
        int index = s.lastIndexOf(".");
        if (index > 0) {
            return s.substring(0, index);
        }

        return "Default";
    }

    private String[] descriptorToTypes(String descriptor) {

        int typesCount = 0;
        for (int index = 0; index < descriptor.length(); index++) {
            if (descriptor.charAt(index) == ';') {
                typesCount++;
            }
        }

        String types[] = new String[typesCount];

        int typeIndex = 0;
        for (int index = 0; index < descriptor.length(); index++) {

            int startIndex = descriptor.indexOf(CLASS_DESCRIPTOR, index);
            if (startIndex < 0) {
                break;
            }

            index = descriptor.indexOf(';', startIndex + 1);
            types[typeIndex++] = descriptor.substring(startIndex + 1, index);
        }

        return types;
    }

    class Constant {

        private byte _tag;

        private int _nameIndex;

        private int _typeIndex;

        private Object _value;

        Constant(byte tag, int nameIndex) {
            this(tag, nameIndex, -1);
        }

        Constant(byte tag, Object value) {
            this(tag, -1, -1);
            _value = value;
        }

        Constant(byte tag, int nameIndex, int typeIndex) {
            _tag = tag;
            _nameIndex = nameIndex;
            _typeIndex = typeIndex;
            _value = null;
        }

        byte getTag() {
            return _tag;
        }

        int getNameIndex() {
            return _nameIndex;
        }

        int getTypeIndex() {
            return _typeIndex;
        }

        Object getValue() {
            return _value;
        }

        public String toString() {

            StringBuffer s = new StringBuffer("");

            s.append("tag: " + getTag());

            if (getNameIndex() > -1) {
                s.append(" nameIndex: " + getNameIndex());
            }

            if (getTypeIndex() > -1) {
                s.append(" typeIndex: " + getTypeIndex());
            }

            if (getValue() != null) {
                s.append(" value: " + getValue());
            }

            return s.toString();
        }
    }

    class FieldOrMethodInfo {

        private int _accessFlags;

        private int _nameIndex;

        private int _descriptorIndex;

        FieldOrMethodInfo(int accessFlags, int nameIndex, int descriptorIndex) {

            _accessFlags = accessFlags;
            _nameIndex = nameIndex;
            _descriptorIndex = descriptorIndex;
        }

        int accessFlags() {
            return _accessFlags;
        }

        int getNameIndex() {
            return _nameIndex;
        }

        int getDescriptorIndex() {
            return _descriptorIndex;
        }

        public String toString() {
            StringBuffer s = new StringBuffer("");

            try {

                s.append("\n    name (#" + getNameIndex() + ") = "
                        + toUTF8(getNameIndex()));

                s.append("\n    signature (#" + getDescriptorIndex() + ") = "
                        + toUTF8(getDescriptorIndex()));

                String[] types = descriptorToTypes(toUTF8(getDescriptorIndex()));
                for (int t = 0; t < types.length; t++) {
                    s.append("\n        type = " + types[t]);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return s.toString();
        }
    }

    class AttributeInfo {

        private String name;

        private byte[] value;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void setValue(byte[] value) {
            this.value = value;
        }

        public byte[] getValue() {
            return this.value;
        }
    }

    /**
     * Returns a string representation of this object.
     * 
     * @return String representation.
     */
    public String toString() {

        StringBuffer s = new StringBuffer();

        try {

            s.append("\n" + className + ":\n");

            s.append("\nConstants:\n");
            for (int i = 1; i < constantPool.length; i++) {
                Constant entry = getConstantPoolEntry(i);
                s.append("    " + i + ". " + entry.toString() + "\n");
                if (entry.getTag() == CONSTANT_DOUBLE
                        || entry.getTag() == CONSTANT_LONG) {
                    i++;
                }
            }

            s.append("\nClass Name: " + className + "\n");
            s.append("Super Name: " + superClassName + "\n\n");

            s.append(interfaceNames.length + " interfaces\n");
            for (int i = 0; i < interfaceNames.length; i++) {
                s.append("    " + interfaceNames[i] + "\n");
            }

            s.append("\n" + fields.length + " fields\n");
            for (int i = 0; i < fields.length; i++) {
                s.append(fields[i].toString() + "\n");
            }

            s.append("\n" + methods.length + " methods\n");
            for (int i = 0; i < methods.length; i++) {
                s.append(methods[i].toString() + "\n");
            }

            s.append("\nDependencies:\n");
            Iterator imports = jClass.getImportedPackages().iterator();
            while (imports.hasNext()) {
                JavaPackage jPackage = (JavaPackage) imports.next();
                s.append("    " + jPackage.getName() + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return s.toString();
    }

    /**
     * Test main.
     */
    public static void main(String args[]) {
        try {

            ClassFileParser.DEBUG = true;

            if (args.length <= 0) {
                System.err.println("usage: ClassFileParser <class-file>");
                System.exit(0);
            }

            ClassFileParser parser = new ClassFileParser();

            parser.parse(new File(args[0]));

            System.err.println(parser.toString());

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
