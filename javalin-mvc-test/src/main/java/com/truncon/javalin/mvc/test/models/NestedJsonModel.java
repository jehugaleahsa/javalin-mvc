package com.truncon.javalin.mvc.test.models;

import com.truncon.javalin.mvc.api.FromJson;

public class NestedJsonModel {
    @FromJson
    public PrimitiveModel field;
    private PrimitiveModel setter;
    private PrimitiveModel parameter;

    public PrimitiveModel getSetter() {
        return setter;
    }

    @FromJson
    public void setSetter(PrimitiveModel model) {
        this.setter = model;
    }

    public PrimitiveModel getParameter() {
        return parameter;
    }

    public void setParameter(@FromJson PrimitiveModel model) {
        this.parameter = model;
    }
}
