package com.ymcmp.ttable;

import java.util.Map;
import java.util.Arrays;
import java.util.LinkedHashMap;

import com.ymcmp.ttable.divider.DividerBuilder;

import org.junit.Test;

import static org.junit.Assert.*;

public class TableBuilderTest {

    public static final AlignmentStrategy LEFT_CENTER_ALIGN = new AlignmentStrategy(
            new com.ymcmp.ttable.width.LeftAlignmentStrategy(),
            new com.ymcmp.ttable.height.CenterAlignmentStrategy());

    public static final AlignmentStrategy RIGHT_CENTER_ALIGN = new AlignmentStrategy(
            new com.ymcmp.ttable.width.RightAlignmentStrategy(),
            new com.ymcmp.ttable.height.CenterAlignmentStrategy());

    private TableBuilder generateAlignedData() {
        final TableBuilder fmt = new TableBuilder(4, 3);
        fmt.getCell(0, 0).setText("Foo");
        fmt.getCell(1, 0).setText("F").setPreferredAlignment(LEFT_CENTER_ALIGN);
        fmt.getCell(2, 0).setText("o").setPreferredAlignment(LEFT_CENTER_ALIGN);

        fmt.getCell(0, 1).setText("Bar");
        fmt.getCell(1, 1).setText("B");
        fmt.getCell(2, 1).setText("a");
        fmt.getCell(3, 1).setText("r");

        fmt.getCell(0, 2).setText("Baz");
        fmt.getCell(1, 2).setText("z").setPreferredAlignment(RIGHT_CENTER_ALIGN);

        return fmt;
    }

    @Test
    public void alignUsesPreferredAlignment() {
        final TableBuilder fmt = generateAlignedData();
        assertEquals(4, fmt.rows);
        assertEquals(3, fmt.columns);
        assertEquals(
                "Foo Bar Baz\n" +
                "F    B    z\n" +
                "o    a     \n" +
                "     r     " , fmt.align().toString());
    }

    @Test
    public void forceAlignIgnoresPreferredAlignment() {
        final TableBuilder fmt = generateAlignedData();
        assertEquals(4, fmt.rows);
        assertEquals(3, fmt.columns);
        assertEquals(
                "Foo Bar Baz\n" +
                " F   B   z \n" +
                " o   a     \n" +
                "     r     " , fmt.forceAlign().toString());
    }

    @Test
    public void paddingCharacterCanBeChanged() {
        final TableBuilder fmt = generateAlignedData();

        fmt.getCell(1, 0).setPaddingChar('.');
        fmt.getCell(2, 0).setPaddingChar('.');
        fmt.getCell(3, 0).setPaddingChar('.');

        fmt.getCell(2, 1).setPaddingChar('-');

        fmt.getCell(3, 2).setPaddingChar('*');

        assertEquals(4, fmt.rows);
        assertEquals(3, fmt.columns);
        assertEquals(
                "Foo Bar Baz\n" +
                ".F.  B   z \n" +
                ".o. -a-    \n" +
                "...  r  ***" , fmt.forceAlign().toString());
    }
}