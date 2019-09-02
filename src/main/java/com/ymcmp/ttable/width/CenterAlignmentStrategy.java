package com.ymcmp.ttable.width;

import com.ymcmp.ttable.Range;

public class CenterAlignmentStrategy implements WidthAlignmentStrategy {

    @Override
    public Range getCharRange(int chars, int width) {
        if (chars >= width) {
            return Range.upperExclusiveRange(0, width);
        }
        return Range.upperExclusiveRange((width - chars) / 2, Math.min((width + chars) / 2, width));
    }
}