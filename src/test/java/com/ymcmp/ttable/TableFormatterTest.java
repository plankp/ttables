package com.ymcmp.ttable;

import java.util.Map;
import java.util.Arrays;
import java.util.LinkedHashMap;

import com.ymcmp.ttable.border.Border;
import com.ymcmp.ttable.divider.DividerBuilder;

import org.junit.Test;

import static com.ymcmp.ttable.border.Border.ASCII_BORDER;

import static org.junit.Assert.*;

public class TableFormatterTest {

    @Test
    public void dividersAreTakenIntoAccount() {
        final Map<String, String[]> data = new LinkedHashMap<>();
        data.put("Foo", new String[]{ "F", "o" });
        data.put("Bar", new String[]{ "B", "a", "r" });
        data.put("Baz", new String[]{ "z" });
        final TableBuilder builder = TableUtils.fromMultimap(data, Arrays::asList);
        final TableFormatter fmt = builder.align();

        assertEquals(4, fmt.rows);
        assertEquals(3, fmt.columns);

        final DividerBuilder div = new DividerBuilder()
                .addRow(0, '-')
                .addColumn(0, '|')
                .addJunction(0, 0, '+');
        fmt.updateDivider(div.build());
        assertEquals(
                "Foo | Bar Baz\n" +
                "----+--------\n" +
                " F  |  B   z \n" +
                " o  |  a     \n" +
                "    |  r     " , fmt.toString());
    }

    @Test
    public void rowDividerAsDefaultJunctionPoint() {
        final Map<String, String[]> data = new LinkedHashMap<>();
        data.put("Foo", new String[]{ "F", "o" });
        data.put("Bar", new String[]{ "B", "a", "r" });
        data.put("Baz", new String[]{ "z" });
        final TableBuilder builder = TableUtils.fromMultimap(data, Arrays::asList);
        final TableFormatter fmt = builder.align();

        assertEquals(4, fmt.rows);
        assertEquals(3, fmt.columns);

        final DividerBuilder div = new DividerBuilder()
                .addRow(0, '-')
                .addColumn(0, '|')
                .addJunction(0, 0, '+')
                .addColumn(1, '|');
        fmt.updateDivider(div.build());
        assertEquals(
                "Foo | Bar | Baz\n" +
                "----+----------\n" +
                " F  |  B  |  z \n" +
                " o  |  a  |    \n" +
                "    |  r  |    " , fmt.toString());
    }

    @Test
    public void columnDividerAsOptionalJunctionPoint() {
        final Map<String, String[]> data = new LinkedHashMap<>();
        data.put("Foo", new String[]{ "F", "o" });
        data.put("Bar", new String[]{ "B", "a", "r" });
        data.put("Baz", new String[]{ "z" });
        final TableBuilder builder = TableUtils.fromMultimap(data, Arrays::asList);
        final TableFormatter fmt = builder.align();

        assertEquals(4, fmt.rows);
        assertEquals(3, fmt.columns);

        final DividerBuilder div = new DividerBuilder()
                .addRow(0, '-')
                .addColumn(0, '|')
                .addJunction(0, 0, '+')
                .addColumn(1, '|');
        fmt.shouldPickRowOverColumnDividerAtJunctions(false);
        fmt.updateDivider(div.build());
        assertEquals(
                "Foo | Bar | Baz\n" +
                "----+-----|----\n" +
                " F  |  B  |  z \n" +
                " o  |  a  |    \n" +
                "    |  r  |    " , fmt.toString());
    }

    @Test
    public void specialJunctionPointsAreSupported() {
        final Object[][] data = {
            {"A", "B", "C"},
            {1, 2, 3},
            {1.0, 'q', null}
        };
        final TableFormatter fmt = TableUtils.fromArray(data).align();
        fmt.updateBorder(ASCII_BORDER);

        final DividerBuilder div = DividerBuilder
                .asciiGridTemplate(fmt.rows, fmt.columns)
                .addJunction(-1, 0, '+')
                .addJunction(-1, 1, '*')
                .addJunction(3, 0, '!')
                .addJunction(3, 1, '@')
                .addJunction(0, -1, '=')
                .addJunction(1, -1, '<')
                .addJunction(0, 3, '>')
                .addJunction(1, 3, '~');

        fmt.updateDivider(div.build());

        assertEquals(
                "+----+---*-----+\n" +
                "| A  | B |  C  |\n" +
                "=----+---+----->\n" +
                "| 1  | 2 |  3  |\n" +
                "<----+---+-----~\n" +
                "|1.0 | q | null|\n" +
                "+----!---@-----+", fmt.toString());
    }

    @Test
    public void spacingAroundColumnsIsOptional() {
        final Object[][] data = {
            {"A", "B", "C"},
            {1, 2, 3},
            {1.0, 'q', null}
        };
        final TableFormatter fmt = TableUtils.fromArray(data).align();
        fmt.updateBorder(ASCII_BORDER);

        final DividerBuilder div = DividerBuilder
                .asciiGridTemplate(fmt.rows, fmt.columns)
                .addJunction(-1, 0, '+')
                .addJunction(-1, 1, '*')
                .addJunction(3, 0, '!')
                .addJunction(3, 1, '@')
                .addJunction(0, -1, '=')
                .addJunction(1, -1, '<')
                .addJunction(0, 3, '>')
                .addJunction(1, 3, '~');

        fmt.updateDivider(div.build());

        fmt.shouldPlaceSpacingAroundColumnDivider(false);
        assertEquals(
                "+---+-*----+\n" +
                "| A |B| C  |\n" +
                "=---+-+---->\n" +
                "| 1 |2| 3  |\n" +
                "<---+-+----~\n" +
                "|1.0|q|null|\n" +
                "+---!-@----+", fmt.toString());

        fmt.shouldPlaceSpacingAfterLeftBorder(true);
        fmt.shouldPlaceSpacingAfterRightBorder(true);
        fmt.shouldPlaceSpacingAroundColumnDivider(true);
        assertEquals(
                "+-----+---*------+\n" +
                "|  A  | B |  C   |\n" +
                "=-----+---+------>\n" +
                "|  1  | 2 |  3   |\n" +
                "<-----+---+------~\n" +
                "| 1.0 | q | null |\n" +
                "+-----!---@------+", fmt.toString());
    }

    @Test
    public void spacingOptionsOnlyHappenIfColumnsAndBordersExist() {
        final Object[][] data = {
            {"A", "B", "C"},
            {1, 2, 3},
            {1.0, 'q', null}
        };
        final TableFormatter fmt = TableUtils.fromArray(data).align();

        fmt.shouldPlaceSpacingAfterLeftBorder(true);
        fmt.shouldPlaceSpacingAfterRightBorder(true);
        fmt.shouldPlaceSpacingAroundColumnDivider(true);
        assertEquals(
                " A  B  C  \n" +
                " 1  2  3  \n" +
                "1.0 q null", fmt.toString());
    }

    @Test
    public void borderAreAppliedCorrectly() {
        final Object[][] data = {
            {"A", "B", "C"},
            {1, 2, 3},
            {1.0, 'q', null}
        };
        final TableFormatter fmt = TableUtils.fromArray(data).align();
        fmt.updateBorder(new Border('*', '-', '|', '/', '#', '@', '+', ' '));

        assertEquals(
                "@**********#\n" +
                "/ A  B  C  |\n" +
                "/ 1  2  3  |\n" +
                "/1.0 q null|\n" +
                " ----------+", fmt.toString());
    }

    @Test
    public void spaceIsNotPaddedIfOnlyTwoParallelBarsExists() {
        final Object[][] data = {
            {"A", "B", "C"},
            {1, 2, 3},
            {1.0, 'q', null}
        };
        final TableFormatter fmt = TableUtils.fromArray(data).align();

        fmt.updateBorder(Border.fourBars('*', '-', null, null));
        assertEquals(
                "**********\n" +
                " A  B  C  \n" +
                " 1  2  3  \n" +
                "1.0 q null\n" +
                "----------", fmt.toString());

        fmt.updateBorder(Border.fourBars(null, null, '-', '|'));
        assertEquals(
                "| A  B  C  -\n" +
                "| 1  2  3  -\n" +
                "|1.0 q null-", fmt.toString());
    }

    @Test
    public void spaceIsPaddedIfTwoAdjacentBarsExists() {
        final Object[][] data = {
            {"A", "B", "C"},
            {1, 2, 3},
            {1.0, 'q', null}
        };
        final TableFormatter fmt = TableUtils.fromArray(data).align();

        fmt.updateBorder(Border.fourBars('*', null, null, '|'));
        assertEquals(
                " **********\n" +
                "| A  B  C  \n" +
                "| 1  2  3  \n" +
                "|1.0 q null", fmt.toString());

        fmt.updateBorder(Border.fourBars('*', null, '|', null));
        assertEquals(
                "********** \n" +
                " A  B  C  |\n" +
                " 1  2  3  |\n" +
                "1.0 q null|", fmt.toString());

        fmt.updateBorder(Border.fourBars(null, '*', '-', null));
        assertEquals(
                " A  B  C  -\n" +
                " 1  2  3  -\n" +
                "1.0 q null-\n" +
                "********** ", fmt.toString());

        fmt.updateBorder(Border.fourBars(null, '*', null, '-'));
        assertEquals(
                "- A  B  C  \n" +
                "- 1  2  3  \n" +
                "-1.0 q null\n" +
                " **********", fmt.toString());
    }

    @Test
    public void spaceIsPaddedIfOnlyThreeCornersExists() {
        final Object[][] data = {
            {"A", "B", "C"},
            {1, 2, 3},
            {1.0, 'q', null}
        };
        final TableFormatter fmt = TableUtils.fromArray(data).align();

        fmt.updateBorder(Border.fourCorners('#', '@', '+', null));
        assertEquals(
                "@          #\n" +
                "  A  B  C   \n" +
                "  1  2  3   \n" +
                " 1.0 q null \n" +
                "           +", fmt.toString());

        fmt.updateBorder(Border.fourCorners('#', '@', null, '+'));
        assertEquals(
                "@          #\n" +
                "  A  B  C   \n" +
                "  1  2  3   \n" +
                " 1.0 q null \n" +
                "+           ", fmt.toString());

        fmt.updateBorder(Border.fourCorners('#', null, '@', '+'));
        assertEquals(
                "           #\n" +
                "  A  B  C   \n" +
                "  1  2  3   \n" +
                " 1.0 q null \n" +
                "+          @", fmt.toString());

        fmt.updateBorder(Border.fourCorners(null, '#', '@', '+'));
        assertEquals(
                "#           \n" +
                "  A  B  C   \n" +
                "  1  2  3   \n" +
                " 1.0 q null \n" +
                "+          @", fmt.toString());
    }

    @Test
    public void spaceIsPaddedIfOnlyTwoCornersExists() {
        final Object[][] data = {
            {"A", "B", "C"},
            {1, 2, 3},
            {1.0, 'q', null}
        };
        final TableFormatter fmt = TableUtils.fromArray(data).align();

        fmt.updateBorder(Border.fourCorners('#', '@', null, null));
        assertEquals(
                "@          #\n" +
                "  A  B  C   \n" +
                "  1  2  3   \n" +
                " 1.0 q null ", fmt.toString());

        fmt.updateBorder(Border.fourCorners(null, null, '#', '@'));
        assertEquals(
                "  A  B  C   \n" +
                "  1  2  3   \n" +
                " 1.0 q null \n" +
                "@          #", fmt.toString());

        fmt.updateBorder(Border.fourCorners(null, '#', '@', null));
        assertEquals(
                "#           \n" +
                "  A  B  C   \n" +
                "  1  2  3   \n" +
                " 1.0 q null \n" +
                "           @", fmt.toString());

        fmt.updateBorder(Border.fourCorners('#', null, '@', null));
        assertEquals(
                "          #\n" +
                " A  B  C   \n" +
                " 1  2  3   \n" +
                "1.0 q null \n" +
                "          @", fmt.toString());

        fmt.updateBorder(Border.fourCorners(null, '#', null, '@'));
        assertEquals(
                "#          \n" +
                "  A  B  C  \n" +
                "  1  2  3  \n" +
                " 1.0 q null\n" +
                "@          ", fmt.toString());
    }

    @Test
    public void spaceIsPaddedIfOnlyOneCornerExists() {
        final Object[][] data = {
            {"A", "B", "C"},
            {1, 2, 3},
            {1.0, 'q', null}
        };
        final TableFormatter fmt = TableUtils.fromArray(data).align();

        fmt.updateBorder(Border.fourCorners('#', null, null, null));
        assertEquals(
                "          #\n" +
                " A  B  C   \n" +
                " 1  2  3   \n" +
                "1.0 q null ", fmt.toString());

        fmt.updateBorder(Border.fourCorners(null, '#', null, null));
        assertEquals(
                "#          \n" +
                "  A  B  C  \n" +
                "  1  2  3  \n" +
                " 1.0 q null", fmt.toString());

        fmt.updateBorder(Border.fourCorners(null, null, '#', null));
        assertEquals(
                " A  B  C   \n" +
                " 1  2  3   \n" +
                "1.0 q null \n" +
                "          #", fmt.toString());

        fmt.updateBorder(Border.fourCorners(null, null, null, '#'));
        assertEquals(
                "  A  B  C  \n" +
                "  1  2  3  \n" +
                " 1.0 q null\n" +
                "#          ", fmt.toString());
    }
}