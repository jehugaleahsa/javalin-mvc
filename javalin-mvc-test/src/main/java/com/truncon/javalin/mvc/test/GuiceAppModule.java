package com.truncon.javalin.mvc.test;

import com.google.inject.AbstractModule;
import com.truncon.javalin.mvc.api.MvcModule;
import com.truncon.javalin.mvc.test.utils.Dependency;
import com.truncon.javalin.mvc.test.utils.DependencyImpl;

@MvcModule
public final class GuiceAppModule extends AbstractModule {
    public GuiceAppModule() {
    }

    @Override
    protected void configure() {
        super.configure();
        bind(Dependency.class).to(DependencyImpl.class);
    }
}
