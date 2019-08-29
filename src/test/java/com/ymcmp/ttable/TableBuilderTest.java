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
        final Map<String, String[]> data = new LinkedHashMap<>();
        data.put("Foo", new String[]{ "F", "o" });
        data.put("Bar", new String[]{ "B", "a", "r" });
        data.put("Baz", new String[]{ "z" });
        final TableBuilder fmt = TableUtils.fromMultimap(data, Arrays::asList);

        fmt.getCell(1, 0).setPreferredAlignment(LEFT_CENTER_ALIGN);
        fmt.getCell(2, 0).setPreferredAlignment(LEFT_CENTER_ALIGN);
        fmt.getCell(3, 0).setPreferredAlignment(LEFT_CENTER_ALIGN);

        fmt.getCell(1, 2).setPreferredAlignment(RIGHT_CENTER_ALIGN);
        fmt.getCell(2, 2).setPreferredAlignment(RIGHT_CENTER_ALIGN);
        fmt.getCell(3, 2).setPreferredAlignment(RIGHT_CENTER_ALIGN);

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
}