package com.ymcmp.ttable.width;

import com.ymcmp.ttable.Range;

public class LeftAlignmentStrategy implements WidthAlignmentStrategy {

    @Override
    public Range getCharRange(int chars, int width) {
        return Range.upperExclusiveRange(0, Math.min(chars, width));
    }
}