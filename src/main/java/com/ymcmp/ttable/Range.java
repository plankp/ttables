package com.ymcmp.ttable;

import java.util.Arrays;

import java.util.function.IntConsumer;

public final class Range {

    public final int start;
    public final int end;

    private Range(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public static Range inclusiveRange(int start, int end) {
        return new Range(start, end);
    }

    public static Range upperExclusiveRange(int start, int end) {
        return new Range(start, end - 1);
    }

    public int difference() {
        return this.end - this.start + 1;
    }

    public int clamp(int value) {
        return Math.max(this.start, Math.min(this.end, value));
    }

    public void loop(IntConsumer consumer) {
        for (int i = this.start; i <= this.end; ++i) {
            consumer.accept(i);
        }
    }

    public <T extends Object> void outerFill(T[] array, T data) {
        Arrays.fill(array, 0, this.start, data);
        Arrays.fill(array, this.end + 1, array.length, data);
    }
}