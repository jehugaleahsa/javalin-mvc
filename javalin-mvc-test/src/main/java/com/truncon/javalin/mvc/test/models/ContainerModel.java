package com.truncon.javalin.mvc.test.models;

import com.truncon.javalin.mvc.api.FromQuery;

public class ContainerModel {
    @FromQuery
    public String container;

    public NestedFieldModel field;

    private NestedSetterModel setter;

    public NestedSetterModel getSetter() {
        return setter;
    }

    public void setSetter(NestedSetterModel setter) {
        this.setter = setter;
    }
}
