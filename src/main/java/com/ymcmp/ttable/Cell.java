package com.ymcmp.ttable;

import java.util.Arrays;

import java.util.stream.Collectors;

public final class Cell {

    private final String[] lines;

    private WidthAlignment wAlign;
    private HeightAlignment hAlign;

    public Cell(String[] lines) {
        this(lines, null, null);
    }

    public Cell(String[] lines, WidthAlignment wAlign, HeightAlignment hAlign) {
        this.lines = lines;
        this.wAlign = wAlign;
        this.hAlign = hAlign;
    }

    public static Cell fromText(final String str) {
        return fromText(str, null, null);
    }

    public static Cell fromText(final String str, WidthAlignment wAlign, HeightAlignment hAlign) {
        return new Cell(str == null ? new String[0] : str.split("\n"), wAlign, hAlign);
    }

    public void setPreferredAlignment(WidthAlignment wAlign, HeightAlignment hAlign) {
        this.wAlign = wAlign;
        this.hAlign = hAlign;
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

    public String[] align(int newHeight, int newWidth, HeightAlignment hDefAlign, WidthAlignment wDefAlign) {
        return forceAlign(newHeight, newWidth,
                hAlign == null ? hDefAlign : hAlign,
                wAlign == null ? wDefAlign : wAlign);
    }

    public String[] forceAlign(final int newHeight, final int newWidth, final HeightAlignment hAlign, final WidthAlignment wAlign) {
        final String[] result = new String[newHeight];
        final String fullPad = new String(repeatChars(newWidth, ' '));

        // Align height
        final int start;
        switch (hAlign) {
            case TOP:
                start = 0;
                Arrays.fill(result, lines.length, result.length, fullPad);
                break;
            case CENTER:
                start = (result.length - lines.length) / 2;
                Arrays.fill(result, 0, start, fullPad);
                Arrays.fill(result, start + lines.length, result.length, fullPad);
                break;
            case BOTTOM:
                start = result.length - lines.length;
                Arrays.fill(result, 0, start, fullPad);
                break;
            default:
                throw new AssertionError("Unhandled height alignment " + hAlign);
        }

        System.arraycopy(lines, 0, result, start, lines.length);

        // Align width
        // Only need to process $lines.length from $start in $result[]
        // since the other lines are added in and are already padded
        for (int i = 0; i < lines.length; ++i) {
            final int offset = start + i;
            final String original = result[offset];
            final int padWidth = newWidth - original.length();

            if (padWidth < 1) {
                // No padding needed
                continue;
            }

            final StringBuilder sb = new StringBuilder(original);
            switch (wAlign) {
                case LEFT:
                    sb.append(repeatChars(padWidth, ' '));
                    break;
                case CENTER: {
                    final int midpoint = padWidth / 2;
                    sb.insert(0, repeatChars(midpoint, ' '));
                    sb.append(repeatChars(padWidth - midpoint, ' '));
                    break;
                }
                case RIGHT:
                    sb.insert(0, repeatChars(padWidth, ' '));
                    break;
                default:
                    throw new AssertionError("Unhandled width alignment " + wAlign);
            }
            result[offset] = sb.toString();
        }

        return result;
    }

    private static char[] repeatChars(int length, char unit) {
        final char[] array = new char[length];
        Arrays.fill(array, unit);
        return array;
    }

    @Override
    public String toString() {
        return Arrays.stream(lines).collect(Collectors.joining("\n"));
    }
}