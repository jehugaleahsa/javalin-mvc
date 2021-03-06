package com.truncon.javalin.mvc.test.models;

import com.truncon.javalin.mvc.api.FromQuery;

public final class PrimitiveQueryParamFieldModel {
    @FromQuery
    public boolean booleanValue;

    @FromQuery
    public int intValue;

    @FromQuery
    public double doubleValue;

    @FromQuery
    public byte byteValue;

    @FromQuery
    public short shortValue;

    @FromQuery
    public float floatValue;

    @FromQuery
    public char charValue;

    @FromQuery
    public long longValue;
}
