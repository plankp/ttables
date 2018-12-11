package com.ymcmp.ttable;

import java.util.Map;
import java.util.Arrays;
import java.util.LinkedHashMap;

import com.ymcmp.ttable.divider.DividerBuilder;

import org.junit.Test;

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

        div.addColumn(1, '|');
        fmt.updateDivider(div.build());
        assertEquals(
                "Foo | Bar | Baz\n" +
                "----+----------\n" +
                " F  |  B  |  z \n" +
                " o  |  a  |    \n" +
                "    |  r  |    " , fmt.toString());
    }
}