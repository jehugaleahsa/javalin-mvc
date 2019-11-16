package com.truncon.javalin.mvc.test.models;

public final class PrimitiveModel {
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
