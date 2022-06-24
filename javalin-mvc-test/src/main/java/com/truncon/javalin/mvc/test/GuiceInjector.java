package com.truncon.javalin.mvc.test;

import com.google.inject.Guice;
import com.google.inject.Module;
import com.truncon.javalin.mvc.api.Injector;

public final class GuiceInjector implements Injector {
    private final com.google.inject.Injector injector;
    
    public GuiceInjector(Module... modules) {
        this.injector = Guice.createInjector(modules);
    }

    @Override
    public <T> T getInstance(Class<T> clz) {
        return injector.getInstance(clz);
    }

    @Override
    public Object getHandle() {
        return injector;
    }
};
