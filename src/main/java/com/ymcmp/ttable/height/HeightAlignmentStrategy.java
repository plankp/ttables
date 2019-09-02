package com.ymcmp.ttable.height;

import com.ymcmp.ttable.Range;

public interface HeightAlignmentStrategy {

    public Range getLineRange(int lines, int height);
}