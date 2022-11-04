package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.api.MvcComponent;
import com.truncon.javalin.mvc.test.controllers.InjectionController;
import com.truncon.javalin.mvc.test.controllers.ws.injection.WsInjectedControllerController;
import com.truncon.javalin.mvc.test.converters.InjectionConverter;
import com.truncon.javalin.mvc.test.handlers.InjectionHandler;
import com.truncon.javalin.mvc.test.handlers.WsInjectionHandler;
import com.truncon.javalin.mvc.test.models.InjectionModel;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = AppModule.class)
@MvcComponent
public interface WebContainer {
    InjectionController getInjectionController();
    InjectionHandler getInjectionHandler();
    InjectionConverter getInjectionConverter();
    InjectionModel getInjectionModel();
    WsInjectedControllerController getWsInjectionController();
    WsInjectionHandler getWsInjectionHandler();
}
