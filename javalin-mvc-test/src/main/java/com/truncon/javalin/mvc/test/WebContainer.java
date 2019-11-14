package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.api.ControllerComponent;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component
@ControllerComponent
public interface WebContainer {
    WebContainer getContainer();
}
