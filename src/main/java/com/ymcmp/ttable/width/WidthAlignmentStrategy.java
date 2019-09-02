package com.ymcmp.ttable.width;

import com.ymcmp.ttable.Range;

public interface WidthAlignmentStrategy {

    public Range getCharRange(int chars, int width);
}