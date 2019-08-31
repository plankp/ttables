package com.ymcmp.ttable;

import java.util.Arrays;
import java.util.LinkedList;

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

    @Override
    public String toString() {
        if (this.cached != null) {
            return this.cached;
        }

        final String result = this.generateTable();
        this.cached = result;
        return result;
    }

    private String generateTable() {
        final LinkedList<String> logicalLines = new LinkedList<>();

        if (this.border.hasTopBorder()) {
            logicalLines.add(this.generateTopBorder());
        }

        for (int i = 0; i < this.rows; ++i) {
            logicalLines.add(this.generateRow(i));

            final String divider = this.generateHorizontalDivider(i);
            if (divider != null) {
                logicalLines.add(divider);
            }
        }

        if (this.border.hasBottomBorder()) {
            logicalLines.add(this.generateBottomBorder());
        }

        return logicalLines.stream()
                .collect(Collectors.joining("\n"));
    }

    private String generateRow(int rowIdx) {
        final String[][] row = this.table[rowIdx];
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.rowMaxLength[rowIdx]; ++i) {
            sb.append(this.border.getLeftBarElement());

            for (int j = 0; j < this.columns; ++j) {
                final String[] cellLines = row[j];
                sb.append(cellLines[i]).append(' ');

                final int colDiv = this.divider.getColumnDivider(j);
                if (colDiv >= 0) {
                    sb.append((char) colDiv).append(' ');
                }
            }

            sb.deleteCharAt(sb.length() - 1);
            sb.append(this.border.getRightBarElement()).append('\n');
        }

        final int len = sb.length();
        if (len > 0) {
            sb.deleteCharAt(len - 1);
        }

        return sb.toString();
    }

    private String generateHorizontalDivider(int rowIdx) {
        final int rowDiv = this.divider.getRowDivider(rowIdx);
        if (rowDiv < 0) {
            return null;
        }

        final char rowDivCh = (char) rowDiv;
        final StringBuilder sb = new StringBuilder();

        sb.append(this.border.getLeftBarElement());

        for (int j = 0; j < this.columns; ++j) {
            final char[] bottomLine = new char[this.colMaxLength[j] + 1];
            Arrays.fill(bottomLine, rowDivCh);
            sb.append(bottomLine);

            final int colDiv = this.divider.getColumnDivider(j);
            if (colDiv >= 0) {
                sb.append(this.getJunctionOrDefault(rowIdx, j, rowDivCh)).append(rowDivCh);
            }
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append(this.border.getRightBarElement());

        return sb.toString();
    }

    private String generateTopBorder() {
        final StringBuilder sb = new StringBuilder();

        int padWidth = 0;
        for (int j = 0; j < this.columns; ++j) {
            padWidth += this.colMaxLength[j] + 1;
            if (this.divider.getColumnDivider(j) >= 0) {
                padWidth += 2;
            }
        }

        padWidth -= 1;
        return this.border.getTopLeftCorner()
                + this.border.getTopBar(padWidth)
                + this.border.getTopRightCorner();
    }

    private String generateBottomBorder() {
        final StringBuilder sb = new StringBuilder();

        int padWidth = 0;
        for (int j = 0; j < this.columns; ++j) {
            padWidth += this.colMaxLength[j] + 1;
            if (this.divider.getColumnDivider(j) >= 0) {
                padWidth += 2;
            }
        }

        padWidth -= 1;
        return this.border.getBottomLeftCorner()
                + this.border.getBottomBar(padWidth)
                + this.border.getBottomRightCorner();
    }
}
