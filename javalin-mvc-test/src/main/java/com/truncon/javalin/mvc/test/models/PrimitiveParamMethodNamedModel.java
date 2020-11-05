package com.truncon.javalin.mvc.test.models;

import com.truncon.javalin.mvc.api.Named;

public final class PrimitiveParamMethodNamedModel {
    private boolean booleanValue;
    private int intValue;
    private double doubleValue;
    private byte byteValue;
    private short shortValue;
    private float floatValue;
    private char charValue;
    private long longValue;

    public int getInteger() {
        return intValue;
    }

    @Named("aInteger")
    public void setInteger(int value) {
        this.intValue = value;
    }

    public boolean getBoolean() {
        return booleanValue;
    }

    @Named("aBoolean")
    public void setBoolean(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public double getDouble() {
        return doubleValue;
    }

    @Named("aDouble")
    public void setDouble(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public byte getByte() {
        return byteValue;
    }

    @Named("aByte")
    public void setByte(byte byteValue) {
        this.byteValue = byteValue;
    }

    public short getShort() {
        return shortValue;
    }

    @Named("aShort")
    public void setShort(short shortValue) {
        this.shortValue = shortValue;
    }

    public float getFloat() {
        return floatValue;
    }

    @Named("aFloat")
    public void setFloat(float floatValue) {
        this.floatValue = floatValue;
    }

    public char getChar() {
        return charValue;
    }

    @Named("aChar")
    public void setChar(char charValue) {
        this.charValue = charValue;
    }

    public long getLong() {
        return longValue;
    }

    @Named("aLong")
    public void setLong(long longValue) {
        this.longValue = longValue;
    }
}
