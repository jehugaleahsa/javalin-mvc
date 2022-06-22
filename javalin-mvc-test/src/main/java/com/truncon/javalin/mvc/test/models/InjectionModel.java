package com.truncon.javalin.mvc.test.models;

import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.test.utils.Dependency;

import javax.inject.Inject;

public final class InjectionModel {
    private final Dependency dependency;
    private int x;

    @Inject
    public InjectionModel(Dependency dependency) {
        this.dependency = dependency;
    }

    public int getX() {
        return x;
    }

    @FromQuery
    public void setX(int value) {
        this.x = value;
    }

    public String getValue() {
        return dependency.getValue();
    }
}
