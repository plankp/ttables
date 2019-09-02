package com.ymcmp.ttable.width;

import com.ymcmp.ttable.Range;

public class RightAlignmentStrategy implements WidthAlignmentStrategy {

    @Override
    public Range getCharRange(int chars, int width) {
        return Range.upperExclusiveRange(width - chars, width);
    }
}