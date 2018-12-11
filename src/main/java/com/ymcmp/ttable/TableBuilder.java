package com.ymcmp.ttable;

import java.util.Arrays;

public class TableBuilder {

    public final int rows;
    public final int columns;

    private final Cell[][] table;

    private final int[] rowMaxLength;
    private final int[] colMaxLength;

    public TableBuilder(final int rows, final int columns) {
        this.rows = rows;
        this.columns = columns;

        this.table = new Cell[rows][columns];

        this.rowMaxLength = new int[rows];
        this.colMaxLength = new int[columns];
    }

    public TableFormatter align() {
        return align(WidthAlignment.CENTER, HeightAlignment.CENTER);
    }

    public TableFormatter align(final WidthAlignment wAlign, final HeightAlignment hAlign) {
        final String[][][] result = new String[rows][columns][];
        for (int i = 0; i < rows; ++i) {
            final int newHeight = rowMaxLength[i];
            for (int j = 0; j < columns; ++j) {
                result[i][j] = this.table[i][j].align(newHeight, colMaxLength[j], hAlign, wAlign);
            }
        }
        return new TableFormatter(result, rowMaxLength, colMaxLength);
    }

    public TableFormatter forceAlign() {
        return forceAlign(WidthAlignment.CENTER, HeightAlignment.CENTER);
    }

    public TableFormatter forceAlign(final WidthAlignment wAlign, final HeightAlignment hAlign) {
        final String[][][] result = new String[rows][columns][];
        for (int i = 0; i < rows; ++i) {
            final int newHeight = rowMaxLength[i];
            for (int j = 0; j < columns; ++j) {
                result[i][j] = this.table[i][j].forceAlign(newHeight, colMaxLength[j], hAlign, wAlign);
            }
        }
        return new TableFormatter(result, rowMaxLength, colMaxLength);
    }

    public void setCellFromText(int row, int col, String str) {
        setCell(row, col, Cell.fromText(str));
    }

    public void setCellFromLines(int row, int col, String... lines) {
        setCell(row, col, new Cell(lines));
    }

    public void setCell(int row, int col, Cell cell) {
        table[row][col] = cell;

        // Calculate max lengths
        final int lineCount = cell.getLineCount();
        if (rowMaxLength[row] < lineCount) {
            rowMaxLength[row] = lineCount;
        }
        final int cmp = cell.getMaxLineLength();
        if (colMaxLength[col] < cmp) {
            colMaxLength[col] = cmp;
        }
    }

    public Cell getCell(int row, int col) {
        return table[row][col];
    }
}