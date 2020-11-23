package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.api.MvcComponent;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component
@MvcComponent
public interface WebContainer {
    WebContainer getContainer();
}
