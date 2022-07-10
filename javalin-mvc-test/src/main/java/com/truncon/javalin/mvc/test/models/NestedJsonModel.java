package com.truncon.javalin.mvc.test.models;

import com.truncon.javalin.mvc.api.FromBody;

public class NestedJsonModel {
    @FromBody
    public PrimitiveModel field;
    private PrimitiveModel setter;
    private PrimitiveModel parameter;

    public PrimitiveModel getSetter() {
        return setter;
    }

    @FromBody
    public void setSetter(PrimitiveModel model) {
        this.setter = model;
    }

    public PrimitiveModel getParameter() {
        return parameter;
    }

    public void setParameter(@FromBody PrimitiveModel model) {
        this.parameter = model;
    }
}
