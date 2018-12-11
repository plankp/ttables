package com.ymcmp.ttable.border;

public final class Border {

    // Use this if you only want the corners to display!
    public static final Border EMPTY_BORDER = new Border(" ", " ", " ", " ");
    public static final Border ASCII_BORDER = new Border("-", "|", "-", "|");
    public static final Border UNICODE_BORDER = new Border("\u2500", "\u2502", "\u2500", "\u2502");

    public final String left;
    public final String right;
    public final String top;
    public final String bottom;

    public Border() {
        this("", "", "", "");
    }

    public Border(String top, String right, String bottom, String left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }
}