package com.ymcmp.ttable.border;

public final class Corner {

    public static final Corner ASCII_CORNER = new Corner("+", "+", "+", "+");
    public static final Corner UNICODE_CORNER = new Corner("\u2510", "\u2518", "\u2514", "\u250C");

    public final String topLeft;
    public final String topRight;
    public final String bottomLeft;
    public final String bottomRight;

    public Corner() {
        this("", "", "", "");
    }

    public Corner(String tr, String br, String bl, String tl) {
        this.topRight = tr;
        this.bottomRight = br;
        this.bottomLeft = bl;
        this.topLeft = tl;
    }
}