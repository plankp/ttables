package com.ymcmp.ttable;

import java.util.Arrays;

import java.util.stream.Collectors;

import java.util.function.Consumer;

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

    private boolean confSpacingAfterRightBorder = false;
    private boolean confSpacingAfterLeftBorder = false;
    private boolean confSpacingAroundColumnDivider = true;
    private boolean confFillRowDividerAtJunction = true;

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

    public void shouldPlaceSpacingAfterRightBorder(boolean flag) {
        this.cached = null;
        this.confSpacingAfterRightBorder = flag;
    }

    public void shouldPlaceSpacingAfterLeftBorder(boolean flag) {
        this.cached = null;
        this.confSpacingAfterLeftBorder = flag;
    }

    public void shouldPlaceSpacingAroundColumnDivider(boolean flag) {
        this.cached = null;
        this.confSpacingAroundColumnDivider = flag;
    }

    public void shouldPickRowOverColumnDividerAtJunctions(boolean flag) {
        this.cached = null;
        this.confFillRowDividerAtJunction = flag;
    }

    private char getJunctionOrDefault(int row, int col, char defaultChar) {
        final int ch = divider.getJunctionDivider(row, col);
        return ch >= 0 ? (char) ch : defaultChar;
    }

    private String getSpecialJunction(int row, int col, String defaultStr) {
        if (defaultStr.isEmpty()) {
            return "";
        }

        final int ch = divider.getJunctionDivider(row, col);
        return ch >= 0 ? Character.toString((char) ch) : defaultStr;
    }

    public String getCell(int row, int col) {
        return Arrays.stream(table[row][col]).collect(Collectors.joining("\n"));
    }

    @Override
    public String toString() {
        if (this.cached != null) {
            return this.cached;
        }

        final StringBuilder sb = new StringBuilder();
        this.generateTable(line -> sb.append(line).append('\n'));

        final int len = sb.length();
        if (len > 0) {
            // Remove trailing '\n'
            sb.deleteCharAt(len - 1);
        }

        final String result = sb.toString();
        this.cached = result;
        return result;
    }

    public void generateTable(Consumer<? super String> consumer) {
        this.generateTopBorder(consumer);

        for (int i = 0; i < this.rows; ++i) {
            this.generateRow(consumer, i);
            this.generateHorizontalDivider(consumer, i);
        }

        this.generateBottomBorder(consumer);
    }

    public void generateRow(Consumer<? super String> consumer, int rowIdx) {
        final String[][] row = this.table[rowIdx];
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.rowMaxLength[rowIdx]; ++i) {
            sb.setLength(0);

            final String leftBar = this.border.getLeftBarElement();
            if (!leftBar.isEmpty()) {
                sb.append(leftBar);
                if (this.confSpacingAfterLeftBorder) {
                    sb.append(' ');
                }
            }

            for (int j = 0; j < this.columns; ++j) {
                final String[] cellLines = row[j];
                sb.append(cellLines[i]).append(' ');

                final int colDiv = this.divider.getColumnDivider(j);
                if (colDiv >= 0) {
                    if (!this.confSpacingAroundColumnDivider) {
                        sb.deleteCharAt(sb.length() - 1);
                    }

                    sb.append((char) colDiv);

                    if (this.confSpacingAroundColumnDivider) {
                        sb.append(' ');
                    }
                }
            }

            sb.deleteCharAt(sb.length() - 1);

            final String rightBar = this.border.getRightBarElement();
            if (!rightBar.isEmpty()) {
                if (this.confSpacingAfterRightBorder) {
                    sb.append(' ');
                }
                sb.append(rightBar);
            }

            consumer.accept(sb.toString());
        }
    }

    public void generateTopBorder(Consumer<? super String> consumer) {
        if (this.border.hasTopBorder()) {
            consumer.accept(this.generateHorizontalLine(
                    -1,
                    this.border.getTopLeftCorner(),
                    this.border.getTopBarElement(),
                    this.border.getTopRightCorner()));
        }
    }

    public void generateBottomBorder(Consumer<? super String> consumer) {
        if (this.border.hasBottomBorder()) {
            consumer.accept(this.generateHorizontalLine(
                    this.rows,
                    this.border.getBottomLeftCorner(),
                    this.border.getBottomBarElement(),
                    this.border.getBottomRightCorner()));
        }
    }

    public void generateHorizontalDivider(Consumer<? super String> consumer, int rowIdx) {
        final int rowDiv = this.divider.getRowDivider(rowIdx);
        if (rowDiv >= 0) {
            consumer.accept(this.generateHorizontalLine(
                    rowIdx,
                    this.border.getLeftBarElement(),
                    (char) rowDiv,
                    this.border.getRightBarElement()));
        }
    }

    private String generateHorizontalLine(int rowIdx, String leftElement, char barElement, String rightElement) {
        final StringBuilder sb = new StringBuilder();

        final String leftBar = this.getSpecialJunction(rowIdx, -1, leftElement);
        if (!leftBar.isEmpty()) {
            sb.append(leftBar);
            if (this.confSpacingAfterLeftBorder) {
                sb.append(barElement);
            }
        }

        for (int j = 0; j < this.columns; ++j) {
            final int limit = this.colMaxLength[j] + 1;
            for (int i = 0; i < limit; ++i) {
                sb.append(barElement);
            }

            final int colDiv = this.divider.getColumnDivider(j);
            if (colDiv >= 0) {
                if (!this.confSpacingAroundColumnDivider) {
                    sb.deleteCharAt(sb.length() - 1);
                }

                sb.append(this.getJunctionOrDefault(rowIdx, j,
                        this.confFillRowDividerAtJunction ? barElement : (char) colDiv));

                if (this.confSpacingAroundColumnDivider) {
                    sb.append(barElement);
                }
            }
        }

        sb.deleteCharAt(sb.length() - 1);

        final String rightBar = this.getSpecialJunction(rowIdx, this.columns, rightElement);
        if (!rightBar.isEmpty()) {
            if (this.confSpacingAfterRightBorder) {
                sb.append(barElement);
            }
            sb.append(rightBar);
        }

        return sb.toString();
    }
}
