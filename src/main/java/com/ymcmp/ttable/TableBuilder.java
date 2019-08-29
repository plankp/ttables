package com.ymcmp.ttable;

import java.util.Arrays;

import com.ymcmp.ttable.width.WidthAlignmentStrategy;
import com.ymcmp.ttable.height.HeightAlignmentStrategy;

public class TableBuilder {

    public static final AlignmentStrategy DEFAULT_ALIGN_STRAT = new AlignmentStrategy(
            new com.ymcmp.ttable.width.CenterAlignmentStrategy(),
            new com.ymcmp.ttable.height.CenterAlignmentStrategy());

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

    public void setCellFromText(int row, int col, String str) {
        setCell(row, col, new Cell(str != null ? str.split("\n") : new String[0]));
    }

    public void setCellFromLines(int row, int col, String... lines) {
        setCell(row, col, new Cell(lines));
    }

    public void setCell(int row, int col, Cell cell) {
        if (cell == null) cell = new Cell(new String[0]);
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
        return this.ensureGetCell(row, col);
    }

    private Cell ensureGetCell(int row, int col) {
        Cell cell = this.table[row][col];
        if (cell == null) {
            this.table[row][col] = cell = new Cell(new String[0]);
        }
        return cell;
    }

    public TableFormatter align() {
        return align(DEFAULT_ALIGN_STRAT);
    }

    public TableFormatter align(final AlignmentStrategy alignStrat) {
        final String[][][] result = new String[rows][columns][];
        for (int i = 0; i < rows; ++i) {
            final int newHeight = rowMaxLength[i];
            for (int j = 0; j < columns; ++j) {
                result[i][j] = this.ensureGetCell(i, j).align(newHeight, colMaxLength[j], alignStrat);
            }
        }
        return new TableFormatter(result, rowMaxLength, colMaxLength);
    }

    public TableFormatter forceAlign() {
        return forceAlign(DEFAULT_ALIGN_STRAT);
    }

    public TableFormatter forceAlign(final AlignmentStrategy alignStrat) {
        final String[][][] result = new String[rows][columns][];
        for (int i = 0; i < rows; ++i) {
            final int newHeight = rowMaxLength[i];
            for (int j = 0; j < columns; ++j) {
                result[i][j] = this.ensureGetCell(i, j).forceAlign(newHeight, colMaxLength[j], alignStrat);
            }
        }
        return new TableFormatter(result, rowMaxLength, colMaxLength);
    }
}