package com.truncon.javalin.mvc.test.models;

import com.truncon.javalin.mvc.api.Named;

public final class PrimitiveParamFieldNamedModel {
    @Named("aBoolean")
    public boolean booleanValue;
    @Named("aInteger")
    public int intValue;
    @Named("aDouble")
    public double doubleValue;
    @Named("aByte")
    public byte byteValue;
    @Named("aShort")
    public short shortValue;
    @Named("aFloat")
    public float floatValue;
    @Named("aChar")
    public char charValue;
    @Named("aLong")
    public long longValue;
}
