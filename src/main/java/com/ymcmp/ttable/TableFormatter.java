package com.ymcmp.ttable;

import java.util.Arrays;
import java.util.ArrayList;

import java.util.stream.Collectors;

import com.ymcmp.ttable.border.Border;
import com.ymcmp.ttable.border.Corner;
import com.ymcmp.ttable.divider.Divider;

public class TableFormatter {

    public final int rows;
    public final int columns;

    private final String[][][] table;

    private final int[] rowMaxLength;
    private final int[] colMaxLength;

    private String cached = null;

    private Border border = new Border();
    private Corner corner = new Corner();

    private Divider divider = new Divider();

    public TableFormatter(String[][][] table, int[] rowMaxLength, int[] colMaxLength) {
        this.table = table;
        this.rowMaxLength = rowMaxLength;
        this.colMaxLength = colMaxLength;

        this.rows = rowMaxLength.length;
        this.columns = colMaxLength.length;
    }

    public void updateBorder(Border border) {
        this.cached = null;
        this.border = border == null ? new Border() : border;
    }

    public void updateCorner(Corner corner) {
        this.cached = null;
        this.corner = corner == null ? new Corner() : corner;
    }

    public void updateDivider(Divider divider) {
        this.cached = null;
        this.divider = divider == null ? new Divider() : divider;
    }

    private char getJunctionOrDefault(int row, int col, char defaultChar) {
        final int ch = divider.getJunctionDivider(row, col);
        return ch >= 0 ? (char) ch : defaultChar;
    }

    public String getCell(int row, int col) {
        return Arrays.stream(table[row][col]).collect(Collectors.joining("\n"));
    }

    private void drawTable(final ArrayList<StringBuilder> lines) {
        for (int i = 0, offset = 0, start = 0; i < rows; ++i) {
            lines.add(new StringBuilder());
            final int rowDivIdx = rowMaxLength[i];
            for (int j = 0; j < columns; ++j) {
                final String[] cell = table[i][j];
                final int rowDiv = divider.getRowDivider(i);
                final int colDiv = divider.getColumnDivider(j);
                final int bound = cell.length + (rowDiv >= 0 ? 1 : 0);
                if (j == 0) {
                    start += bound;
                }

                for (int k = 0, save = offset; k < bound; ++k, ++save) {
                    while (lines.size() <= save) {
                        lines.add(new StringBuilder());
                    }

                    final StringBuilder sb = lines.get(save);
                    if (k == rowDivIdx) {
                        final char[] div = new char[colMaxLength[j]];
                        Arrays.fill(div, (char) rowDiv);
                        sb.append(div);
                    } else {
                        sb.append(cell[k]);

                        if (colDiv >= 0) {
                            sb.append(' ').append((char) colDiv);
                        }
                    }

                    if (j + 1 != columns) {
                        if (k == rowDivIdx) {
                            final char c = (char) rowDiv;
                            sb.append(c);
                            if (colDiv >= 0) {
                                sb.append(getJunctionOrDefault(i, j, c));
                                sb.append(c);
                            }
                        } else {
                            sb.append(' ');
                        }
                    }
                }
            }

            offset = start;
        }
    }

    private String generateTopBorder(final int length) {
        if (length < 1) {
            return "";
        }

        final StringBuilder prefix = new StringBuilder();
        if (!border.top.isEmpty()) {
            while (prefix.length() < length) {
                prefix.append(border.top);
            }
            prefix.insert(0, corner.topLeft).append(corner.topRight).append('\n');
        }
        prefix.append(border.left);

        return prefix.toString();
    }

    private String generateBottomBorder(final int length) {
        if (length < 1) {
            return "";
        }

        final StringBuilder suffix = new StringBuilder();
        if (!border.bottom.isEmpty()) {
            while (suffix.length() < length) {
                suffix.append(border.bottom);
            }
            suffix.insert(0, corner.bottomLeft).insert(0, '\n').append(corner.bottomRight);
        }
        suffix.insert(0, border.right);

        return suffix.toString();
    }

    @Override
    public String toString() {
        if (this.cached != null) {
            return this.cached;
        }

        final ArrayList<StringBuilder> lines = new ArrayList<>(rows);

        drawTable(lines);

        final int padCount = lines.isEmpty() ? 0 : lines.get(0).length();
        final String prefix = generateTopBorder(padCount);
        final String suffix = generateBottomBorder(padCount);

        final String result = lines.stream()
                .map(StringBuilder::toString)
                .collect(Collectors.joining(border.right + '\n' + border.left, prefix, suffix));
        this.cached = result;
        return result;
    }
}
