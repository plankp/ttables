package com.ymcmp.ttable;

import java.util.Arrays;
import java.util.ArrayList;

import java.util.stream.Collectors;

import com.ymcmp.ttable.border.Border;
import com.ymcmp.ttable.divider.Divider;

public class TableFormatter {

    public final int rows;
    public final int columns;

    private final String[][][] table;

    private final int[] rowMaxLength;
    private final int[] colMaxLength;

    private String cached = null;

    private Border border = new Border();
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

    @Override
    public String toString() {
        if (this.cached != null) {
            return this.cached;
        }

        final ArrayList<StringBuilder> lines = new ArrayList<>(rows);

        drawTable(lines);

        final int padCount = lines.isEmpty() ? 0 : lines.get(0).length();
        final String top = this.generateTopBorder(padCount);
        final String bottom = this.generateBottomBorder(padCount);

        final String result = lines.stream()
                .map(StringBuilder::toString)
                .collect(Collectors.joining(this.generateDelimiter(), top, bottom));
        this.cached = result;
        return result;
    }

    private String generateTopBorder(final int length) {
        final StringBuilder sb = new StringBuilder();
        if (this.border.hasTopBorder()) {
            sb.append(this.border.getTopLeftCorner())
                    .append(this.border.getTopBar(length))
                    .append(this.border.getTopRightCorner())
                    .append('\n');
        }

        sb.append(this.border.getLeftBarElement());
        return sb.toString();
    }

    private String generateBottomBorder(final int length) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.border.getRightBarElement());

        if (this.border.hasBottomBorder()) {
            sb.append('\n')
                    .append(this.border.getBottomLeftCorner())
                    .append(this.border.getBottomBar(length))
                    .append(this.border.getBottomRightCorner());
        }
        return sb.toString();
    }

    private String generateDelimiter() {
        return this.border.getRightBarElement() + '\n'
                + this.border.getLeftBarElement();
    }
}
