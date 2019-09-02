package com.ymcmp.ttable.height;

import com.ymcmp.ttable.Range;

public class BottomAlignmentStrategy implements HeightAlignmentStrategy {

    @Override
    public Range getLineRange(int lines, int height) {
        return Range.upperExclusiveRange(height - lines, height);
    }
}