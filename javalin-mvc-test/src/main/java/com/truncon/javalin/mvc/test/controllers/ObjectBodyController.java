package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.*;
import com.truncon.javalin.mvc.test.models.BoxedModel;
import com.truncon.javalin.mvc.test.models.PrimitiveModel;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Controller
public class ObjectBodyController {
    public static final String PRIMITIVE_BODY_ROUTE = "/api/body/json/primitive";
    @HttpPost(route = PRIMITIVE_BODY_ROUTE)
    public ActionResult getPrimitiveModel(PrimitiveModel model) {
        return new JsonResult(model);
    }

    public static final String EXPLICIT_PRIMITIVE_BODY_ROUTE = "/api/body/json/primitive/explicit";
    @HttpPost(route = EXPLICIT_PRIMITIVE_BODY_ROUTE)
    public ActionResult getExplicitPrimitiveModel(@FromBody PrimitiveModel model) {
        return new JsonResult(model);
    }

    public static final String BOXED_BODY_ROUTE = "/api/body/json/boxed";
    @HttpPost(route = BOXED_BODY_ROUTE)
    public ActionResult getBoxedModel(BoxedModel model) {
        return new JsonResult(model);
    }

    public static final String EXPLICIT_BOXED_BODY_ROUTE = "/api/body/json/boxed/explicit";
    @HttpPost(route = EXPLICIT_BOXED_BODY_ROUTE)
    public ActionResult getExplicitBoxedModel(@FromBody BoxedModel model) {
        return new JsonResult(model);
    }

    public static final String INPUT_STREAM_BODY_ROUTE = "/api/body/input_stream";
    @HttpPost(route = INPUT_STREAM_BODY_ROUTE)
    public ActionResult postInputStream(InputStream stream) throws IOException {
        String decoded = new BufferedReader(new InputStreamReader(stream)).readLine();
        return new ContentResult(decoded);
    }
}
