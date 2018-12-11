package com.ymcmp.ttable;

import java.util.Map;
import java.util.Arrays;
import java.util.LinkedHashMap;

import org.junit.Test;


import static com.ymcmp.ttable.border.Border.ASCII_BORDER;
import static com.ymcmp.ttable.border.Corner.ASCII_CORNER;

import static org.junit.Assert.*;

public class TableUtilsTest {

    @Test
    public void fromArrayHandlesNull() {
        final Object[][] data = {
            {"A", "B", "C"},
            {1, 2, 3},
            {1.0, 'q', null}
        };
        final TableBuilder fmt = TableUtils.fromArray(data);
        assertEquals(3, fmt.rows);
        assertEquals(3, fmt.columns);
        assertEquals(
                " A  B  C  \n" +
                " 1  2  3  \n" +
                "1.0 q null", fmt.align().toString());
    }

    @Test
    public void fromArrayPadsArray() {
        final Object[][] data = {
            {"A", "B", "C"},
            {1, 2, 3},
            {1.0, 'q'}
        };
        final TableBuilder fmt = TableUtils.fromArray(data);
        assertEquals(3, fmt.rows);
        assertEquals(3, fmt.columns);
        assertEquals(
                " A  B C\n" +
                " 1  2 3\n" +
                "1.0 q  ", fmt.align().toString());
    }

    @Test
    public void fromArrayWorksWithEmptyArrays() {
        final Object[][] data = {};
        final TableBuilder fmt = TableUtils.fromArray(data);
        assertEquals(0, fmt.rows);
        assertEquals(0, fmt.columns);
        assertEquals(
            "", fmt.align().toString());
    }

    @Test
    public void fromMapMakesTableWithTwoRows() {
        final Map<String, Integer> data = new LinkedHashMap<>();
        data.put("Foo", 1);
        data.put("Bar", -8);
        data.put("Baz", 10);
        final TableBuilder fmt = TableUtils.fromMap(data);
        assertEquals(2, fmt.rows);
        assertEquals(3, fmt.columns);
        assertEquals(
                "Foo Bar Baz\n" +
                " 1  -8  10 ", fmt.align().toString());
    }

    @Test
    public void fromMultimapMakesTableWithNRows() {
        final Map<String, String[]> data = new LinkedHashMap<>();
        data.put("Foo", new String[]{ "F", "o" });
        data.put("Bar", new String[]{ "B", "a", "r" });
        data.put("Baz", new String[]{ "z" });
        final TableBuilder fmt = TableUtils.fromMultimap(data, Arrays::asList);
        assertEquals(4, fmt.rows);
        assertEquals(3, fmt.columns);
        assertEquals(
                "Foo Bar Baz\n" +
                " F   B   z \n" +
                " o   a     \n" +
                "     r     " , fmt.align().toString());
    }

    @Test
    public void gridsAreAppliedCorrectly() {
        final Object[][] data = {
            {"A", "B", "C"},
            {1, 2, 3},
            {1.0, 'q', null}
        };
        final TableFormatter fmt = TableUtils.fromArray(data).align();
        TableUtils.applyGridDivider(fmt);
        fmt.updateBorder(ASCII_BORDER);
        fmt.updateCorner(ASCII_CORNER);

        assertEquals(
                "+--------------+\n" +
                "| A  | B |  C  |\n" +
                "|----+---+-----|\n" +
                "| 1  | 2 |  3  |\n" +
                "|----+---+-----|\n" +
                "|1.0 | q | null|\n" +
                "+--------------+", fmt.toString());
    }
}