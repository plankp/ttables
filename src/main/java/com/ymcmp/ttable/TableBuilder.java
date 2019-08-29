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

    public Cell getCell(int row, int col) {
        return this.ensureGetCell(row, col);
    }

    private Cell ensureGetCell(int row, int col) {
        Cell cell = this.table[row][col];
        if (cell == null) {
            this.table[row][col] = cell = new Cell();
        }
        return cell;
    }

    public void recomputeCellSizes() {
        // Reset size info
        Arrays.fill(this.rowMaxLength, 0);
        Arrays.fill(this.colMaxLength, 0);

        // Fill in the maximum length for each row and column
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.columns; ++j) {
                final Cell cell = this.ensureGetCell(i, j);
                final int lineCount = cell.getLineCount();
                final int longestLineLength = cell.getMaxLineLength();

                if (this.rowMaxLength[i] < lineCount) {
                    this.rowMaxLength[i] = lineCount;
                }
                if (this.colMaxLength[j] < longestLineLength) {
                    this.colMaxLength[j] = longestLineLength;
                }
            }
        }
    }

    public TableFormatter align() {
        return align(DEFAULT_ALIGN_STRAT);
    }

    public TableFormatter align(final AlignmentStrategy alignStrat) {
        this.recomputeCellSizes();

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
        this.recomputeCellSizes();

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