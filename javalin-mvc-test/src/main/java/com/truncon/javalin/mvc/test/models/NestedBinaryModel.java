package com.truncon.javalin.mvc.test.models;

import com.truncon.javalin.mvc.api.ws.FromBinary;

public final class NestedBinaryModel {
    @FromBinary
    public byte[] field;
    private byte[] setter;
    private byte[] parameter;

    public byte[] getSetter() {
        return setter;
    }

    @FromBinary
    public void setSetter(byte[] data) {
        this.setter = data;
    }

    public byte[] getParameter() {
        return parameter;
    }

    public void setParameter(@FromBinary byte[] data) {
        this.parameter = data;
    }
}
