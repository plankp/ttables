package com.ymcmp.ttable.width;

public class CenterAlignmentStrategy implements WidthAlignmentStrategy {

    @Override
    public int getFirstCharOffset(int chars, int width) {
        return (width - chars) / 2;
    }
}