package com.ymcmp.ttable;

import java.util.HashMap;

import com.ymcmp.ttable.size.CellSizeStrategy;
import com.ymcmp.ttable.size.DynamicSizeStrategy;
import com.ymcmp.ttable.width.WidthAlignmentStrategy;
import com.ymcmp.ttable.height.HeightAlignmentStrategy;

public class TableBuilder {

    private static interface CellFormatMapper {

        public String[] map(Cell cell, int cellHeight, int cellWidth, AlignmentStrategy alignment);
    }

    public static final AlignmentStrategy DEFAULT_ALIGN_STRAT = new AlignmentStrategy(
            new com.ymcmp.ttable.width.CenterAlignmentStrategy(),
            new com.ymcmp.ttable.height.CenterAlignmentStrategy());

    public static final CellSizeStrategy DEFAULT_SIZE_STRAT = new DynamicSizeStrategy();

    public final int rows;
    public final int columns;

    private final Cell[][] table;
    private final HashMap<Integer, CellSizeStrategy> rowSizeStrat;
    private final HashMap<Integer, CellSizeStrategy> colSizeStrat;

    public TableBuilder(final int rows, final int columns) {
        this.rows = rows;
        this.columns = columns;

        this.table = new Cell[rows][columns];
        this.rowSizeStrat = new HashMap<>();
        this.colSizeStrat = new HashMap<>();
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

    public void setSizingStrategyForRow(final int row, final CellSizeStrategy strat) {
        if (row < 0 || row >= this.rows) {
            throw new IndexOutOfBoundsException("Accessing row " + row + " out of " + this.rows);
        }

        if (strat == null) {
            this.rowSizeStrat.remove(row);
        } else {
            this.rowSizeStrat.put(row, strat);
        }
    }

    public void setSizingStrategyForColumn(final int col, final CellSizeStrategy strat) {
        if (col < 0 || col >= this.columns) {
            throw new IndexOutOfBoundsException("Accessing column " + col + " out of " + this.columns);
        }

        if (strat == null) {
            this.colSizeStrat.remove(col);
        } else {
            this.colSizeStrat.put(col, strat);
        }
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
        final int[] rowMaxLength = new int[this.rows];
        final int[] colMaxLength = new int[this.columns];
        this.recomputeCellSizes(rowMaxLength, colMaxLength);

        final String[][][] result = new String[this.rows][this.columns][];
        for (int i = 0; i < this.rows; ++i) {
            final int newHeight = rowMaxLength[i];
            for (int j = 0; j < this.columns; ++j) {
                result[i][j] = mapper.map(this.ensureGetCell(i, j), newHeight, colMaxLength[j], alignStrat);
            }
        }
        return new TableFormatter(result, rowMaxLength, colMaxLength);
    }

    private void recomputeCellSizes(int[] rowMaxLength, int[] colMaxLength) {
        // Fill in the maximum length for each row and column
        for (int i = 0; i < this.rows; ++i) {
            final CellSizeStrategy currentRowSizeStrat = this.rowSizeStrat.getOrDefault(i, DEFAULT_SIZE_STRAT);
            for (int j = 0; j < this.columns; ++j) {
                final Cell cell = this.ensureGetCell(i, j);

                final int resolvedLineCount = currentRowSizeStrat.resolveSize(cell.getLineCount());
                if (rowMaxLength[i] < resolvedLineCount) {
                    rowMaxLength[i] = resolvedLineCount;
                }

                final int resolvedLineLength = this.colSizeStrat.getOrDefault(j, DEFAULT_SIZE_STRAT)
                        .resolveSize(cell.getMaxLineLength());
                if (colMaxLength[j] < resolvedLineLength) {
                    colMaxLength[j] = resolvedLineLength;
                }
            }
        }
    }
}