package io.javalin.mvc.injection.dagger;

import dagger.Module;
import dagger.Provides;
import io.javalin.http.Context;
import io.javalin.mvc.DefaultModelBinder;
import io.javalin.mvc.api.*;
import io.javalin.mvc.JavalinHttpContext;

@Module
public class JavalinControllerModule {
    private final Context context;

    public JavalinControllerModule(Context context) {
        this.context = context;
    }

    @Provides
    public HttpContext getHttpContext() {
        return new JavalinHttpContext(context);
    }

    @Provides
    public HttpRequest getHttpRequest(HttpContext context) {
        return context.getRequest();
    }

    @Provides
    public HttpResponse getHttpResponse(HttpContext context) {
        return context.getResponse();
    }

    @Provides
    public ModelBinder getModelBinder(HttpRequest request) {
        return new DefaultModelBinder(request);
    }
}
