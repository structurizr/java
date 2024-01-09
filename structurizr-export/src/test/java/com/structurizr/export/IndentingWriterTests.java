package com.structurizr.export;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IndentingWriterTests {

    @Test
    public void test_WithDefaultSettings() {
        IndentingWriter writer = new IndentingWriter();

        writer.writeLine("Line 1");
        writer.indent();
        writer.writeLine("Line 2");
        writer.indent();
        writer.writeLine("Line 3");
        writer.outdent();
        writer.writeLine("Line 4");
        writer.outdent();
        writer.writeLine("Line 4");

        assertEquals("Line 1\n" +
                "  Line 2\n" +
                "    Line 3\n" +
                "  Line 4\n" +
                "Line 4", writer.toString());
    }

    @Test
    public void test_WithSpaces() {
        IndentingWriter writer = new IndentingWriter();
        writer.setIndentType(IndentType.Spaces);
        writer.setIndentQuantity(4);

        writer.writeLine("Line 1");
        writer.indent();
        writer.writeLine("Line 2");
        writer.indent();
        writer.writeLine("Line 3");
        writer.outdent();
        writer.writeLine("Line 4");
        writer.outdent();
        writer.writeLine("Line 4");

        assertEquals("Line 1\n" +
                "    Line 2\n" +
                "        Line 3\n" +
                "    Line 4\n" +
                "Line 4", writer.toString());
    }

    @Test
    public void test_WithTabs() {
        IndentingWriter writer = new IndentingWriter();
        writer.setIndentType(IndentType.Tabs);
        writer.setIndentQuantity(1);

        writer.writeLine("Line 1");
        writer.indent();
        writer.writeLine("Line 2");
        writer.indent();
        writer.writeLine("Line 3");
        writer.outdent();
        writer.writeLine("Line 4");
        writer.outdent();
        writer.writeLine("Line 4");

        assertEquals("Line 1\n" +
                "\tLine 2\n" +
                "\t\tLine 3\n" +
                "\tLine 4\n" +
                "Line 4", writer.toString());
    }

}