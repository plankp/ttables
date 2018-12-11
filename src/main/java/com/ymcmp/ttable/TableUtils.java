package com.ymcmp.ttable;

import java.util.Map;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;

import java.util.function.Function;

import com.ymcmp.ttable.border.Border;
import com.ymcmp.ttable.border.Corner;
import com.ymcmp.ttable.divider.DividerBuilder;

public final class TableUtils {

    private TableUtils() {
    }

    public static <T> TableBuilder fromArray(final T[][] data) {
        return fromArray(data, String::valueOf);
    }

    public static <T> TableBuilder fromArray(final T[][] data, Function<? super T, String> mapper) {
        final int rows = data.length;
        final int cols = Arrays.stream(data)
                .mapToInt(e -> e.length)
                .max().orElse(0);
        final TableBuilder fmt = new TableBuilder(rows, cols);
        for (int i = 0; i < rows; ++i) {
            final T[] alias = data[i];
            for (int j = 0; j < cols; ++j) {
                fmt.setCellFromText(i, j, j < alias.length ? mapper.apply(alias[j]) : null);
            }
        }
        return fmt;
    }

    public static <K, V> TableBuilder fromMap(final Map<K, V> map) {
        return fromMap(map, String::valueOf);
    }

    public static <K, V> TableBuilder fromMap(final Map<K, V> map, Function<? super V, String> mapper) {
        // k1 k2 k3 ... kn
        // v1 v2 v3 ... vn

        final TableBuilder fmt = new TableBuilder(2, map.size());
        int i = 0;
        for (final Map.Entry<K, V> pair : map.entrySet()) {
            final int col = i++;
            fmt.setCellFromText(0, col, String.valueOf(pair.getKey()));
            fmt.setCellFromText(1, col, mapper.apply(pair.getValue()));
        }
        return fmt;
    }

    public static <K, V> TableBuilder fromMultimap(final Map<K, V> map, Function<? super V, ? extends Collection<String>> mapper) {
        // k1 k2 k3 ... kn
        // s1 s2 s3 ... sn
        // t1 t2 t3 ... tn

        final ArrayList<String> keys = new ArrayList<>();
        final ArrayList<String[]> values = new ArrayList<>();

        int rows = 0;
        final int cols = map.size();
        for (final Map.Entry<K, V> pair : map.entrySet()) {
            keys.add(String.valueOf(pair.getKey()));

            final String[] v = mapper.apply(pair.getValue()).toArray(new String[0]);
            values.add(v);
            if (rows < v.length) {
                rows = v.length;
            }
        }

        // + 1 because keys also take up a row
        final TableBuilder fmt = new TableBuilder(rows + 1, cols);

        // Fill keys
        for (int i = 0; i < cols; ++i) {
            fmt.setCellFromText(0, i, keys.get(i));
        }

        // Fill values
        for (int j = 0; j < cols; ++j) {
            final String[] v = values.get(j);
            for (int i = 0; i < rows; ++i) {
                fmt.setCellFromText(i + 1, j, i < v.length ? v[i] : null);
            }
        }
        return fmt;
    }

    public static void applyGridDivider(TableFormatter fmt) {
        applyGridDivider(fmt, '-', '|', '+');
    }

    public static void applyGridDivider(final TableFormatter fmt, final char row, final char col, final char intersect) {
        final DividerBuilder div = new DividerBuilder();
        for (int i = 0; i < fmt.rows - 1; ++i) {
            div.addRow(i, row);
            for (int j = 0; j < fmt.columns - 1; ++j) {
                if (i == 0) {
                    div.addColumn(j, col);
                }
                div.addJunction(i, j, intersect);
            }
        }
        fmt.updateDivider(div.build());
    }

    public static void applyGridPerimeter(final TableFormatter fmt, final char horiz, final char vert, final char corner) {
        final String sHoriz = Character.toString(horiz);
        final String sVert  = Character.toString(vert);
        final String sCross = Character.toString(corner);

        fmt.updateBorder(new Border(sHoriz, sVert, sHoriz, sVert));
        fmt.updateCorner(new Corner(sCross, sCross, sCross, sCross));
    }
}