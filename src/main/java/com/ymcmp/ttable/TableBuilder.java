package com.ymcmp.ttable;

import java.util.Arrays;

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
    private final CellSizeStrategy[] rowSizeStrat;
    private final CellSizeStrategy[] colSizeStrat;

    public TableBuilder(final int rows, final int columns) {
        this.rows = rows;
        this.columns = columns;

        this.table = new Cell[rows][columns];
        this.rowSizeStrat = new CellSizeStrategy[rows];
        this.colSizeStrat = new CellSizeStrategy[columns];

        Arrays.fill(this.rowSizeStrat, DEFAULT_SIZE_STRAT);
        Arrays.fill(this.colSizeStrat, DEFAULT_SIZE_STRAT);
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

    public void setSizingStrategyForRow(int row, CellSizeStrategy strat) {
        this.rowSizeStrat[row] = strat != null ? strat : DEFAULT_SIZE_STRAT;
    }

    public void setSizingStrategyForColumn(int col, CellSizeStrategy strat) {
        this.colSizeStrat[col] = strat != null ? strat : DEFAULT_SIZE_STRAT;
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
            for (int j = 0; j < this.columns; ++j) {
                final Cell cell = this.ensureGetCell(i, j);

                final int resolvedLineCount = this.rowSizeStrat[i].resolveSize(cell.getLineCount());
                if (rowMaxLength[i] < resolvedLineCount) {
                    rowMaxLength[i] = resolvedLineCount;
                }

                final int resolvedLineLength = this.colSizeStrat[j].resolveSize(cell.getMaxLineLength());
                if (colMaxLength[j] < resolvedLineLength) {
                    colMaxLength[j] = resolvedLineLength;
                }
            }
        }
    }
}