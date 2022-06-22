package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.InjectionController;
import com.truncon.javalin.mvc.test.converters.InjectionConverter;
import com.truncon.javalin.mvc.test.handlers.InjectionHandler;
import com.truncon.javalin.mvc.test.models.InjectionModel;
import com.truncon.javalin.mvc.test.utils.Dependency;
import com.truncon.javalin.mvc.test.utils.DependencyImpl;
import dagger.Module;
import dagger.Provides;

@Module
public final class AppModule {
    @Provides
    public Dependency provideDependency() {
        return new DependencyImpl();
    }

    @Provides
    public InjectionController providesInjectionController(Dependency dependency) {
        return new InjectionController(dependency);
    }

    @Provides
    public InjectionHandler providesInjectionHandler(Dependency dependency) {
        return new InjectionHandler(dependency);
    }

    @Provides
    public InjectionConverter providesInjectionConverter(Dependency dependency) {
        return new InjectionConverter(dependency);
    }
}
