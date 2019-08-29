package com.ymcmp.ttable.height;

public class BottomAlignmentStrategy implements HeightAlignmentStrategy {

    @Override
    public int getFirstLineOffset(int lines, int height) {
        return height - lines;
    }
}