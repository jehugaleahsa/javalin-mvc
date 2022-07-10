package com.truncon.javalin.mvc.test.models;

import com.truncon.javalin.mvc.api.FromBody;

public final class NestedBinaryModel {
    @FromBody
    public byte[] field;
    private byte[] setter;
    private byte[] parameter;

    public byte[] getSetter() {
        return setter;
    }

    @FromBody
    public void setSetter(byte[] data) {
        this.setter = data;
    }

    public byte[] getParameter() {
        return parameter;
    }

    public void setParameter(@FromBody byte[] data) {
        this.parameter = data;
    }
}
