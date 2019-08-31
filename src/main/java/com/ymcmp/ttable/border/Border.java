package com.ymcmp.ttable.border;

import java.util.Arrays;

public final class Border {

    public static final Border ASCII_BORDER = new Border(
            '-', '-', '|', '|', '+', '+', '+', '+');

    public static final Border UNICODE_BORDER = new Border(
            '\u2500', '\u2500', '\u2502', '\u2502',
            '\u2510', '\u250C', '\u2518', '\u2514');

    public static final Border BOX_BRACKET = new Border(
            ' ', ' ', '\u2502', '\u2502',
            '\u2510', '\u250C', '\u2518', '\u2514');

    private static final char DEFAULT_PADDING_CHAR = ' ';

    private final Character top;
    private final Character bottom;
    private final Character right;
    private final Character left;

    private final Character topRight;
    private final Character topLeft;
    private final Character bottomRight;
    private final Character bottomLeft;

    public Border() {
        this(null, null, null, null, null, null, null, null);
    }

    public Border(Character top, Character bottom, Character right, Character left,
                  Character tr, Character tl, Character br, Character bl) {
        this.top = top;
        this.bottom = bottom;
        this.right = right;
        this.left = left;

        this.topRight = tr;
        this.topLeft = tl;
        this.bottomRight = br;
        this.bottomLeft = bl;
    }

    public static Border fourBars(Character top, Character bottom, Character right, Character left) {
        return new Border(top, bottom, right, left, null, null, null, null);
    }

    public static Border fourCorners(Character tr, Character tl, Character br, Character bl) {
        return new Border(null, null, null, null, tr, tl, br, bl);
    }

    public boolean hasTopBorder() {
        return this.top != null
            || this.topRight != null
            || this.topLeft != null;
    }

    public Character getTopBarElement() {
        if (this.hasTopBorder()) {
            return this.top != null ? this.top : DEFAULT_PADDING_CHAR;
        }
        return null;
    }

    public boolean hasBottomBorder() {
        return this.bottom != null
            || this.bottomRight != null
            || this.bottomLeft != null;
    }

    public Character getBottomBarElement() {
        if (this.hasBottomBorder()) {
            return this.bottom != null ? this.bottom : DEFAULT_PADDING_CHAR;
        }
        return null;
    }

    public boolean hasRightBorder() {
        return this.right != null
            || this.topRight != null
            || this.bottomRight != null;
    }

    public String getRightBarElement() {
        if (this.hasRightBorder()) {
            return Character.toString(
                    this.right != null ? this.right : DEFAULT_PADDING_CHAR);
        }
        return "";
    }

    public boolean hasLeftBorder() {
        return this.left != null
            || this.topLeft != null
            || this.bottomLeft != null;
    }

    public String getLeftBarElement() {
        if (this.hasLeftBorder()) {
            return Character.toString(
                    this.left != null ? this.left : DEFAULT_PADDING_CHAR);
        }
        return "";
    }

    public String getTopRightCorner() {
        if (this.hasRightBorder()) {
            return Character.toString(
                    this.topRight != null ? this.topRight : DEFAULT_PADDING_CHAR);
        }
        return "";
    }

    public String getTopLeftCorner() {
        if (this.hasLeftBorder()) {
            return Character.toString(
                    this.topLeft != null ? this.topLeft : DEFAULT_PADDING_CHAR);
        }
        return "";
    }

    public String getBottomRightCorner() {
        if (this.hasRightBorder()) {
            return Character.toString(
                    this.bottomRight != null ? this.bottomRight : DEFAULT_PADDING_CHAR);
        }
        return "";
    }

    public String getBottomLeftCorner() {
        if (this.hasLeftBorder()) {
            return Character.toString(
                    this.bottomLeft != null ? this.bottomLeft : DEFAULT_PADDING_CHAR);
        }
        return "";
    }
}