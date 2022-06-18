package com.truncon.javalin.mvc.test.utils;

public final class DependencyImpl implements Dependency {
    public DependencyImpl() {
    }

    @Override
    public String getValue() {
        return "hello";
    }
}
