package com.ymcmp.ttable.size;

import com.ymcmp.ttable.Range;

public class RangeLimitStrategy implements CellSizeStrategy {

    public final Range range;

    public RangeLimitStrategy(Range range) {
        this.range = range;
    }

    @Override
    public int resolveSize(int incomingSize) {
        return this.range.clamp(incomingSize);
    }
}