package com.ymcmp.ttable.height;

import com.ymcmp.ttable.Range;

public class TopAlignmentStrategy implements HeightAlignmentStrategy {

    @Override
    public Range getLineRange(int lines, int height) {
        return Range.upperExclusiveRange(0, Math.min(lines, height));
    }
}