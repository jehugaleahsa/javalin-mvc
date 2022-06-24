package com.truncon.javalin.mvc.test.converters;

import com.truncon.javalin.mvc.api.Converter;
import com.truncon.javalin.mvc.api.HttpContext;
import com.truncon.javalin.mvc.test.utils.Dependency;

import javax.inject.Inject;

public final class InjectionConverter {
    private final Dependency dependency;

    @Inject
    public InjectionConverter(Dependency dependency) {
        this.dependency = dependency;
    }

    @Converter("local-date-time-converter")
    public String convertTo(HttpContext context) {
        // We just ignore whatever is passed as a parameter and return the dependency's value.
        return dependency.getValue();
    }
}
