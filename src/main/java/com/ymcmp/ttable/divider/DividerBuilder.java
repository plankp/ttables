package com.ymcmp.ttable.divider;

import java.util.Map;
import java.util.HashMap;

public class DividerBuilder {

    protected final Map<Integer, Character> rows;
    protected final Map<Integer, Character> cols;
    protected final Map<Integer, Map<Integer, Character>> cross;

    public DividerBuilder() {
        this.rows = new HashMap<>();
        this.cols = new HashMap<>();
        this.cross = new HashMap<>();
    }

    public static DividerBuilder gridDividerTemplate(int rowCount, int columnCount) {
        return gridDividerTemplate(rowCount, columnCount, '-', '|', '+');
    }

    public static DividerBuilder gridDividerTemplate(int rowCount, int columnCount, final char row, final char col, final char intersect) {
        final DividerBuilder div = new DividerBuilder();

        for (int i = 0; i < columnCount - 1; ++i) {
            div.addColumn(i, col);
        }

        for (int i = 0; i < rowCount - 1; ++i) {
            div.addRow(i, row);
            for (int j = 0; j < columnCount - 1; ++j) {
                div.addJunction(i, j, intersect);
            }
        }

        return div;
    }

    public Divider build() {
        return new Divider(
                new HashMap<>(rows), new HashMap<>(cols), new HashMap<>(cross));
    }

    public DividerBuilder reset() {
        this.rows.clear();
        this.cols.clear();
        this.cross.clear();
        return this;
    }

    public DividerBuilder addRow(int row, char unit) {
        this.rows.put(row, unit);
        return this;
    }

    public DividerBuilder addRows(Map<Integer, Character> rows) {
        this.rows.putAll(rows);
        return this;
    }

    public DividerBuilder addColumn(int col, char unit) {
        this.cols.put(col, unit);
        return this;
    }

    public DividerBuilder addColumns(Map<Integer, Character> cols) {
        this.cols.putAll(cols);
        return this;
    }

    public DividerBuilder addJunction(int row, int col, char unit) {
        Map<Integer, Character> map = cross.get(row);
        if (map == null) {
            cross.put(row, (map = new HashMap<>()));
        }
        map.put(col, unit);
        return this;
    }

    public DividerBuilder removeRow(int row) {
        rows.remove(row);
        return this;
    }

    public DividerBuilder removeColumn(int col) {
        cols.remove(col);
        return this;
    }

    public DividerBuilder removeJunction(int row, int col) {
        final Map<Integer, Character> map = cross.get(row);
        if (map != null) {
            map.remove(col);
            if (map.isEmpty()) {
                cross.remove(row);
            }
        }
        return this;
    }
}