package com.ymcmp.ttable.size;

public class LowerLimitStrategy implements CellSizeStrategy {

    public final int lowerLimit;

    private LowerLimitStrategy(int lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public static LowerLimitStrategy atLeast(int lowerLimit) {
        return new LowerLimitStrategy(lowerLimit);
    }

    @Override
    public int resolveSize(int incomingSize) {
        return Math.max(this.lowerLimit, incomingSize);
    }
}