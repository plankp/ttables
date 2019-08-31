package com.ymcmp.ttable;

import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;

import java.util.function.Function;

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
                fmt.getCell(i, j).setText(j < alias.length ? mapper.apply(alias[j]) : null);
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
            fmt.getCell(0, col).setText(String.valueOf(pair.getKey()));
            fmt.getCell(1, col).setText(mapper.apply(pair.getValue()));
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
            fmt.getCell(0, i).setText(keys.get(i));
        }

        // Fill values
        for (int j = 0; j < cols; ++j) {
            final String[] v = values.get(j);
            for (int i = 0; i < rows; ++i) {
                fmt.getCell(i + 1, j).setText(i < v.length ? v[i] : null);
            }
        }
        return fmt;
    }

    public static <T> TableBuilder fromTable(final List<String> columnNames, List<T> rowData, Function<? super T, ? extends List<String>> mapper) {
        final TableBuilder fmt = new TableBuilder(rowData.size() + 1, columnNames.size());

        // Fill column names on 0th row
        for (int i = 0; i < fmt.columns; ++i) {
            fmt.getCell(0, i).setText(columnNames.get(i));
        }

        // Fill values
        for (int i = 0; i < fmt.rows - 1; ++i) {
            final List<String> colData = mapper.apply(rowData.get(i));
            for (int j = 0; j < fmt.columns; ++j) {
                fmt.getCell(i + 1, j).setText(j < colData.size() ? colData.get(j) : null);
            }
        }

        return fmt;
    }

    public static void applyGridDivider(TableFormatter fmt) {
        fmt.updateDivider(DividerBuilder
                .asciiGridTemplate(fmt.rows, fmt.columns)
                .build());
    }

    public static void applyGridDivider(final TableFormatter fmt, final char row, final char col, final char intersect) {
        fmt.updateDivider(DividerBuilder
                .gridDividerTemplate(fmt.rows, fmt.columns, row, col, intersect)
                .build());
    }
}