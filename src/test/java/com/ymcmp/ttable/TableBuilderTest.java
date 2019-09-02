package com.ymcmp.ttable;

import java.util.Map;
import java.util.Arrays;
import java.util.LinkedHashMap;

import com.ymcmp.ttable.divider.DividerBuilder;

import org.junit.Test;

import static org.junit.Assert.*;

public class TableBuilderTest {

    public static final AlignmentStrategy LEFT_TOP_ALIGN = new AlignmentStrategy(
            new com.ymcmp.ttable.width.LeftAlignmentStrategy(),
            new com.ymcmp.ttable.height.TopAlignmentStrategy());

    public static final AlignmentStrategy LEFT_CENTER_ALIGN = new AlignmentStrategy(
            new com.ymcmp.ttable.width.LeftAlignmentStrategy(),
            new com.ymcmp.ttable.height.CenterAlignmentStrategy());

    public static final AlignmentStrategy LEFT_BOTTOM_ALIGN = new AlignmentStrategy(
            new com.ymcmp.ttable.width.LeftAlignmentStrategy(),
            new com.ymcmp.ttable.height.BottomAlignmentStrategy());

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

    @Test
    public void testHeightAlignments() {
        final TableBuilder table = new TableBuilder(3, 2);

        table.getCell(0, 0).setText("Top").setPreferredAlignment(LEFT_TOP_ALIGN);
        table.getCell(1, 0).setText("Center").setPreferredAlignment(LEFT_CENTER_ALIGN);
        table.getCell(2, 0).setText("Bottom").setPreferredAlignment(LEFT_BOTTOM_ALIGN);

        final String[] lines = {
            "Lorem ipsum dolor sit amet,",
            "consectetur adipiscing elit,",
            "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua...",
        };
        table.getCell(0, 1).setLines(lines).setPreferredAlignment(RIGHT_CENTER_ALIGN);
        table.getCell(1, 1).setLines(lines);
        table.getCell(2, 1).setLines(lines).setPreferredAlignment(LEFT_CENTER_ALIGN);

        final TableFormatter fmt = table.align();
        fmt.updateDivider(new DividerBuilder()
                .addRow(0, '-').addRow(1, '-')
                .addColumn(0, '|')
                .addJunction(0, 0, '|').addJunction(1, 0, '|')
                .build());

        assertEquals(
                "Top    |                                          Lorem ipsum dolor sit amet,\n" +
                "       |                                         consectetur adipiscing elit,\n" +
                "       | sed do eiusmod tempor incididunt ut labore et dolore magna aliqua...\n" +
                "-------|---------------------------------------------------------------------\n" +
                "       |                     Lorem ipsum dolor sit amet,                     \n" +
                "Center |                     consectetur adipiscing elit,                    \n" +
                "       | sed do eiusmod tempor incididunt ut labore et dolore magna aliqua...\n" +
                "-------|---------------------------------------------------------------------\n" +
                "       | Lorem ipsum dolor sit amet,                                         \n" +
                "       | consectetur adipiscing elit,                                        \n" +
                "Bottom | sed do eiusmod tempor incididunt ut labore et dolore magna aliqua...", fmt.toString());
    }

    @Test
    public void cellDimensionLimitsTruncatesTextIfNeeded() {
        final TableBuilder table = new TableBuilder(3, 2);

        table.getCell(0, 0).setText("Top").setPreferredAlignment(LEFT_TOP_ALIGN);
        table.getCell(1, 0).setText("Center").setPreferredAlignment(LEFT_CENTER_ALIGN);
        table.getCell(2, 0).setText("Bottom").setPreferredAlignment(LEFT_BOTTOM_ALIGN);

        final String[] lines = {
            "Lorem ipsum dolor sit amet,",
            "consectetur adipiscing elit,",
            "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua...",
        };
        table.getCell(0, 1).setLines(lines);
        table.getCell(1, 1).setLines(lines);
        table.getCell(2, 1).setText("Hello!");

        table.setMaxHeightForRow(0, 1);
        table.setMaxWidthForColumn(0, 6);
        table.setMaxWidthForColumn(1, 10);

        final TableFormatter fmt = table.align();
        fmt.updateDivider(new DividerBuilder()
                .addRow(0, '-').addRow(1, '-')
                .addColumn(0, '|')
                .addJunction(0, 0, '|').addJunction(1, 0, '|')
                .build());

        assertEquals(
                "Top    | Lorem ipsu\n" +
                "-------|-----------\n" +
                "       | Lorem ipsu\n" +
                "Center | consectetu\n" +
                "       | sed do eiu\n" +
                "-------|-----------\n" +
                "Bottom |   Hello!  ", fmt.toString());
    }
}
