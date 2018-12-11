package com.ymcmp.ttable.border;

public class CornerBuilder {

    protected String topLeft = "";
    protected String topRight = "";
    protected String bottomLeft = "";
    protected String bottomRight = "";

    public Corner build() {
        return new Corner(topRight, bottomRight, bottomLeft, topLeft);
    }

    public CornerBuilder reset() {
        this.topLeft = "";
        this.topRight = "";
        this.bottomLeft = "";
        this.bottomRight = "";
        return this;
    }

    public CornerBuilder set(String tr, String br, String bl, String tl) {
        return setTopRight(tr).setBottomRight(br).setBottomLeft(bl).setTopLeft(tl);
    }

    public CornerBuilder setTopLeft(String topLeft) {
        this.topLeft = topLeft == null ? "" : topLeft;
        return this;
    }

    public CornerBuilder setBottomLeft(String bottomLeft) {
        this.bottomLeft = bottomLeft == null ? "" : bottomLeft;
        return this;
    }

    public CornerBuilder setTopRight(String topRight) {
        this.topRight = topRight == null ? "" : topRight;
        return this;
    }

    public CornerBuilder setBottomRight(String bottomRight) {
        this.bottomRight = bottomRight == null ? "" : bottomRight;
        return this;
    }
}