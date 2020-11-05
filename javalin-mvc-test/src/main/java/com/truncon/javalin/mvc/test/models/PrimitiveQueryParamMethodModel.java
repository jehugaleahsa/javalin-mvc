package com.truncon.javalin.mvc.test.models;

import com.truncon.javalin.mvc.api.FromQuery;

public final class PrimitiveQueryParamMethodModel {
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

    @FromQuery
    public void setInteger(int value) {
        this.intValue = value;
    }

    public boolean getBoolean() {
        return booleanValue;
    }

    @FromQuery
    public void setBoolean(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public double getDouble() {
        return doubleValue;
    }

    @FromQuery
    public void setDouble(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public byte getByte() {
        return byteValue;
    }

    @FromQuery
    public void setByte(byte byteValue) {
        this.byteValue = byteValue;
    }

    public short getShort() {
        return shortValue;
    }

    @FromQuery
    public void setShort(short shortValue) {
        this.shortValue = shortValue;
    }

    public float getFloat() {
        return floatValue;
    }

    @FromQuery
    public void setFloat(float floatValue) {
        this.floatValue = floatValue;
    }

    public char getChar() {
        return charValue;
    }

    @FromQuery
    public void setChar(char charValue) {
        this.charValue = charValue;
    }

    public long getLong() {
        return longValue;
    }

    @FromQuery
    public void setLong(long longValue) {
        this.longValue = longValue;
    }
}
