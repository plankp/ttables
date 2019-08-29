package com.ymcmp.ttable;

import java.util.Arrays;

import java.util.stream.Collectors;

public final class Cell {

    private static final char[] EMPTY_CHAR_ARRAY = new char[0];
    private static final String[] EMPTY_STR_ARRAY = new String[0];

    private String[] lines = EMPTY_STR_ARRAY;
    private char padding = ' ';
    private AlignmentStrategy preferred;

    public Cell setText(String str) {
        this.lines = (str == null) ? EMPTY_STR_ARRAY : str.split("\n");
        return this;
    }

    public Cell setLines(String[] lines) {
        this.lines = (lines == null) ? EMPTY_STR_ARRAY : lines;
        return this;
    }

    public Cell setPreferredAlignment(AlignmentStrategy preferred) {
        this.preferred = preferred;
        return this;
    }

    public Cell setPaddingChar(char padding) {
        this.padding = padding;
        return this;
    }

    public int getLineCount() {
        return lines.length;
    }

    public String getLine(int line) {
        return lines[line];
    }

    public int getMaxLineLength() {
        return Arrays.stream(lines)
                .mapToInt(String::length)
                .max().orElse(0);
    }

    public String[] align(final int newHeight, final int newWidth, final AlignmentStrategy backupStrat) {
        return forceAlign(newHeight, newWidth, this.preferred != null ? this.preferred : backupStrat);
    }

    public String[] forceAlign(final int newHeight, final int newWidth, final AlignmentStrategy strategy) {
        final String[] result = new String[newHeight];
        final String fullPad = new String(repeatChars(newWidth, this.padding));

        // Align height
        final int startHeight = strategy.getFirstLineOffset(this.lines.length, result.length);
        Arrays.fill(result, 0, startHeight, fullPad);
        Arrays.fill(result, startHeight + this.lines.length, result.length, fullPad);

        // Align width
        // Only process the region where there is actually text
        final StringBuilder buffer = new StringBuilder(newWidth);
        for (int i = 0; i < this.lines.length; ++i) {
            final int height = startHeight + i;

            final String line = this.lines[i];
            final int leftPadding = strategy.getFirstCharOffset(line.length(), newWidth);
            final int rightPadding = newWidth - line.length() - leftPadding;

            buffer.setLength(0);
            result[height] = buffer
                    .append(repeatChars(leftPadding, this.padding))
                    .append(line)
                    .append(repeatChars(rightPadding, this.padding))
                    .toString();
        }

        return result;
    }

    private static char[] repeatChars(int length, char unit) {
        if (length <= 0) {
            return EMPTY_CHAR_ARRAY;
        }

        final char[] array = new char[length];
        Arrays.fill(array, unit);
        return array;
    }

    @Override
    public String toString() {
        return Arrays.stream(lines).collect(Collectors.joining("\n"));
    }
}