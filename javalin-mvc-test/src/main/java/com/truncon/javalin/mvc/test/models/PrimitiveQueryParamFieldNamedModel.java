package com.truncon.javalin.mvc.test.models;

import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.Named;

public final class PrimitiveQueryParamFieldNamedModel {
    @FromQuery
    @Named("aBoolean")
    private boolean booleanValue;
    @FromQuery
    @Named("aInteger")
    private int intValue;
    @FromQuery
    @Named("aDouble")
    private double doubleValue;
    @FromQuery
    @Named("aByte")
    private byte byteValue;
    @FromQuery
    @Named("aShort")
    private short shortValue;
    @FromQuery
    @Named("aFloat")
    private float floatValue;
    @FromQuery
    @Named("aChar")
    private char charValue;
    @FromQuery
    @Named("aLong")
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
