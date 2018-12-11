package com.ymcmp.ttable.border;

public class BorderBuilder {

    protected String left = "";
    protected String right = "";
    protected String top = "";
    protected String bottom = "";

    public Border build() {
        return new Border(top, right, bottom, left);
    }

    public BorderBuilder reset() {
        this.left = "";
        this.right = "";
        this.top = "";
        this.bottom = "";
        return this;
    }

    public BorderBuilder set(String top, String right, String bottom, String left) {
        return setTop(top).setRight(right).setBottom(bottom).setLeft(left);
    }

    public BorderBuilder setTop(String top) {
        this.top = top == null ? "" : top;
        return this;
    }

    public BorderBuilder setBottom(String bottom) {
        this.bottom = bottom == null ? "" : bottom;
        return this;
    }

    public BorderBuilder setLeft(String left) {
        this.left = left == null ? "" : left;
        return this;
    }

    public BorderBuilder setRight(String right) {
        this.right = right == null ? "" : right;
        return this;
    }
}