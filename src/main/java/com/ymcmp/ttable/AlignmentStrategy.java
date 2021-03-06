package com.ymcmp.ttable;

import com.ymcmp.ttable.width.WidthAlignmentStrategy;
import com.ymcmp.ttable.height.HeightAlignmentStrategy;

public final class AlignmentStrategy implements WidthAlignmentStrategy, HeightAlignmentStrategy {

    private final WidthAlignmentStrategy widthAlignmentStrategy;
    private final HeightAlignmentStrategy heightAlignmentStrategy;

    public AlignmentStrategy(WidthAlignmentStrategy wAlign, HeightAlignmentStrategy hAlign) {
        this.widthAlignmentStrategy = wAlign;
        this.heightAlignmentStrategy = hAlign;
    }

    // --- Forward WidthAlignmentStrategy methods ---

    @Override
    public Range getCharRange(int chars, int width) {
        return this.widthAlignmentStrategy.getCharRange(chars, width);
    }

    // --- Forward HeightAlignmentStrategy methods ---

    @Override
    public Range getLineRange(int lines, int height) {
        return this.heightAlignmentStrategy.getLineRange(lines, height);
    }
}