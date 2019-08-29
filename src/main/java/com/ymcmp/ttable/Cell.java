package com.ymcmp.ttable;

import java.util.Arrays;

import java.util.stream.Collectors;

public final class Cell {

    private final String[] lines;

    private AlignmentStrategy preferred;

    public Cell(String[] lines) {
        this.lines = lines;
    }

    public void setPreferredAlignment(AlignmentStrategy preferred) {
        this.preferred = preferred;
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
        return alignHelper(newHeight, newWidth, this.lines, this.preferred != null ? this.preferred : backupStrat);
    }

    public String[] forceAlign(final int newHeight, final int newWidth, final AlignmentStrategy strat) {
        return alignHelper(newHeight, newWidth, this.lines, strat);
    }

    private static String[] alignHelper(final int newHeight, final int newWidth, final String[] lines, final AlignmentStrategy strategy) {
        final String[] result = new String[newHeight];
        final String fullPad = new String(repeatChars(newWidth, ' '));

        // Align height
        final int startHeight = strategy.getFirstLineOffset(lines.length, result.length);
        Arrays.fill(result, 0, startHeight, fullPad);
        Arrays.fill(result, startHeight + lines.length, result.length, fullPad);

        // Align width
        // Only process the region where there is actually text
        for (int i = 0; i < lines.length; ++i) {
            final int height = startHeight + i;

            final String line = lines[i];
            final int leftPadding = strategy.getFirstCharOffset(line.length(), newWidth);
            final int rightPadding = newWidth - line.length() - leftPadding;

            if (leftPadding <= 0 && rightPadding <= 0) {
                // No padding needed
                result[height] = line;
                continue;
            }

            result[height] = new StringBuilder(newWidth)
                    .append(repeatChars(leftPadding, ' '))
                    .append(line)
                    .append(repeatChars(rightPadding, ' '))
                    .toString();
        }

        return result;
    }

    private static char[] repeatChars(int length, char unit) {
        final char[] array = new char[Math.max(0, length)];
        Arrays.fill(array, unit);
        return array;
    }

    @Override
    public String toString() {
        return Arrays.stream(lines).collect(Collectors.joining("\n"));
    }
}