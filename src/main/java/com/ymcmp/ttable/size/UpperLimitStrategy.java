package com.ymcmp.ttable.size;

public class UpperLimitStrategy implements CellSizeStrategy {

    public final int upperLimit;

    private UpperLimitStrategy(int upperLimit) {
        this.upperLimit = upperLimit;
    }

    public static UpperLimitStrategy atMost(int upperLimit) {
        return new UpperLimitStrategy(upperLimit);
    }

    @Override
    public int resolveSize(int incomingSize) {
        return Math.min(this.upperLimit, incomingSize);
    }
}