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

        final Range lineRange;
        if (this.lines.length >= newHeight) {
            // Text is already overflowing, ignore height-alignment and truncate
            lineRange = Range.upperExclusiveRange(0, newHeight);
        } else {
            // Align height by padding empty lines
            lineRange = strategy.getLineRange(this.lines.length, newHeight);
            lineRange.outerFill(result, new String(repeatChars(newWidth, this.padding)));
        }

        // Only process the region where there is actually text
        lineRange.loop(height -> {
            final String line = this.lines[height - lineRange.start];
            if (line.length() >= newWidth) {
                // Text is already overflowing, ignore width-alignment and truncate
                result[height] = line.substring(0, newWidth);
            } else {
                // Align width by padding left and/or right
                final Range charRange = strategy.getCharRange(line.length(), newWidth);
                result[height] = new StringBuilder(newWidth)
                        .append(repeatChars(charRange.start, this.padding))
                        .append(line)
                        .append(repeatChars(newWidth - charRange.end - 1, this.padding))
                        .toString();
            }
        });

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