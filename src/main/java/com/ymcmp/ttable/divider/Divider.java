package com.ymcmp.ttable.divider;

import java.util.Map;
import java.util.Collections;

public final class Divider {

    private final Map<Integer, Character> rows;
    private final Map<Integer, Character> cols;
    private final Map<Integer, Map<Integer, Character>> cross;

    public Divider() {
        this(Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap());
    }

    public Divider(Map<Integer, Character> rows, Map<Integer, Character> cols, Map<Integer, Map<Integer, Character>> cross) {
        this.rows = rows;
        this.cols = cols;
        this.cross = cross;
    }

    private static int utilGetOrDefaultChar(final Map<Integer, Character> map, int offset, int def) {
        final Character boxed = map.get(offset);
        if (boxed == null) {
            return def;
        }
        return boxed;
    }

    public int getRowDivider(int row) {
        return utilGetOrDefaultChar(rows, row, -1);
    }

    public int getColumnDivider(int col) {
        return utilGetOrDefaultChar(cols, col, -1);
    }

    public int getJunctionDivider(int row, int col) {
        final Map<Integer, Character> map = cross.get(row);
        if (map != null) {
            return utilGetOrDefaultChar(map, col, -1);
        }
        return -1;
    }
}