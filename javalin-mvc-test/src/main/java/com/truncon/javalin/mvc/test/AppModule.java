package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.utils.Dependency;
import com.truncon.javalin.mvc.test.utils.DependencyImpl;
import dagger.Binds;
import dagger.Module;

@Module
public interface AppModule {
    @Binds
    Dependency bindDependency(DependencyImpl impl);
}
