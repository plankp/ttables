package com.ymcmp.ttable.width;

public class RightAlignmentStrategy implements WidthAlignmentStrategy {

    @Override
    public int getFirstCharOffset(int chars, int width) {
        return width - chars;
    }
}