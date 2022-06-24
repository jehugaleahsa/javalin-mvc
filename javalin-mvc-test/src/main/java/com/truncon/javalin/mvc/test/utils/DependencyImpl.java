package com.truncon.javalin.mvc.test.utils;

import javax.inject.Inject;

public final class DependencyImpl implements Dependency {
    @Inject
    public DependencyImpl() {
    }

    @Override
    public String getValue() {
        return "hello";
    }
}
