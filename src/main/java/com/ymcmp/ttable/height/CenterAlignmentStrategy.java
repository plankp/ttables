package com.ymcmp.ttable.height;

public class CenterAlignmentStrategy implements HeightAlignmentStrategy {

    @Override
    public int getFirstLineOffset(int lines, int height) {
        return (height - lines) / 2;
    }
}