package com.truncon.javalin.mvc.annotations.processing;

public final class MutableInt extends Number {
    private int value;

    public MutableInt() {
    }

    public MutableInt(int value) {
        this.value = value;
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    public int incrementAndGet() {
        return ++value;
    }

    public int getAndIncrement() {
        return value++;
    }
}
