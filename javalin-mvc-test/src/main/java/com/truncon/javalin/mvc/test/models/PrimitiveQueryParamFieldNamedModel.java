package com.truncon.javalin.mvc.test.models;

import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.Named;

public final class PrimitiveQueryParamFieldNamedModel {
    @FromQuery
    @Named("aBoolean")
    public boolean booleanValue;

    @FromQuery
    @Named("aInteger")
    public int intValue;

    @FromQuery
    @Named("aDouble")
    public double doubleValue;

    @FromQuery
    @Named("aByte")
    public byte byteValue;

    @FromQuery
    @Named("aShort")
    public short shortValue;

    @FromQuery
    @Named("aFloat")
    public float floatValue;

    @FromQuery
    @Named("aChar")
    public char charValue;

    @FromQuery
    @Named("aLong")
    public long longValue;
}
