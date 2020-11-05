package com.truncon.javalin.mvc.test.models;

import com.truncon.javalin.mvc.api.FromQuery;

public final class PrimitiveQueryParamFieldModel {
    @FromQuery
    private boolean booleanValue;
    @FromQuery
    private int intValue;
    @FromQuery
    private double doubleValue;
    @FromQuery
    private byte byteValue;
    @FromQuery
    private short shortValue;
    @FromQuery
    private float floatValue;
    @FromQuery
    private char charValue;
    @FromQuery
    private long longValue;

    public int getInteger() {
        return intValue;
    }

    public void setInteger(int value) {
        this.intValue = value;
    }

    public boolean getBoolean() {
        return booleanValue;
    }

    public void setBoolean(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public double getDouble() {
        return doubleValue;
    }

    public void setDouble(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public byte getByte() {
        return byteValue;
    }

    public void setByte(byte byteValue) {
        this.byteValue = byteValue;
    }

    public short getShort() {
        return shortValue;
    }

    public void setShort(short shortValue) {
        this.shortValue = shortValue;
    }

    public float getFloat() {
        return floatValue;
    }

    public void setFloat(float floatValue) {
        this.floatValue = floatValue;
    }

    public char getChar() {
        return charValue;
    }

    public void setChar(char charValue) {
        this.charValue = charValue;
    }

    public long getLong() {
        return longValue;
    }

    public void setLong(long longValue) {
        this.longValue = longValue;
    }
}
