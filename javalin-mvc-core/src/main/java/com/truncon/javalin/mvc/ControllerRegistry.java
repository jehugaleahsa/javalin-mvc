package com.truncon.javalin.mvc;

import io.javalin.Javalin;

public interface ControllerRegistry {
    void register(Javalin app);
}
