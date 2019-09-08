package com.ymcmp.ttable.size;

public class DynamicSizeStrategy implements CellSizeStrategy {

    @Override
    public int resolveSize(int incomingSize) {
        return incomingSize;
    }
}