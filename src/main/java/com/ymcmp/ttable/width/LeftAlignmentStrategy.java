package com.ymcmp.ttable.width;

public class LeftAlignmentStrategy implements WidthAlignmentStrategy {

    @Override
    public int getFirstCharOffset(int chars, int width) {
        return 0;
    }
}