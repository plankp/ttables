package com.ymcmp.ttable.divider;

import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.LinkedHashMap;

import org.junit.Test;

import com.ymcmp.ttable.TableUtils;
import com.ymcmp.ttable.TableFormatter;

import static com.ymcmp.ttable.border.Border.ASCII_BORDER;
import static com.ymcmp.ttable.border.Border.UNICODE_BORDER;

import static org.junit.Assert.*;

public class DividerBuilderTest {

    @Test
    public void asciiGridIsAppliedCorrectly() {
        final Object[][] data = {
            {"A", "B", "C"},
            {1, 2, 3},
            {1.0, 'q', null}
        };
        final TableFormatter fmt = TableUtils.fromArray(data).align();

        fmt.updateDivider(DividerBuilder
                .asciiGridTemplate(fmt.rows, fmt.columns)
                .build());
        assertEquals(
                " A  | B |  C  \n" +
                "----+---+-----\n" +
                " 1  | 2 |  3  \n" +
                "----+---+-----\n" +
                "1.0 | q | null", fmt.toString());

        fmt.updateBorder(ASCII_BORDER);
        assertEquals(
                "+--------------+\n" +
                "| A  | B |  C  |\n" +
                "|----+---+-----|\n" +
                "| 1  | 2 |  3  |\n" +
                "|----+---+-----|\n" +
                "|1.0 | q | null|\n" +
                "+--------------+", fmt.toString());
    }

    @Test
    public void unicodeGridIsAppliedCorrectly() {
        final Object[][] data = {
            {"A", "B", "C"},
            {1, 2, 3},
            {1.0, 'q', null}
        };
        final TableFormatter fmt = TableUtils.fromArray(data).align();

        fmt.updateDivider(DividerBuilder
                .unicodeGridTemplate(fmt.rows, fmt.columns)
                .build());
        assertEquals(
                " A  │ B │  C  \n" +
                "────┼───┼─────\n" +
                " 1  │ 2 │  3  \n" +
                "────┼───┼─────\n" +
                "1.0 │ q │ null", fmt.toString());

        fmt.updateBorder(UNICODE_BORDER);
        assertEquals(
                "┌────┬───┬─────┐\n" +
                "│ A  │ B │  C  │\n" +
                "├────┼───┼─────┤\n" +
                "│ 1  │ 2 │  3  │\n" +
                "├────┼───┼─────┤\n" +
                "│1.0 │ q │ null│\n" +
                "└────┴───┴─────┘", fmt.toString());
    }

    @Test
    public void dividersAreAppliedForHorizontalTables() {
        final Object[][] data = {
            {"A", "B", "C"},
        };
        final TableFormatter fmt = TableUtils.fromArray(data).align();
        fmt.updateDivider(DividerBuilder
                .asciiGridTemplate(fmt.rows, fmt.columns)
                .build());

        assertEquals(
                "A | B | C", fmt.toString());
    }

    @Test
    public void dividersAreAppliedForVerticalTables() {
        final Object[][] data = {
            {"A"},
            {"B"},
            {"C"},
        };
        final TableFormatter fmt = TableUtils.fromArray(data).align();
        fmt.updateDivider(DividerBuilder
                .asciiGridTemplate(fmt.rows, fmt.columns)
                .build());

        assertEquals(
                "A\n" +
                "-\n" +
                "B\n" +
                "-\n" +
                "C", fmt.toString());
    }
}