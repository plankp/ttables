package com.ymcmp.ttable;

import java.util.Arrays;

import com.ymcmp.ttable.width.WidthAlignmentStrategy;
import com.ymcmp.ttable.height.HeightAlignmentStrategy;

public class TableBuilder {

    private static interface CellFormatMapper {

        public String[] map(Cell cell, int cellHeight, int cellWidth, AlignmentStrategy alignment);
    }

    public static final AlignmentStrategy DEFAULT_ALIGN_STRAT = new AlignmentStrategy(
            new com.ymcmp.ttable.width.CenterAlignmentStrategy(),
            new com.ymcmp.ttable.height.CenterAlignmentStrategy());

    public final int rows;
    public final int columns;

    private final Cell[][] table;

    public TableBuilder(final int rows, final int columns) {
        this.rows = rows;
        this.columns = columns;

        this.table = new Cell[rows][columns];
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

    public TableFormatter align() {
        return align(DEFAULT_ALIGN_STRAT);
    }

    public TableFormatter align(final AlignmentStrategy alignStrat) {
        return this.mapCellToFormat(alignStrat, Cell::align);
    }

    public TableFormatter forceAlign() {
        return forceAlign(DEFAULT_ALIGN_STRAT);
    }

    public TableFormatter forceAlign(final AlignmentStrategy alignStrat) {
        return this.mapCellToFormat(alignStrat, Cell::forceAlign);
    }

    private TableFormatter mapCellToFormat(final AlignmentStrategy alignStrat, CellFormatMapper mapper) {
        final int[] rowMaxLength = new int[rows];
        final int[] colMaxLength = new int[columns];
        this.recomputeCellSizes(rowMaxLength, colMaxLength);

        final String[][][] result = new String[rows][columns][];
        for (int i = 0; i < rows; ++i) {
            final int newHeight = rowMaxLength[i];
            for (int j = 0; j < columns; ++j) {
                result[i][j] = mapper.map(this.ensureGetCell(i, j), newHeight, colMaxLength[j], alignStrat);
            }
        }
        return new TableFormatter(result, rowMaxLength, colMaxLength);
    }

    private void recomputeCellSizes(int[] rowMaxLength, int[] colMaxLength) {
        // Fill in the maximum length for each row and column
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.columns; ++j) {
                final Cell cell = this.ensureGetCell(i, j);
                final int lineCount = cell.getLineCount();
                final int longestLineLength = cell.getMaxLineLength();

                if (rowMaxLength[i] < lineCount) {
                    rowMaxLength[i] = lineCount;
                }
                if (colMaxLength[j] < longestLineLength) {
                    colMaxLength[j] = longestLineLength;
                }
            }
        }
    }
}