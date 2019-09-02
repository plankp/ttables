package com.ymcmp.ttable.height;

import com.ymcmp.ttable.Range;

public class BottomAlignmentStrategy implements HeightAlignmentStrategy {

    @Override
    public Range getLineRange(int lines, int height) {
        if (lines >= height) {
            return Range.upperExclusiveRange(0, height);
        }
        return Range.upperExclusiveRange(height - lines, height);
    }
}